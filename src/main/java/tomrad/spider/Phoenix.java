package tomrad.spider;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tomrad.spider.Balance.BalanceValue;
import tomrad.spider.IkRoutines.CalcIkResult;
import tomrad.spider.Servo.CheckAnglesResult;
import tomrad.spider.controller.Controller;
import tomrad.spider.gait.Gait;

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

	private CountDownLatch mainLoopEnded;
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
	public static int GPStart = 0;

	/** Number of the sequence */
	public static int GPSeq = 0;

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
	public static int SpeedControl = 0; // Adjustible Delay

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
			boolean success = false;
			int attempt = 1;
			while (!success) {
				success = controller.InitController();
				log.info("InitController {}: success={}", attempt++, success);
			}

			if (!success) {
				quit();
			}

		} catch (Exception e) {
			log.error("Unexpected error", e);
		}
	}

	private void quit() {
		System.exit(1);
	}

	// ====================================================================
	// [MAIN]
	void MainLoop() throws IOException {

		TravelLength travelLength = new TravelLength(0, 0, 0);
		mainLoopEnded = new CountDownLatch(1);
		
		// main:
		log.info("Entering main loop ...");
		while (getRemainingLoops() != 0) {

			try {

				// servo.pause(500); // pause 1000
	
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
				CheckAnglesResult checkedAngles = servo.CheckAngles(ikResult);
	
				// Drive Servos
				Eyes = servo.ServoDriverMain(Eyes, controller.isHexOn(), controller.isPrevHexOn(),
						controller.getInputTimeDelay(), SpeedControl, travelLength, checkedAngles, gait.getNominalSpeed());
	
				// Store previous HexOn State
				controller.rememberHexOn();
	
				decrementRemainingLoops();

				// goto main
			
			} catch (IKSolutionError e) {
				log.error(e.getMessage());
			}

		}

		// dead:
		// goto dead
		log.debug("Ended main loop");
		servo.FreeServos();		
		
		mainLoopEnded.countDown();
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
		try {
			MainLoop();
		} catch (IOException e) {
			log.error("Unexpected error", e);
		}
	}

	public void stop() {
		setRemainingLoops(1);		
		try {
			mainLoopEnded.await(10, TimeUnit.SECONDS);
			log.debug("Phoenix stopped.");
		} catch (InterruptedException e) {
			log.error("Failed to stop gracefully.", e);
		}
	}

	/** Debugging aid: limits the count of the main loop */
	private volatile int remainingLoops = -1;

	private synchronized void setRemainingLoops(int value) {
		remainingLoops = value;
	}

	private synchronized int getRemainingLoops() {
		return remainingLoops;
	}

	private synchronized void decrementRemainingLoops() {
		if (remainingLoops > 0) {
			remainingLoops -= 1;
		}
	}
}
