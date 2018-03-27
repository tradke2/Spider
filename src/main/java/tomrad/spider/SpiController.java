package tomrad.spider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpiController {

	final static Logger log = LoggerFactory.getLogger(SpiController.class);

	private final GpioWiring wiringPi;

	private int CLK_DELAY = 4; // 4

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
	
		_commandPin = wiringPi.physPinToGpio(pS2CMD);
		_dataPin = wiringPi.physPinToGpio(pS2DAT);
		_clkPin = wiringPi.physPinToGpio(pS2CLK);
		_attnPin = wiringPi.physPinToGpio(pS2SEL);

		wiringPi.pinMode(_commandPin, GpioWiring.OUTPUT); // Set command pin to output
		wiringPi.pinMode(_dataPin, GpioWiring.INPUT); // Set data pin to input
		wiringPi.pullUpDnControl(_dataPin, GpioWiring.PUD_UP); // activate PI internal pulldown resistor
		wiringPi.pinMode(_attnPin, GpioWiring.OUTPUT); // Set attention pin to output
		wiringPi.pinMode(_clkPin, GpioWiring.OUTPUT); // Set clock pin to output

	}

	/**
	 * Bit bang a single bit.
	 * @param outBit
	 *            The bit to transmit.
	 * 
	 * @return The bit received.
	 */
	private boolean transmitBit(boolean outBit) {
		// print("write outBit=%d, _commandPin=1" % outBit)
		wiringPi.digitalWrite(_commandPin, outBit ? 1 : 0);
	
		// Pull clock low to transfer bit
		wiringPi.digitalWrite(_clkPin, 0);
	
		// Wait for the clock delay before reading the received bit.
		wiringPi.delayMicroseconds(CLK_DELAY);
	
		// Read the data pin.
		boolean inBit = wiringPi.digitalRead(_dataPin) == 1;
	
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
	byte[] transmitBytes(byte[] outbytes) {
		int _readDelay = 4;
	
		if (log.isDebugEnabled())
		{
			log.debug("transmitBytes: outBytes={}", byteArrayToString(outbytes));
		}
		
		// int[] outBits = reduce(lambda accu, b: accu + b, map(lambda byte: map(lambda
		// s: (byte >> s) & 1, range(8)), outbytes));
		// int[] outBits = Stream.of(outbytes).reduce((accu, b) -> accu + b,
		// map(b -> map(s -> (b >> s) & 1, range(8)), outbytes));
		// print(outBits)
	
		boolean[] outBits = bitsOf(outbytes);
		boolean[] inBits = new boolean[outBits.length];
	
		// Ready to begin transmitting, pull attention low.
		wiringPi.digitalWrite(_attnPin, 0);
	
		// inBits = map(lambda bit: transmitBit(bit), outBits);
		// int[] inBits = Stream.of(outBits).map(bit -> transmitBit(bit)).toArray();
		// print(inBits)
		for (int i = 0; i < outBits.length; i++) {
			inBits[i] = transmitBit(outBits[i]);
		}
	
		// Packet finished, release attention line.
		wiringPi.digitalWrite(_attnPin, 1);
	
		// chunks = [inBits[x:x + 8] for x in range(0, len(inBits), 8)];
	
		// chunks = [inBits[x:x + 8] for x in range(0, len(inBits), 8)];
		// map(lambda chunk: chunk.reverse(), chunks);
		// result = map(lambda chunk: reduce(lambda accu, bit: (accu << 1) | bit,
		// chunk), chunks);
	
		byte[] result = bytesOf(inBits); 
		
		// Wait some time before beginning another packet.
		wiringPi.delay(_readDelay);
	
		if (log.isDebugEnabled())
		{
			log.debug("transmitBytes: result={}", byteArrayToString(result));
		}
		return result;
	}

	public void setClkPin() {
		wiringPi.digitalWrite(_clkPin, 1); // high PS2CLK
	}

	private byte[] bytesOf(boolean[] inBits) {
		byte[] result = new byte[(inBits.length + 7) / 8];
		int pos = 0;
		byte b = 0x01;
		for (int i = 0; i < inBits.length; i++)
		{	
			boolean bit = inBits[i];
			if (bit)
			{
				result[pos] |= b;
			}
			b <<= 1;
			if (b == 0)
			{
				b = 0x01;
				pos += 1;
			}
		}
		return result;
	}

	private boolean[] bitsOf(byte[] bytes) {
		boolean[] result = new boolean[8 * bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			boolean[] bits = bitsOf(bytes[i]);
			System.arraycopy(bits, 0, result, i * 8, 8);
		}
		return result;
	}

	private boolean[] bitsOf(byte b) {
		int n = 8;
		final boolean[] set = new boolean[n];
		while (--n >= 0) {
			set[n] = (b & 0x80) != 0;
			b <<= 1;
		}
		return set;
	}

	static String byteArrayToString(byte[] ba) {		
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < ba.length; i++)
		{
			if (i > 0) sb.append(", ");
			sb.append(String.format("0x%x", ba[i]));
		}
		return sb.append("]").toString();
	}

}
