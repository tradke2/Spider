package tomrad.spider;

import java.util.ArrayDeque;
import java.util.Deque;

public class WiringMock implements GpioWiring {

	private Deque<Integer> bitQueue = new ArrayDeque<>();
	private int dataPin;
	private int commandPin;
	
	public WiringMock()
	{
		this(0,0);
	}
	
	public WiringMock(int dataPin, int commandPin) {
		this.dataPin = dataPin;
		this.commandPin = commandPin;
	}
	
	@Override
	public int wiringPiSetupGpio() {
		return 0;
	}

	@Override
	public int physPinToGpio(int physPin) {
		return physPin;
	}

	@Override
	public void pinMode(int gpioPin, String mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pullUpDnControl(int gpioPin, String pullDownMode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void digitalWrite(int gpioPin, int i) {
		if (gpioPin == commandPin) bitQueue.offer(i);
	}

	@Override
	public void delay(int i) {
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
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

}
