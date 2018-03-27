package tomrad.spider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;

public class WiringPi implements GpioWiring {

	private static Logger log = LoggerFactory.getLogger(WiringPi.class);
	
//	private GpioController gpio;
	private Gpio gpio;

	public WiringPi()
	{
		log.debug("WiringPi ctor");
//		log.debug("wiringPiSetupGpio vor getInstance()");
//		GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));
//		gpio  = GpioFactory.getInstance();
//		log.debug("wiringPiSetupGpio nach getInstance()");
	}
	
	/* (non-Javadoc)
	 * @see tomrad.spider.GpioWiring#wiringPiSetupGpio()
	 */
	@Override
	public int wiringPiSetupGpio() {
		log.debug("wiringPiSetupGpio beg");
//		int result = Gpio.wiringPiSetupPhys();
//		log.debug("wiringPiSetupGpio nach wiringPiSetupPhys()");
//		if (result == 0)
//		{
//		}
//		log.debug("wiringPiSetupGpio end");
//		return result;
		int result = Gpio.wiringPiSetup();
		log.debug("wiringPiSetupGpio end: result={}", result);
		return result;
	}

	/* (non-Javadoc)
	 * @see tomrad.spider.GpioWiring#physPinToGpio(int)
	 */
	@Override
	public int physPinToGpio(int physPin) {
		return physPin;//Gpio.physPinToGpio(physPin);
	}

	/* (non-Javadoc)
	 * @see tomrad.spider.GpioWiring#pinMode(int, java.lang.String)
	 */
	@Override
	public void pinMode(int gpioPin, String mode) {
		log.debug("pinMode: setting pin {} to {}", gpioPin, mode);
		int pinMode = getPinMode(mode);
		Gpio.pinMode(gpioPin, pinMode);
//		Pin raspiPin = getPin(gpioPin);
//		GpioPin provisionedPin = gpio.getProvisionedPin(raspiPin);
//		if (provisionedPin == null) {
//			provisionedPin = gpio.provisionPin(raspiPin, pinMode);
//		}
//		provisionedPin.setMode(pinMode);
	}

	/* (non-Javadoc)
	 * @see tomrad.spider.GpioWiring#pullUpDnControl(int, java.lang.String)
	 */
	@Override
	public void pullUpDnControl(int gpioPin, String pullDownMode) {
		log.debug("pullUpDnControl: setting pin {} to {}", gpioPin, pullDownMode);
		int pdMode = getPinPullResistance(pullDownMode);
		Gpio.pullUpDnControl(gpioPin, pdMode);
//		Pin raspiPin = getPin(gpioPin);
//		GpioPin provisionedPin = gpio.getProvisionedPin(raspiPin);
//		if (provisionedPin == null)
//		{
//			throw new IllegalArgumentException(Integer.toString(gpioPin));
//		}
//		provisionedPin.setPullResistance(resistance);
	}

	/* (non-Javadoc)
	 * @see tomrad.spider.GpioWiring#digitalWrite(int, int)
	 */
	@Override
	public void digitalWrite(int gpioPin, int bit) {
		log.debug("digitalWrite: pin {} = {}", gpioPin, bit);
		Gpio.digitalWrite(gpioPin, bit);
//		Pin raspiPin = getPin(gpioPin);
//		GpioPin provisionedPin = gpio.getProvisionedPin(raspiPin);
//		if (provisionedPin == null)
//		{
//			throw new IllegalArgumentException(Integer.toString(gpioPin));
//		}
//		if (!provisionedPin.isMode(PinMode.DIGITAL_OUTPUT))
//		{
//			throw new IllegalStateException(provisionedPin.getMode().toString());
//		}
//		if (i == 0)
//		{
//			((GpioPinDigitalOutput)provisionedPin).low();
//		}
//		else
//		{
//			((GpioPinDigitalOutput)provisionedPin).high();
//		}
	}

	/* (non-Javadoc)
	 * @see tomrad.spider.GpioWiring#delay(int)
	 */
	@Override
	public void delay(int milliseconds) {
		Gpio.delay(milliseconds);
	}

	/* (non-Javadoc)
	 * @see tomrad.spider.GpioWiring#delayMicroseconds(int)
	 */
	@Override
	public void delayMicroseconds(int microseconds) {
		Gpio.delayMicroseconds(microseconds);
//		try {
//			Thread.sleep(0, microseconds * 1000);
//		} catch (InterruptedException e) {
////			log.error("delayMicroseconds: Thread.sleep() interrupted.", e);
//		}
	}

	/* (non-Javadoc)
	 * @see tomrad.spider.GpioWiring#digitalRead(int)
	 */
	@Override
	public int digitalRead(int gpioPin) {
		int result = Gpio.digitalRead(gpioPin);
//		Pin raspiPin = getPin(gpioPin);
//		GpioPin provisionedPin = gpio.getProvisionedPin(raspiPin);
//		if (provisionedPin == null)
//		{
//			throw new IllegalArgumentException(Integer.toString(gpioPin));
//		}
//		if (!provisionedPin.isMode(PinMode.DIGITAL_INPUT))
//		{
//			throw new IllegalStateException(provisionedPin.getMode().toString());
//		}
//		int result = 0;
//		if (((GpioPinDigitalInput)provisionedPin).isHigh())
//		{
//			result = 1;
//		}
//		else if (((GpioPinDigitalInput)provisionedPin).isLow())
//		{
//			result = 0;
//		}
		log.debug("digitalRead: pin {} = {}", gpioPin, result);
		return result;
	}

	private Pin getPin(int gpioPin) {
//		log.debug("getPin: physPin={}", gpioPin);
		Pin raspiPin = RaspiPin.getPinByAddress(gpioPin);
		if (raspiPin == null) {
			throw new IllegalArgumentException(Integer.toString(gpioPin));
		}
//		log.debug("getPin: raspiPin={}", raspiPin.getName());
		return raspiPin;
	}

	private int getPinMode(String mode) {
		switch (mode) {
		case INPUT:
			return Gpio.INPUT;
		case OUTPUT:
			return Gpio.OUTPUT;
		default:
			throw new IllegalArgumentException(mode);
		}
	}

	private int getPinPullResistance(String pullDownMode) {
		switch (pullDownMode) {
		case PUD_UP:
			return Gpio.PUD_UP;
		case PUD_DOWN:
			return Gpio.PUD_DOWN;
		case PUD_OFF:
			return Gpio.PUD_OFF;
		default:
			throw new IllegalArgumentException(pullDownMode);
		}
	}

	@Override
	public void shutdown() {
//		if (gpio != null)
//		{
//			log.debug("shutdown: shutting down");
//			gpio.shutdown();
//		}
	}

}
