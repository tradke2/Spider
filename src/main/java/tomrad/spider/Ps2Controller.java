package tomrad.spider;

import static tomrad.spider.Ps2ControllerConstants.ALLPRESSUREMODE;
import static tomrad.spider.Ps2ControllerConstants.ANALOGMODE;
import static tomrad.spider.Ps2ControllerConstants.BYTE_DELAY;
import static tomrad.spider.Ps2ControllerConstants.MAX_INIT_ATTEMPT;
import static tomrad.spider.Ps2ControllerConstants.MAX_READ_DELAY;
import static tomrad.spider.Ps2ControllerConstants.enterConfigMode;
import static tomrad.spider.Ps2ControllerConstants.exitConfigAllPressureMode;
import static tomrad.spider.Ps2ControllerConstants.exitConfigMode;
import static tomrad.spider.Ps2ControllerConstants.exitConfigMode2;
import static tomrad.spider.Ps2ControllerConstants.setAllPressureMode;
import static tomrad.spider.Ps2ControllerConstants.setModeAnalogLockMode;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ps2Controller {

	private final static Logger log = LoggerFactory.getLogger(Ps2Controller.class);

	// private members:
	private short _controllerMode = -1;
	int _commandPin = -1;
	int _attnPin = -1;
	int _clkPin = -1;
	int _readDelay = 0;
	private int[] _btnLastState = new int[2];
	private int[] _btnChangedState = new int[2];
	private short[] PS2data = new short[21];
	private long _last_read = 0;
	private SpiController spiController;

	private final GpioWiring _wiringPi;

	public Ps2Controller(GpioWiring wiringPi) {
		_wiringPi = wiringPi;
	}

	private static String byteArrayToString(short[] ba) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < ba.length; i++) {
			if (i > 0)
				sb.append(", ");
			sb.append(String.format("%02X", (byte) ba[i]));
		}
		return sb.append("]").toString();
	}

	public static void logHexArray(short[] hexArray) {
		log.debug(String.format("Antwort: %s", byteArrayToString(hexArray)));
		return;
	}

	/**
	 * Sets up the pins for the SPI protocol used to talk to the PS2-controller.
	 * <br>
	 * NOTE: This method uses the WiringPi numbering scheme.
	 * 
	 * @param _commandPin
	 *            The RPi pin that is connected to the COMMAND line of PS2 remote.
	 * @param _dataPin
	 *            The RPi pin that is connected to the DATA line of PS2 remote.
	 * @param _clkPin
	 *            The RPi pin that is connected to the CLOCK line of PS2 remote.
	 * @param _attnPin
	 *            The RPi pin that is connected to the ATTENTION line of PS2 remote.
	 * @return 1 - Config success. 0 - Controller is not responding.
	 */
	public void setupPins(int _commandPin, int _dataPin, int _clkPin, int _attnPin) {
		
		log.debug("setupPins: _commandPin={}, _dataPin={}, _clkPin={}, _attnPin={}", _commandPin, _dataPin, _clkPin,
				_attnPin);
		
		// INITIALIZE I/O
		this._commandPin = _commandPin;
		this._clkPin = _clkPin;
		this._attnPin = _attnPin;

		spiController = new SpiController(_wiringPi);
		spiController.setupPins(_commandPin, _dataPin, _clkPin, _attnPin);

		_controllerMode = ANALOGMODE;

		log.debug("setupPins: _controllerMode={}", String.format("%#02x", (byte) _controllerMode));
	}

	/**
	 * Initializes the I/O pins and sets up the controller for the desired mode.
	 * <br>
	 * --!! NOTE !!-- {@link #setupPins(int, int, int, int)} must be called first to
	 * set the pins for the SPI protocol..
	 * <p>
	 * TODO: <br>
	 * This function is hard coded to configure the controller for analog mode. Must
	 * also implement input parameters to choose what mode to use. If you want
	 * digital mode or analog mode with all pressures then use reInitController()
	 * </p>
	 */
	public int initializeController() {

		_readDelay = 1;
		log.debug("initializeController: _readDelay={}", _readDelay);
		
		// Set command pin and clock pin high, ready to initialize a transfer.
		_wiringPi.digitalWrite(_commandPin, 1);
		_wiringPi.digitalWrite(_clkPin, 1);

		// Read controller a few times to check if it is talking.
		readPS2();
		readPS2();

		// Initialize the read delay to be 1 millisecond.
		// Increment read_delay until controller accepts commands.
		// This is a but of dynamic debugging. Read delay usually needs to be about 2.
		// But for some controllers, especially wireless ones it needs to be a bit
		// higher.

		// Try up until readDelay = MAX_READ_DELAY
		while (true) {

			// Transmit the enter config command.
			spiController.transmitBytes(enterConfigMode);

			// Set mode to analog mode and lock it there.
			spiController.transmitBytes(setModeAnalogLockMode);

			// Return all pressures
			// self.transmitBytes(setAllPressureMode)

			// Exit config mode.
			spiController.transmitBytes(exitConfigMode);

			// Attempt to read the controller.
			readPS2();

			// If read was successful (controller indicates it is in analog mode), break
			// this config loop.
			if (PS2data[1] == _controllerMode) {
				break;
			}

			// If we have tried and failed 10 times. call it quits,
			if (_readDelay == MAX_READ_DELAY) {
				log.debug("initializeController: failed");
				return 0;
			}

			// Otherwise increment the read delay and go for another loop
			_readDelay = _readDelay + 1;
			log.debug("initializeController: _readDelay={}", _readDelay);
		}

		log.debug("initializeController: success");
		return 1;
	}

	/**
	 * Read the PS2 Controller. Save the returned data to PS2data.
	 * 
	 * @return The received bytes.
	 */
	public short[] readPS2() {
		log.debug("readPS2: _last_read={}", _last_read);
		long timeSince = _wiringPi.millis() - _last_read;
		log.debug("readPS2: timeSince={}", timeSince);

		if (timeSince > 1500) // waited too long
		{
			log.debug("waited too long");
			_last_read = _wiringPi.millis(); // break endless recursion via reInitialize()
			reInitializeController(_controllerMode);
		}

		if (timeSince < _readDelay) // waited too short
		{
			_wiringPi.delay(_readDelay - timeSince);
		}

		// Ensure that the command bit is high before lowering attention.
		_wiringPi.digitalWrite(_commandPin, 1);
		// Ensure that the clock is high.
		_wiringPi.digitalWrite(_clkPin, 1);

		// Wait for a while between transmitting bytes so that pins can stabilize.
		_wiringPi.delayMicroseconds(BYTE_DELAY);

		// The TX and RX buffer used to read the controller.
		short[] TxRx1 = new short[] { 0x01, 0x42, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		short[] TxRx2 = new short[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

		// Grab the first 9 bits
		System.arraycopy(spiController.transmitBytes(TxRx1), 0, PS2data, 0, 9);

		// If controller is in full data return mode, get the rest of data
		if (PS2data[1] == 0x79) {
			System.arraycopy(spiController.transmitBytes(TxRx2), 0, PS2data, 9, 12);
		}

		_last_read = _wiringPi.millis();

		// Detect which buttons have been changed
		_btnChangedState[0] = PS2data[3] ^ _btnLastState[0];
		_btnChangedState[1] = PS2data[4] ^ _btnLastState[1];
		// Save the current button states as the last state for next read)
		_btnLastState[0] = PS2data[3];
		_btnLastState[1] = PS2data[4];

		return Arrays.copyOf(PS2data, PS2data.length);
	}

	/**
	 * Re-Initializes the I/O pins and sets up the controller for the desired mode.
	 * <br>
	 * TODO: <br>
	 * Currently this function is only coded for either analog mode without all
	 * pressures returned or analog mode with all pressures. Need to implement
	 * digital.
	 * 
	 * @param The
	 *            desired controller mode.
	 * @return 1 - Success! -1 - Invalid mode! -2 - Failed to get controller into
	 *         desired mode in less than MAX_INIT_ATTEMPT attempts
	 */
	private int reInitializeController(short _controllerMode) {

		log.debug("reInitializeController: {}", String.format("%#02x", (byte) _controllerMode));
		
		this._controllerMode = _controllerMode;
		if (_controllerMode != ANALOGMODE && _controllerMode != ALLPRESSUREMODE) {
			log.debug("reInitializeController: illegal mode");
			return -1;
		}

		for (int initAttempts = 1; initAttempts < MAX_INIT_ATTEMPT; initAttempts++) {
			log.debug("attempt #{}", initAttempts);
			spiController.transmitBytes(enterConfigMode);
			spiController.transmitBytes(setModeAnalogLockMode);
			if (_controllerMode == ALLPRESSUREMODE) {
				spiController.transmitBytes(setAllPressureMode);
				spiController.transmitBytes(exitConfigAllPressureMode);
			}
			spiController.transmitBytes(exitConfigMode2);
			readPS2();
			if (PS2data[1] == _controllerMode) {
				log.debug("reInitializeController: success");
				return 1;
			}
			_wiringPi.delay(_readDelay);
		}

		log.debug("reInitializeController: timeout");
		return -2;
	}

	/**
	 * Request the changed states. To determine what buttons have changed state
	 * since last read.
	 * 
	 * @return Array with two char elements holding the changed states.
	 */
	public int[] getChangedStates() {
		return _btnChangedState;
	}

}
