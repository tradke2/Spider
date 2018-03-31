package tomrad.spider;

import java.util.ArrayDeque;
import java.util.Deque;

public class WiringMock implements GpioWiring {

	private Deque<Integer> bitQueue = new ArrayDeque<>();
	private int dataPin;
	private int commandPin;
	private long _setupTime;
	
	public WiringMock()
	{
		this(0,0);
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
		if (gpioPin == commandPin) bitQueue.offer(i);
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

}
