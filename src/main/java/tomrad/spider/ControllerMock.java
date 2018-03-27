package tomrad.spider;

import tomrad.spider.Gait.TravelLength;

public class ControllerMock implements Controller {

	private boolean hexOn;

	@Override
	public boolean InitController() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TravelLength ControlInput(TravelLength input) {
		return new TravelLength(0, 0, 0);
	}

	@Override
	public void setHexOn(boolean hexOn) {
		this.hexOn = hexOn;
	}

	@Override
	public boolean isHexOn() {
		return hexOn;
	}

	@Override
	public void setPrevHexOn(boolean prevHexOn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPrevHexOn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInputTimeDelay(int inputTimeDelay) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getInputTimeDelay() {
		// TODO Auto-generated method stub
		return 0;
	}

}
