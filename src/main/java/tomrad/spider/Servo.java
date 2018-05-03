package tomrad.spider;

import static tomrad.spider.Config_Ch3.cDegreesPerMicroSecond;
import static tomrad.spider.Config_Ch3.cLFCoxaMax;
import static tomrad.spider.Config_Ch3.cLFCoxaMin;
import static tomrad.spider.Config_Ch3.cLFCoxaPin;
import static tomrad.spider.Config_Ch3.cLFFemurMax;
import static tomrad.spider.Config_Ch3.cLFFemurMin;
import static tomrad.spider.Config_Ch3.cLFFemurPin;
import static tomrad.spider.Config_Ch3.cLFTibiaMax;
import static tomrad.spider.Config_Ch3.cLFTibiaMin;
import static tomrad.spider.Config_Ch3.cLFTibiaPin;
import static tomrad.spider.Config_Ch3.cLMCoxaMax;
import static tomrad.spider.Config_Ch3.cLMCoxaMin;
import static tomrad.spider.Config_Ch3.cLMCoxaPin;
import static tomrad.spider.Config_Ch3.cLMFemurMax;
import static tomrad.spider.Config_Ch3.cLMFemurMin;
import static tomrad.spider.Config_Ch3.cLMFemurPin;
import static tomrad.spider.Config_Ch3.cLMTibiaMax;
import static tomrad.spider.Config_Ch3.cLMTibiaMin;
import static tomrad.spider.Config_Ch3.cLMTibiaPin;
import static tomrad.spider.Config_Ch3.cLRCoxaMax;
import static tomrad.spider.Config_Ch3.cLRCoxaMin;
import static tomrad.spider.Config_Ch3.cLRCoxaPin;
import static tomrad.spider.Config_Ch3.cLRFemurMax;
import static tomrad.spider.Config_Ch3.cLRFemurMin;
import static tomrad.spider.Config_Ch3.cLRFemurPin;
import static tomrad.spider.Config_Ch3.cLRTibiaMax;
import static tomrad.spider.Config_Ch3.cLRTibiaMin;
import static tomrad.spider.Config_Ch3.cLRTibiaPin;
import static tomrad.spider.Config_Ch3.cRFCoxaMax;
import static tomrad.spider.Config_Ch3.cRFCoxaMin;
import static tomrad.spider.Config_Ch3.cRFCoxaPin;
import static tomrad.spider.Config_Ch3.cRFFemurMax;
import static tomrad.spider.Config_Ch3.cRFFemurMin;
import static tomrad.spider.Config_Ch3.cRFFemurPin;
import static tomrad.spider.Config_Ch3.cRFTibiaMax;
import static tomrad.spider.Config_Ch3.cRFTibiaMin;
import static tomrad.spider.Config_Ch3.cRFTibiaPin;
import static tomrad.spider.Config_Ch3.cRMCoxaMax;
import static tomrad.spider.Config_Ch3.cRMCoxaMin;
import static tomrad.spider.Config_Ch3.cRMCoxaPin;
import static tomrad.spider.Config_Ch3.cRMFemurMax;
import static tomrad.spider.Config_Ch3.cRMFemurMin;
import static tomrad.spider.Config_Ch3.cRMFemurPin;
import static tomrad.spider.Config_Ch3.cRMTibiaMax;
import static tomrad.spider.Config_Ch3.cRMTibiaMin;
import static tomrad.spider.Config_Ch3.cRMTibiaPin;
import static tomrad.spider.Config_Ch3.cRRCoxaMax;
import static tomrad.spider.Config_Ch3.cRRCoxaMin;
import static tomrad.spider.Config_Ch3.cRRCoxaPin;
import static tomrad.spider.Config_Ch3.cRRFemurMax;
import static tomrad.spider.Config_Ch3.cRRFemurMin;
import static tomrad.spider.Config_Ch3.cRRFemurPin;
import static tomrad.spider.Config_Ch3.cRRTibiaMax;
import static tomrad.spider.Config_Ch3.cRRTibiaMin;
import static tomrad.spider.Config_Ch3.cRRTibiaPin;
import static tomrad.spider.IkRoutines.BalanceMode;
import static tomrad.spider.IkRoutines.GaitPosX;
import static tomrad.spider.IkRoutines.GaitPosY;
import static tomrad.spider.IkRoutines.GaitPosZ;
import static tomrad.spider.IkRoutines.GaitRotY;
import static tomrad.spider.IkRoutines.LegPosX;
import static tomrad.spider.IkRoutines.LegPosY;
import static tomrad.spider.IkRoutines.LegPosZ;
import static tomrad.spider.IkRoutines.cInitPosX;
import static tomrad.spider.IkRoutines.cInitPosY;
import static tomrad.spider.IkRoutines.cInitPosZ;
import static tomrad.spider.SingleLeg.AllDown;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Servo {

	private static final char CR = 13;

	@Autowired
	private Logger log;

	@Autowired
	GpioWiring wiringPi;

	// Build tables for Leg configuration like I/O and MIN/MAX values to easy access
	// values using a FOR loop
	// Constants are still defined as single values in the cfg file to make it easy
	// to read/configure

	// SSC Pin numbers
	String[] cCoxaPin = new String[] { cRRCoxaPin, cRMCoxaPin, cRFCoxaPin, cLRCoxaPin, cLMCoxaPin, cLFCoxaPin };
	String[] cFemurPin = new String[] { cRRFemurPin, cRMFemurPin, cRFFemurPin, cLRFemurPin, cLMFemurPin, cLFFemurPin };
	String[] cTibiaPin = new String[] { cRRTibiaPin, cRMTibiaPin, cRFTibiaPin, cLRTibiaPin, cLMTibiaPin, cLFTibiaPin };

	// Min / Max values
	int[] cCoxaMin = new int[] { cRRCoxaMin, cRMCoxaMin, cRFCoxaMin, cLRCoxaMin, cLMCoxaMin, cLFCoxaMin };
	int[] cCoxaMax = new int[] { cRRCoxaMax, cRMCoxaMax, cRFCoxaMax, cLRCoxaMax, cLMCoxaMax, cLFCoxaMax };
	int[] cFemurMin = new int[] { cRRFemurMin, cRMFemurMin, cRFFemurMin, cLRFemurMin, cLMFemurMin, cLFFemurMin };
	int[] cFemurMax = new int[] { cRRFemurMax, cRMFemurMax, cRFFemurMax, cLRFemurMax, cLMFemurMax, cLFFemurMax };
	int[] cTibiaMin = new int[] { cRRTibiaMin, cRMTibiaMin, cRFTibiaMin, cLRTibiaMin, cLMTibiaMin, cLFTibiaMin };
	int[] cTibiaMax = new int[] { cRRTibiaMax, cRMTibiaMax, cRFTibiaMax, cLRTibiaMax, cLMTibiaMax, cLFTibiaMax };

	// --------------------------------------------------------------------
	// [TIMING]
	/** Total Cycle time (in milliseconds) */
	long CycleTime = 0;
	/** Start time of the calculation cycles (in milliseconds) */
	long lTimerStart = 0;
	/** End time of the calculation cycles (in milliseconds) */
	long lTimerEnd = 0;
	/** Previous time for the servo updates */
	int PrevSSCTime = 0;
	/** Time for servo updates */
	int SSCTime = 0;

	// --------------------------------------------------------------------
	// [Simple function to get the current time in milliseconds ]
	private long GetCurrentTime() {
		long lCurrentTime = System.currentTimeMillis();
		return lCurrentTime; // switched back to basic
	}

	// --------------------------------------------------------------------
	// Startet den Timer
	void StartTimer() {
		lTimerStart = GetCurrentTime();
		return;
	}

	public static class CheckAnglesResult {
		public final int[] coxaAngle;
		public final int[] femurAngle;
		public final int[] tibiaAngle;

		public CheckAnglesResult(int[] coxaAngle1, int[] femurAngle1, int[] tibiaAngle1) {
			super();
			this.coxaAngle = coxaAngle1;
			this.femurAngle = femurAngle1;
			this.tibiaAngle = tibiaAngle1;
		}
	}

	// --------------------------------------------------------------------
	// [CHECK ANGLES] Checks the mechanical limits of the servos
	public CheckAnglesResult CheckAngles(int[] coxaAngle, int[] femurAngle, int[] tibiaAngle) {
		log.debug("CheckAngles: cCoxaMin={}", Arrays.toString(cCoxaMin));
		log.debug("CheckAngles: cCoxaMax={}", Arrays.toString(cCoxaMax));
		log.debug("CheckAngles: cFemurMin={}", Arrays.toString(cFemurMin));
		log.debug("CheckAngles: cFemurMax={}", Arrays.toString(cFemurMax));
		log.debug("CheckAngles: cTibiaMin={}", Arrays.toString(cTibiaMin));
		log.debug("CheckAngles: cTibiaMax={}", Arrays.toString(cTibiaMax));

		for (int LegIndex = 0; LegIndex < 6; LegIndex++) {
			coxaAngle[LegIndex] = Math.min(Math.max(coxaAngle[LegIndex], cCoxaMin[LegIndex]), cCoxaMax[LegIndex]);
			femurAngle[LegIndex] = Math.min(Math.max(femurAngle[LegIndex], cFemurMin[LegIndex]), cFemurMax[LegIndex]);
			tibiaAngle[LegIndex] = Math.min(Math.max(tibiaAngle[LegIndex], cTibiaMin[LegIndex]), cTibiaMax[LegIndex]);
		}

		log.debug("CheckAngles: CoxaAngle={}", Arrays.toString(coxaAngle));
		log.debug("CheckAngles: FemurAngle={}", Arrays.toString(femurAngle));
		log.debug("CheckAngles: TibiaAngle={}", Arrays.toString(tibiaAngle));

		return new CheckAnglesResult(coxaAngle, femurAngle, tibiaAngle);
	}

	// --------------------------------------------------------------------
	// [SERVO DRIVER MAIN] Updates the positions of the servos
	public boolean ServoDriverMain(boolean Eyes, boolean HexOn, boolean Prev_HexOn, int InputTimeDelay,
			int SpeedControl, TravelLength travelLength, int[] coxaAngle, int[] femurAngle, int[] tibiaAngle)
			throws IOException {
		log.debug("ServoDriveMain: HexOn={}, Prev_HexOn={}\n", HexOn, Prev_HexOn);

		if (HexOn) {

			log.debug("ServoDriverMain: switched on");

			if (HexOn && !Prev_HexOn) {
				sound(new int[][] { { 60, 4000 }, { 80, 4500 }, { 100, 5000 } });
				Eyes = true;
			}

			log.debug("ServoDriverMain: NomGaitSpeed={}", Gait.NomGaitSpeed);
			log.debug("ServoDriverMain: InputTimeDelay={}", InputTimeDelay);
			log.debug("ServoDriverMain: SpeedControl={}", SpeedControl);

			// Set SSC time
			if (travelLength.isInMotion()) {
				SSCTime = Gait.NomGaitSpeed + (InputTimeDelay * 2) + SpeedControl;

				// Add aditional delay when Balance mode is on
				if (BalanceMode) {
					SSCTime += 100;
				}
			} else { // Movement speed excl. Walking
				SSCTime = 200 + SpeedControl;
			}

			// Sync BAP with SSC while walking to ensure the prev is completed before
			// sending the next one
			if ((GaitPosX[Gait.cRF] != 0 || GaitPosX[Gait.cRM] != 0 || GaitPosX[Gait.cRR] != 0
					|| GaitPosX[Gait.cLF] != 0 || GaitPosX[Gait.cLM] != 0 || GaitPosX[Gait.cLR] != 0
					|| GaitPosY[Gait.cRF] != 0 || GaitPosY[Gait.cRM] != 0 || GaitPosY[Gait.cRR] != 0
					|| GaitPosY[Gait.cLF] != 0 || GaitPosY[Gait.cLM] != 0 || GaitPosY[Gait.cLR] != 0
					|| GaitPosZ[Gait.cRF] != 0 || GaitPosZ[Gait.cRM] != 0 || GaitPosZ[Gait.cRR] != 0
					|| GaitPosZ[Gait.cLF] != 0 || GaitPosZ[Gait.cLM] != 0 || GaitPosZ[Gait.cLR] != 0
					|| GaitRotY[Gait.cRF] != 0 || GaitRotY[Gait.cRM] != 0 || GaitRotY[Gait.cRR] != 0
					|| GaitRotY[Gait.cLF] != 0 || GaitRotY[Gait.cLM] != 0 || GaitRotY[Gait.cLR] != 0)) {
				// Get endtime and calculate wait time
				lTimerEnd = GetCurrentTime();
				CycleTime = lTimerEnd - lTimerStart;

				log.debug("ServoDriverMain: PrevSSCTime={}", PrevSSCTime);
				log.debug("ServoDriverMain: CycleTime={}", CycleTime);
				// Wait for previous commands to be completed while walking
				// Min 1 ensures that there always is a value in the pause command
				pause((int) Math.max((PrevSSCTime - CycleTime - 45), 1));
			}
			pause(15);
			PrevSSCTime = ServoDriver(SSCTime, coxaAngle, femurAngle, tibiaAngle);
		} else {
			log.debug("ServoDriverMain: switched off");

			// Turn the bot off
			if (Prev_HexOn) {
				if (!AllDown) {
					SSCTime = 600;
					PrevSSCTime = ServoDriver(SSCTime, coxaAngle, femurAngle, tibiaAngle);
					sound(new int[][] { { 100, 5000 }, { 80, 4500 }, { 60, 4000 } });
					pause(600);
				} else {
					FreeServos();
					Eyes = false;
				}
			}
		}
		return Eyes;
	}

	// --------------------------------------------------------------------
	// [SERVO DRIVER] Updates the positions of the servos
	private int ServoDriver(int SSCTime, int[] coxaAngle, int[] femurAngle, int[] tibiaAngle) throws IOException {

		// Update Right Legs
		for (int LegIndex = 0; LegIndex < 3; LegIndex++) {
			serout("#", cCoxaPin[LegIndex], "P", angleToPulseWitdh(-coxaAngle[LegIndex]));
			serout("#", cFemurPin[LegIndex], "P", angleToPulseWitdh(-femurAngle[LegIndex]));
			serout("#", cTibiaPin[LegIndex], "P", angleToPulseWitdh(-tibiaAngle[LegIndex]));
		}

		// Update Left Legs
		for (int LegIndex = 3; LegIndex < 6; LegIndex++) {
			serout("#", cCoxaPin[LegIndex], "P", angleToPulseWitdh(coxaAngle[LegIndex]));
			serout("#", cFemurPin[LegIndex], "P", angleToPulseWitdh(femurAngle[LegIndex]));
			serout("#", cTibiaPin[LegIndex], "P", angleToPulseWitdh(tibiaAngle[LegIndex]));
		}

		// Send <CR>
		serout("T", SSCTime, "\r");

		PrevSSCTime = SSCTime;
		return PrevSSCTime;
	}

	private int angleToPulseWitdh(int angle) {
		return (int) Math.rint(angle / cDegreesPerMicroSecond) + 1500;
	}

	// --------------------------------------------------------------------
	// [FREE SERVOS] Frees all the servos
	private void FreeServos() throws IOException {
		log.debug("FreeServos:");
		for (int LegIndex = 0; LegIndex < 32; LegIndex++) {
			serout("#", LegIndex, "P0");
		}
		serout("T200\r");
		return;
	}

	// --------------------------------------------------------------------
	// [GET SSC VERSION] Checks SSC version number if it ends with "GP"
	// enable the GP player if it does
	private boolean GetSSCVersion() throws IOException {
		pause(10);
		boolean GPEnable = false;
		log.debug("Check SSC-version");
		serout("ver\r");
		String s = readline();
		if (s.endsWith("GP\r")) {
			GPEnable = true;
		} else {
			sound(new int[][] { { 40, 5000 }, { 40, 5000 } });
		}
		// Index = 0
		// while 1:
		// serin(cSSC_IN, cSSC_BAUD, 1000, timeout, [GPVerData[Index]])
		// Index = (Index+1)%3 // shift last 3 chars in data
		//
		//
		// timeout:
		// if (GPVerData[0] + GPVerData[1] + GPVerData[2]) == 164 : // Check if the last
		// 3 chars are G(71) P(80) cr(13)
		// GPEnable = 1
		// else:
		// sound(P9, [(40,5000),(40,5000)])

		pause(10);
		return GPEnable;
	}

	// --------------------------------------------------------------------
	// [INIT SERVOS] Sets start positions for each leg
	boolean InitServos() throws IOException {

		for (int LegIndex = 0; LegIndex < 6; LegIndex++) {
			LegPosX[LegIndex] = cInitPosX[LegIndex]; // Set start positions for each leg
			LegPosY[LegIndex] = cInitPosY[LegIndex];
			LegPosZ[LegIndex] = cInitPosZ[LegIndex];
		}

		// SSC
		SSCTime = 150;

		// ser = serial.Serial("/dev/ttyUSB0", Config_Ch3.cSSC_BAUD, timeout=5);
		wiringPi.serialOpen(Config_Ch3.cSSC_DEVICE, Config_Ch3.cSSC_BAUD);
		boolean GPEnable = GetSSCVersion();

		return GPEnable;
	}

	// --------------------------------------------------------------------
	// [GP PLAYER]
	int GPStatSeq = 0;
	int GPStatFromStep = 0;
	int GPStatToStep = 0;
	int GPStatTime = 0;

	public int GPPlayer(int GPStart, int GPSeq) throws IOException {
		log.debug("GPPlayer: GPStart={}, GPSeq={}", GPStart, GPSeq);
		// Start sequence
		if (GPStart == 1) {
			serout("PL0SQ", GPSeq, "ONCE\r"); // Start sequence

			// Wait for GPPlayer to complete sequence
			while (GPStatSeq != 255 || GPStatFromStep != 0 || GPStatToStep != 0 || GPStatTime != 0) {
				serout("QPL0\r");
				int[] inBytes = new int[4];
				serin(inBytes);
				GPStatSeq = inBytes[0];
				GPStatFromStep = inBytes[1];
				GPStatToStep = inBytes[2];
				GPStatTime = inBytes[3];
			}

			GPStart = 0;
		}
		return GPStart;
	}

	// --------------------------------------------------------------------
	public void pause(int milliseconds) {
		log.debug("pause: milliseconds={}", milliseconds);
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			log.info("Interupted while sleeping: {}", e.getMessage());
		}
		return;
	}

	// --------------------------------------------------------------------
	private void serout(Object... outputData) throws IOException {
		// log.debug("serout:" + Arrays.toString(outputData));
		String x = Arrays.stream(outputData).map(Object::toString).reduce("", String::concat);
		log.debug("serout: x={}", x);
		wiringPi.serialWrite(Charset.forName("US-ASCII"), x);
		return;
	}

	// --------------------------------------------------------------------
	private void serin(int[] inputData) throws IOException {
		wiringPi.readInto(inputData);
		return;
	}

	// --------------------------------------------------------------------
	private String readline() throws IOException {
		log.debug("readline 1");
		String x = wiringPi.serialReadUntil(CR);
		log.debug("readline returned '{}'", x);
		return x;
	}

	// --------------------------------------------------------------------
	// list of tuples of duration in millisecvons and note in Hz (frequency)
	private void sound(int[][] listOfDurationAndNotes) {
		log.debug("sound: {}", Arrays.deepToString(listOfDurationAndNotes));
		return;
	}
}
