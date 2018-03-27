package tomrad.spider;

public interface GpioWiring {

	String OUTPUT = "Output";
	String INPUT = "Input";
	String PUD_UP = "PulldownUp";
	String PUD_DOWN = "PUD_DOWN";
	String PUD_OFF = "PUD_OFF";

	int wiringPiSetupGpio();

	int physPinToGpio(int physPin);

	void pinMode(int gpioPin, String mode);

	void pullUpDnControl(int gpioPin, String pullDownMode);

	void digitalWrite(int gpioPin, int i);

	void delay(int i);

	void delayMicroseconds(int microseconds);

	int digitalRead(int gpioPin);

	void shutdown();

}