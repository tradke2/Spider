package tomrad.spider;

/**
 * Interface for encapsualtion of Pi4J. This enables to instantiate the
 * application on other platforms than Raspberry Pi.
 */
public interface GpioWiring {

	/**
	 * Pin mode: input or output.
	 */
	public enum PinMode {
		INPUT, OUTPUT
	}

	/**
	 * Pulldown resistor mode: pullup, pulldown or off.
	 */
	public enum PulldownMode {
		PUD_UP, PUD_DOWN, PUD_OFF
	}

	/**
	 * Initializes the WiringPi system. The Raspberry Pi implementation delegates to
	 * one of the Gpio.wiringPiSetup() methods.  
	 * 
	 * @return the result of the called Gpio.wiringPiSetup() method.
	 */
	int wiringPiSetup();

	int configurePin(int physPin);

	void pinMode(int gpioPin, PinMode mode);

	void pullUpDnControl(int gpioPin, PulldownMode pullDownMode);

	void digitalWrite(int gpioPin, int i);

	void delay(long milliseconds);

	void delayMicroseconds(int microseconds);

	int digitalRead(int gpioPin);

	long millis();

}