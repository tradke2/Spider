package tomrad.spider;

public class ControllerMock implements Controller {

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
	public boolean isPrevHexOn() {
		return false;
	}

	@Override
	public int getInputTimeDelay() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void togglePrevHexOn() {
		prevHexOn = !prevHexOn;
	}

}
