package tomrad.spider;
//!/usr/bin/env python

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tomrad.spider.Balance.BalanceValue;
import tomrad.spider.Gait.TravelLength;
import tomrad.spider.IkRoutines.CalcIkResult;
import tomrad.spider.Servo.CheckAnglesResult;

// -*- coding: utf-8 -*-

// Project Lynxmotion Phoenix
// Description: Phoenix software
// Software version: V2.0
// Date: 29-10-2009
// Programmer: Jeroen Janssen (aka Xan)
// 
// Hardware setup: ABB2 with ATOM 28 Pro, SSC32 V2
// 
// NEW IN V2.0
// 	- Moved to fixed point calculations
// 	- Inverted BodyRotX and BodyRotZ direction
// 	- Added deadzone for switching gaits
// 	- Added GP Player
// 	- SSC version check to enable/disable GP player
// 	- Controls changed, Check contol file for more information
// 	- Added separate files for control and configuration functions
// 	- Solved bug at turn-off sequence
// 	- Solved bug about legs beeing lift at small travelvalues in 4 steps tripod gait
// 	- Solved bug about body translate results in rotate when balance is on (Kåre)
// 	- Sequence for wave gait changed (Kåre)
// 	- Improved ATan2 function for IK (Kåre)
// 	- Added option to turn on/off eyes (leds)
// 	- Moving legs to init position improved
// 	- Using Indexed values for legs
// 	- Added single leg control
// 
// KNOWN BUGS:
// 	- None at the moment // )
// 
// Project file order:
// 	1. config_ch3r_v20.bas
// 	2. phoenix_V2x.bas
// 	3. phoenix_Control_ps2.bas
// ====================================================================

@Component
public class Phoenix {

	// --------------------------------------------------------------------
	// [CONSTANTS]

	int BUTTON_DOWN = 0;
	int BUTTON_UP = 1;

	// --------------------------------------------------------------------
	// [INPUTS]
	// butA = None
	// butB = None
	// butC = None

	// prev_butA = None
	// prev_butB = None
	// prev_butC = None
	// --------------------------------------------------------------------
	// [GP PLAYER]
	/** Start the GP Player */
	static int GPStart = 0;

	/** Number of the sequence */
	static int GPSeq = 0;

	/** Received data to check the SSC Version */
	char[] GPVerData = new char[] { 0, 0, 0 };

	/** Enables the GP player when the SSC version ends with "GP<cr>" */
	boolean GPEnable = false;

	// --------------------------------------------------------------------
	// [OUTPUTS]
	int LedA = 0; // Red
	int LedB = 0; // Green
	int LedC = 0; // Orange
	boolean Eyes = false; // Eyes output
	// --------------------------------------------------------------------
	// [VARIABLES]
	static int SpeedControl = 0; // Adjustible Delay

	@Autowired
	private Logger log;

	@Autowired
	private Servo servo;

	@Autowired
	private SingleLeg singleLeg;

	@Autowired
	private IkRoutines ikRoutines;

	@Autowired
	private Gait gait;

	@Autowired
	private Balance balance;

	@Autowired
	private Controller controller;

	/** Debugging aid: limits the count of the main loop */
	private int remainingLoops = -1;

	// ====================================================================
	// [INIT]
	@PostConstruct
	public void Init() {
		// Turning off all the leds
		LedA = 0;
		LedB = 0;
		LedC = 0;
		Eyes = false;

		try {
			GPEnable = servo.InitServos(); // Tars Init Positions
			log.info("InitServos: GPEnable={}", GPEnable);
			singleLeg.InitSingleLeg();
			ikRoutines.InitIK();
			gait.InitGait();

			// Initialize Controller
			boolean success = controller.InitController();
			log.info("InitController: success={}", success);
			if (!success) {
				quit();
			}

			// SSC
			controller.setHexOn(false);

			log.debug("Entering main loop ...");
			MainLoop();
		} catch (Exception e) {
			log.error("Unexpected error", e);
		}
		return;
	}

	private void quit() {
	}

	// ====================================================================
	// [MAIN]
	void MainLoop() throws IOException {
		// TravelLengthX = 0; // Current Travel length X
		// TravelLengthZ = 0; // Current Travel length Z
		// TravelRotationY = 0; // Current Travel Rotation Y
		TravelLength travelLength = new TravelLength(0, 0, 0);

		// main:
		while (remainingLoops != 0) {
			// time.sleep(0.5) // pause 1000

			// Start time
			servo.StartTimer();

			travelLength = controller.ControlInput(travelLength); // Read input

			// ReadButtons() // I/O used by the remote
			WriteOutputs(); // Write Outputs

			// GP Player
			if (GPEnable) {
				servo.GPPlayer(GPStart, GPSeq);
			}

			// Single leg control
			singleLeg.SingleLegControl();

			// Gait
			travelLength = gait.GaitSeq(travelLength);

			// Balance calculations
			BalanceValue balanceValue = balance.CalcBalance();

			// calculate inverse kinematic
			CalcIkResult ikResult = ikRoutines.CalcIK(balanceValue);

			// Check mechanical limits
			CheckAnglesResult checkedAngles = servo.CheckAngles(ikResult.coxaAngle, ikResult.femurAngle,
					ikResult.tibiaAngle);

			// Drive Servos
			Eyes = servo.ServoDriverMain(Eyes, controller.isHexOn(), controller.isPrevHexOn(),
					controller.getInputTimeDelay(), SpeedControl, travelLength, checkedAngles.coxaAngle,
					checkedAngles.femurAngle, checkedAngles.tibiaAngle);

			// Store previous HexOn State
			if (controller.isHexOn()) {
				controller.setPrevHexOn(true);
			} else {
				controller.setPrevHexOn(false);
			}

			if (remainingLoops > 0) {
				remainingLoops -= 1;
			}
			// goto main
		}

		// dead:
		// goto dead
	}

	// ====================================================================
	// [ReadButtons] Reading input buttons from the ABB
	private void ReadButtons() {
		// input P4
		// input P5
		// input P6

		// prev_butA = butA
		// prev_butB = butB
		// prev_butC = butC

		// butA = IN4
		// butB = IN5
		// butC = IN6
		return;
	}

	// --------------------------------------------------------------------
	// [WriteOutputs] Updates the state of the leds
	private void WriteOutputs() {
		// IF ledA = 1 THEN
		// low p4
		// ENDIF
		// IF ledB = 1 THEN
		// low p5
		// ENDIF
		// IF ledC = 1 THEN
		// low p6
		// ENDIF
		if (!Eyes) {
			// wiringpi.digitalWrite(cEyesPin, 0)
		} else {
			// wiringpi.digitalWrite(cEyesPin, 1)
		}
		return;
	}

	// --------------------------------------------------------------------
	// [Entry point]
	public void run() {
		Init();
	}
}
