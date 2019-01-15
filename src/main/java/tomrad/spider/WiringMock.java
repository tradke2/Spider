package tomrad.spider;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Deque;

public class WiringMock implements GpioWiring {

	private Deque<Integer> bitQueue = new ArrayDeque<>();
	private int dataPin;
	private int commandPin;
	private long _setupTime;

	public WiringMock() {
		this(0, 0);
	}

	public WiringMock(int dataPin, int commandPin) {
		this.dataPin = dataPin;
		this.commandPin = commandPin;
	}

	@Override
	public int wiringPiSetup() {
		return internalSetup();
	}

	private int internalSetup() {
		_setupTime = System.currentTimeMillis();
		return 0;
	}

	@Override
	public void pinMode(int gpioPin, PinMode mode) {
		// TODO Auto-generated method stub
	}

	@Override
	public void digitalWrite(int gpioPin, int i) {
		if (gpioPin == commandPin)
			bitQueue.offer(i);
	}

	@Override
	public void delay(long milliseconds) {
		// TODO Auto-generated method stub
	}

	@Override
	public void delayMicroseconds(int microseconds) {
		// TODO Auto-generated method stub
	}

	@Override
	public int digitalRead(int gpioPin) {
		return gpioPin == dataPin && !bitQueue.isEmpty() ? bitQueue.poll() : 0;
	}

	@Override
	public int configurePin(int physPin) {
		return physPin;
	}

	@Override
	public void pullUpDnControl(int gpioPin, PulldownMode pullDownMode) {
		// TODO Auto-generated method stub
	}

	@Override
	public long millis() {
		return System.currentTimeMillis() - _setupTime;
	}

	@Override
	public void serialOpen(String device, int baud) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void serialFlush() throws IllegalStateException, IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void serialWrite(Charset charset, String data) throws IllegalStateException, IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void serialClose() throws IllegalStateException, IOException {
		// TODO Auto-generated method stub

	}

	private String serialReadUntil(char endOfLine) throws IOException {
		return "SSC32-V2.0USB" + endOfLine;
	}

	@Override
	public void readInto(int[] inputData) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public String serialReadln() throws IOException {
		return serialReadUntil(CR);
	}

}
