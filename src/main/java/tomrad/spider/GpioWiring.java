package tomrad.spider;

public interface GpioWiring {

	public enum PinMode {
		INPUT, OUTPUT
	}

	public enum PulldownMode {
		PUD_UP, PUD_DOWN, PUD_OFF
	}
	
	String OUTPUT = "Output";
	String INPUT = "Input";
	String PUD_UP = "PulldownUp";
	String PUD_DOWN = "PUD_DOWN";
	String PUD_OFF = "PUD_OFF";

	int wiringPiSetupGpio();

	int configurePin(int physPin);

	void pinMode(int gpioPin, PinMode mode);

	void pullUpDnControl(int gpioPin, PulldownMode pullDownMode);

	void digitalWrite(int gpioPin, int i);

	void delay(long milliseconds);

	void delayMicroseconds(int microseconds);

	int digitalRead(int gpioPin);

	long millis();

}