package tomrad.spider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.wiringpi.Gpio;

/**
 * Implementierung von {@link GpioWiring} auf Basis von Pi4J.
 * <p>
 * Wegen der Abhängigleit zu Pi4J kann diese Klasse nur auf dem RasPi geladen
 * werden.
 * </p>
 */
public class WiringPi implements GpioWiring {

	private static Logger log = LoggerFactory.getLogger(WiringPi.class);

	private final PinNumberingScheme pinNumberingScheme;

	private final Serial serial;

	public enum PinNumberingScheme {
		WiringPi, Gpio, Physical
	}

	public WiringPi() {
		this(PinNumberingScheme.WiringPi);
	}

	public WiringPi(PinNumberingScheme pinNumberingScheme) {
		log.debug("WiringPi ctor: pinNumberingScheme={}", pinNumberingScheme);
		this.pinNumberingScheme = pinNumberingScheme;
		serial = SerialFactory.createInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tomrad.spider.GpioWiring#wiringPiSetupGpio()
	 */
	@Override
	public int wiringPiSetup() {
		log.debug("wiringPiSetupGpio beg");
		int result = internalWiringPiSetup();
		log.debug("wiringPiSetupGpio end: result={}", result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tomrad.spider.GpioWiring#physPinToGpio(int)
	 */
	@Override
	public int configurePin(int configuredPin) {
		switch (pinNumberingScheme) {
		case Gpio:
			return Gpio.physPinToGpio(configuredPin);
		case Physical:
			return Gpio.physPinToGpio(configuredPin);
		case WiringPi:
		default:
			return configuredPin;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tomrad.spider.GpioWiring#pinMode(int, java.lang.String)
	 */
	@Override
	public void pinMode(int pin, PinMode mode) {
		log.debug("pinMode: setting pin {} to {}", pin, mode);
		int pinMode = getPinMode(mode);
		Gpio.pinMode(pin, pinMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tomrad.spider.GpioWiring#pullUpDnControl(int, java.lang.String)
	 */
	@Override
	public void pullUpDnControl(int pin, PulldownMode pullDownMode) {
		log.debug("pullUpDnControl: setting pin {} to {}", pin, pullDownMode);
		int pdMode = getPinPullResistance(pullDownMode);
		Gpio.pullUpDnControl(pin, pdMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tomrad.spider.GpioWiring#digitalWrite(int, int)
	 */
	@Override
	public void digitalWrite(int pin, int bit) {
		// log.debug("digitalWrite: pin {} = {}", gpioPin, bit);
		Gpio.digitalWrite(pin, bit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tomrad.spider.GpioWiring#delay(int)
	 */
	@Override
	public void delay(long milliseconds) {
		Gpio.delay(milliseconds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tomrad.spider.GpioWiring#delayMicroseconds(int)
	 */
	@Override
	public void delayMicroseconds(int microseconds) {
		Gpio.delayMicroseconds(microseconds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tomrad.spider.GpioWiring#digitalRead(int)
	 */
	@Override
	public int digitalRead(int pin) {
		int result = Gpio.digitalRead(pin);
		// log.debug("digitalRead: pin {} = {}", gpioPin, result);
		return result;
	}

	private int getPinMode(PinMode mode) {
		switch (mode) {
		case INPUT:
			return Gpio.INPUT;
		case OUTPUT:
			return Gpio.OUTPUT;
		default:
			throw new IllegalArgumentException(mode.toString());
		}
	}

	private int getPinPullResistance(PulldownMode pullDownMode) {
		switch (pullDownMode) {
		case PUD_UP:
			return Gpio.PUD_UP;
		case PUD_DOWN:
			return Gpio.PUD_DOWN;
		case PUD_OFF:
			return Gpio.PUD_OFF;
		default:
			throw new IllegalArgumentException(pullDownMode.toString());
		}
	}

	private int internalWiringPiSetup() {
		switch (pinNumberingScheme) {
		case Gpio:
			return Gpio.wiringPiSetupGpio();
		case Physical:
			return Gpio.wiringPiSetupPhys();
		case WiringPi:
		default:
			return Gpio.wiringPiSetup();
		}
	}

	@Override
	public long millis() {
		return Gpio.millis();
	}

	private void checkState() {
		if (serial == null) {
			throw new IllegalStateException(
					"wiringPiSetup() must be called before any operation on serial device can be done.");
		}
	}

	@Override
	public void serialOpen(String device, int baud) throws IOException {
		checkState();
		serial.open(device, baud);
		log.debug("Serial device {} opened with {} baud.", device, baud);
	}

	@Override
	public void serialFlush() throws IOException {
		checkState();
		serial.flush();
		log.debug("Serial device flushed.");
	}

	@Override
	public void serialWrite(Charset charset, String data) throws IOException {
		checkState();
		serial.write(charset, data);
		log.debug("Serial device written.");
	}

	@Override
	public void serialClose() throws IOException {
		checkState();
		serial.close();
		log.debug("Serial device closed.");
	}

	private String serialReadUntil(char endOfLine) throws IOException {
		checkState();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		InputStream is = serial.getInputStream();
		int inByte = is.read();
		while (inByte > -1 && inByte != endOfLine) {
			bos.write(inByte);
			inByte = is.read();
		}
		bos.flush();
		return bos.toString("US-ASCII");
	}

	@Override
	public void readInto(int[] inputData) throws IOException {
		checkState();
		byte[] inBytes = serial.read(inputData.length);
		for (int i = 0; i < inputData.length; i++) {
			inputData[i] = inBytes[i] & 0xff;
		}
	}

	@Override
	public String serialReadln() throws IOException {
		String result = serialReadUntil(CR);
		log.debug("Serial device read: \"{}\"", result);
		return result;
	}

}
