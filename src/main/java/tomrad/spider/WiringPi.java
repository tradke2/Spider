package tomrad.spider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public enum PinNumberingScheme {
		WiringPi, Gpio, Physical
	}

	public WiringPi() {
		this(PinNumberingScheme.WiringPi);
	}

	public WiringPi(PinNumberingScheme pinNumberingScheme) {
		log.debug("WiringPi ctor: pinNumberingScheme={}", pinNumberingScheme);
		this.pinNumberingScheme = pinNumberingScheme;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tomrad.spider.GpioWiring#wiringPiSetupGpio()
	 */
	@Override
	public int wiringPiSetupGpio() {
		log.debug("wiringPiSetupGpio beg");
		int result = wiringPiSetup();
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

	private int wiringPiSetup() {
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

}
