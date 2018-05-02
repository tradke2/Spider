package tomrad.spider;

interface Controller {

	// --------------------------------------------------------------------
	// [InitController] Initialize the PS2 controller
	boolean InitController();

	// --------------------------------------------------------------------
	// [ControlInput] reads the input data from the PS2 controller && processes the
	// data to the parameters.
	TravelLength ControlInput(TravelLength input);

	boolean isHexOn();

	void setPrevHexOn(boolean prev_HexOn);

	boolean isPrevHexOn();

	int getInputTimeDelay();

}