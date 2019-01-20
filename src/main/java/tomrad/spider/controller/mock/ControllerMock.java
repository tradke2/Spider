package tomrad.spider.controller.mock;

import tomrad.spider.TravelLength;
import tomrad.spider.controller.Controller;

public class ControllerMock implements Controller {

	private boolean hexOn;
	private boolean prevHexOn;

	@Override
	public boolean InitController() {
		return true;
	}

	@Override
	public TravelLength ControlInput(TravelLength input) {
		return new TravelLength(0, -64, 0);
	}

	@Override
	public boolean isHexOn() {
		return hexOn;
	}

	@Override
	public boolean isPrevHexOn() {
		return prevHexOn;
	}

	@Override
	public int getInputTimeDelay() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void rememberHexOn() {
		prevHexOn = hexOn;
	}

}
