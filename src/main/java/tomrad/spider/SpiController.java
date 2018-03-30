package tomrad.spider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tomrad.spider.GpioWiring.PinMode;
import tomrad.spider.GpioWiring.PulldownMode;

public class SpiController {

	final static Logger log = LoggerFactory.getLogger(SpiController.class);

	private final GpioWiring wiringPi;

	private int CLK_DELAY = 4; // 4
	private int BYTE_DELAY = 20;

	// --------------------------------------------------------------------
	// [PS2 GPIO Pins The Controller Is Connected To As Used By WirinPi]
	int _commandPin = 0;
	int _dataPin = 0;
	int _clkPin = 0;
	int _attnPin = 0;

	public SpiController(GpioWiring wiringPi) {
		this.wiringPi = wiringPi;
	}

	public void setupPins(int pS2CMD, int pS2DAT, int pS2CLK, int pS2SEL) {
	
		wiringPi.wiringPiSetupGpio();
		
		_commandPin = wiringPi.configurePin(pS2CMD);
		_dataPin = wiringPi.configurePin(pS2DAT);
		_clkPin = wiringPi.configurePin(pS2CLK);
		_attnPin = wiringPi.configurePin(pS2SEL);

		wiringPi.pinMode(_commandPin, PinMode.OUTPUT); // Set command pin to output
		wiringPi.pinMode(_dataPin, PinMode.INPUT); // Set data pin to input
		wiringPi.pullUpDnControl(_dataPin, PulldownMode.PUD_UP); // activate PI internal pulldown resistor
		wiringPi.pinMode(_attnPin, PinMode.OUTPUT); // Set attention pin to output
		wiringPi.pinMode(_clkPin, PinMode.OUTPUT); // Set clock pin to output
		
		wiringPi.digitalWrite(_commandPin, 1);
		wiringPi.digitalWrite(_clkPin, 1);
		wiringPi.digitalWrite(_attnPin, 1);
		
		wiringPi.delayMicroseconds(1000);
	}

	private int transmitBit(int outBit) {

		// print("write outBit=%d, _commandPin=1" % outBit)
		wiringPi.digitalWrite(_commandPin, outBit);
	
		// Pull clock low to transfer bit
		wiringPi.digitalWrite(_clkPin, 0);
	
		// Wait for the clock delay before reading the received bit.
		wiringPi.delayMicroseconds(CLK_DELAY);
	
		// Read the data pin.
		int inBit = wiringPi.digitalRead(_dataPin);
	
		// Done transferring bit. Put clock back high
		wiringPi.digitalWrite(_clkPin, 1);
		wiringPi.delayMicroseconds(CLK_DELAY);
		
		return inBit;
	}


	/**
	 * Bit bang out an entire array of bytes.
	 * @param outbytes
	 *            Bytes to transmit to PS2 controller
	 * 
	 * @return the bytes that were simultaneously received from PS2 controller
	 */
	short[] transmitBytes(short[] outbytes) {
		
		if (log.isDebugEnabled())
		{
			log.debug("transmitBytes: outBytes={}", byteArrayToString(outbytes));
		}

		short[] inbytes = new short[outbytes.length];
		
		// Ready to begin transmitting, pull attention low.
		wiringPi.digitalWrite(_attnPin, 0);

		for (int i = 0; i < outbytes.length; i++)
		{
			inbytes[i] = transmitByte_neu(outbytes[i]);
			wiringPi.delayMicroseconds(BYTE_DELAY);
		}
	
		// Packet finished, release attention line.
		wiringPi.digitalWrite(_attnPin, 1);
	
		if (log.isDebugEnabled())
		{
			log.debug("transmitBytes: result={}", byteArrayToString(inbytes));
		}
		
		return inbytes;
	}

	private short transmitByte_neu(short outByte) {
		short inByte = 0;
		for (int i = 0; i < 8; i++) {
			int outBit = (outByte & 0x01);
			int inBit = transmitBit(outBit);
			inByte |= (inBit << i);
			outByte >>>= 1;
		}
		return inByte;
	}

	public void setClkPin() {
		wiringPi.digitalWrite(_clkPin, 1); // high PS2CLK
	}

	static String byteArrayToString(short[] ba) {		
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < ba.length; i++)
		{
			if (i > 0) sb.append(", ");
			sb.append(String.format("%02X", (byte)ba[i]));
		}
		return sb.append("]").toString();
	}

}
