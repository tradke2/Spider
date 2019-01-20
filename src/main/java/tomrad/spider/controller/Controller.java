package tomrad.spider.controller;

import tomrad.spider.TravelLength;

public interface Controller {

	// --------------------------------------------------------------------
	// [InitController] Initialize the PS2 controller
	boolean InitController();

	// --------------------------------------------------------------------
	// [ControlInput] reads the input data from the PS2 controller && processes the
	// data to the parameters.
	TravelLength ControlInput(TravelLength input);

	boolean isHexOn();

	boolean isPrevHexOn();

	int getInputTimeDelay();

	void rememberHexOn();

}