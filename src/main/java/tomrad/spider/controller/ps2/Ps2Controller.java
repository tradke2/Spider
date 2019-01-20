package tomrad.spider.controller.ps2;

import static tomrad.spider.controller.ps2.Ps2ControllerConstants.ANALOGMODE;
import static tomrad.spider.controller.ps2.Ps2ControllerConstants.BYTE_DELAY;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tomrad.spider.GpioWiring;

public class Ps2Controller {

	private final static Logger log = LoggerFactory.getLogger(Ps2Controller.class);
	private final static int _readDelay = 1;

	// private members:
	private short _controllerMode = -1;
	int _commandPin = -1;
	int _attnPin = -1;
	int _clkPin = -1;
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
	 * Initializes the I/O pins and sets up the controller for the analog mode.
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
	public int initializeAnalogMode() {
		
		log.debug("initializeAnalog");
		
		short chk_ana = 0;
		int cnt = 0;
		
		while(chk_ana != 0x73)
		{
			// put controller in config mode
			_wiringPi.digitalWrite(_commandPin, 1);
			_wiringPi.digitalWrite(_clkPin, 1);
			_wiringPi.digitalWrite(_attnPin, 0);

			spiController.transmitBytes(new short[] {0x01, 0x43, 0x00, 0x01, 0x00});
	           
			_wiringPi.digitalWrite(_commandPin, 1);
			_wiringPi.delay(1);
			_wiringPi.digitalWrite(_attnPin, 1);
	           
			_wiringPi.delay(10);
	          
			// put controller in analouge mode
			_wiringPi.digitalWrite(_commandPin, 1);
			_wiringPi.digitalWrite(_clkPin, 1);
			_wiringPi.digitalWrite(_attnPin, 0);
	         
			spiController.transmitBytes(new short[] {0x01, 0x44, 0x00, 0x01, 0x03, 0x00, 0x00, 0x00, 0x00});
	          
			_wiringPi.digitalWrite(_commandPin, 1);
			_wiringPi.delay(1);
			_wiringPi.digitalWrite(_attnPin, 1);
	          
			_wiringPi.delay(10);
	         
			// exit config mode
			_wiringPi.digitalWrite(_commandPin, 1);
			_wiringPi.digitalWrite(_clkPin, 1);
			_wiringPi.digitalWrite(_attnPin, 0);
	          
			spiController.transmitBytes(new short[] {0x01, 0x43, 0x00, 0x00, 0x5A, 0x5A, 0x5A, 0x5A, 0x5A});
	            
			_wiringPi.digitalWrite(_commandPin, 1);
			_wiringPi.delay(1);
			_wiringPi.digitalWrite(_attnPin, 1);
	          
			_wiringPi.delay(10);
	         
  		    // poll controller and check in analouge mode.
			_wiringPi.digitalWrite(_commandPin, 1);
			_wiringPi.digitalWrite(_clkPin, 1);
			_wiringPi.digitalWrite(_attnPin, 0);
	          	          
			chk_ana = spiController.transmitBytes(new short[] {0x01, 0x42, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00})[1];

			_wiringPi.digitalWrite(_commandPin, 1);
			_wiringPi.delay(1);
			_wiringPi.digitalWrite(_attnPin, 1);
	          
			_wiringPi.delay(10);
	         
		  // keep increasing counter to be dispalyed untill PSx controller confirms it's in analouge mode.
		 log.debug("cnt={}",cnt++);                     
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
		log.trace("readPS2: _last_read={}", _last_read);
		long timeSince = _wiringPi.millis() - _last_read;
		log.trace("readPS2: timeSince={}", timeSince);

		if (timeSince > 1500) // waited too long
		{
			log.debug("waited too long");
			_last_read = _wiringPi.millis(); // break endless recursion via reInitialize()
			initializeAnalogMode();
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
	 * Request the changed states. To determine what buttons have changed state
	 * since last read.
	 * 
	 * @return Array with two char elements holding the changed states.
	 */
	public int[] getChangedStates() {
		return _btnChangedState;
	}

}
