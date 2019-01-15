package tomrad.spider;

import java.io.IOException;
import java.nio.charset.Charset;

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
	char CR = 13;


	int wiringPiSetup();

	int configurePin(int physPin);

	void pinMode(int gpioPin, PinMode mode);

	void pullUpDnControl(int gpioPin, PulldownMode pullDownMode);

	void digitalWrite(int gpioPin, int i);

	void delay(long milliseconds);

	void delayMicroseconds(int microseconds);

	int digitalRead(int gpioPin);

	long millis();

	void serialOpen(String device, int baud) throws IOException;

	void serialFlush() throws IOException;

	void serialWrite(Charset charset, String data) throws IOException;

	void serialClose() throws IOException;

	void readInto(int[] inputData) throws IOException;

	String serialReadln() throws IOException;

}