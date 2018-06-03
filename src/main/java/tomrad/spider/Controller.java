package tomrad.spider;

import tomrad.spider.Gait.TravelLength;

interface Controller {

	// --------------------------------------------------------------------
	// [InitController] Initialize the PS2 controller
	boolean InitController();

	// --------------------------------------------------------------------
	// [ControlInput] reads the input data from the PS2 controller && processes the
	// data to the parameters.
	TravelLength ControlInput(TravelLength input);

	void setHexOn(boolean hexOn);

	boolean isHexOn();

	void setPrevHexOn(boolean prev_HexOn);

	boolean isPrevHexOn();

	void setInputTimeDelay(int inputTimeDelay);

	int getInputTimeDelay();

}