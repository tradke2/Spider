package tomrad.spider;

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
		return true;
	}

	@Override
	public void setPrevHexOn(boolean prevHexOn) {
		this.prevHexOn = prevHexOn;
	}

	@Override
	public boolean isPrevHexOn() {
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
