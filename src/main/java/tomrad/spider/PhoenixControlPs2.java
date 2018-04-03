package tomrad.spider;

import static tomrad.spider.DelayedFormatter.format;
import static tomrad.spider.Gait.GaitType;
import static tomrad.spider.Gait.LegLiftHeight;
import static tomrad.spider.Gait.cRF;
import static tomrad.spider.Gait.cTravelDeadZone;
import static tomrad.spider.IkRoutines.BalanceMode;
import static tomrad.spider.IkRoutines.BodyPosX;
import static tomrad.spider.IkRoutines.BodyPosY;
import static tomrad.spider.IkRoutines.BodyPosZ;
import static tomrad.spider.IkRoutines.BodyRotX;
import static tomrad.spider.IkRoutines.BodyRotY;
import static tomrad.spider.IkRoutines.BodyRotZ;
import static tomrad.spider.Phoenix.GPSeq;
import static tomrad.spider.Phoenix.GPStart;
import static tomrad.spider.Phoenix.SpeedControl;
import static tomrad.spider.SingleLeg.SelectedLeg;
import static tomrad.spider.SpiController.byteArrayToString;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tomrad.spider.Gait.TravelLength;

/**
 * The control input subroutine for the phoenix software is placed in this file.
 * Original MBasic version by Jeroen Janssen (aka Xan).
 * <p>
 * Hardware setup: PS2 version
 * </p>
 * Walk method 1:
 * <ul>
 * <li>Left Stick: Walk/Strafe</li>
 * <li>Right Stick: Rotate</li>
 * </ul>
 * 
 * Walk method 2:
 * <ul>
 * <li>Left Stick: Disable</li>
 * <li>Right Stick: Walk/Rotate</li>
 * </ul>
 * 
 * 
 * PS2 CONTROLS:
 * <p>
 * <dl>
 * <dt>[Common Controls]</dt>
 * <dd>
 * <dl>
 * <dt>Start</dt>
 * <dd>Turn on/off the bot</dd>
 * <dt>L1</dt>
 * <dd>Toggle Shift mode</dd>
 * <dt>L2</dt>
 * <dd>Toggle Rotate mode</dd>
 * <dt>Circle</dt>
 * <dd>Toggle Single leg mode</dd>
 * <dt>Square</dt>
 * <dd>Toggle Balance mode</dd>
 * <dt>Triangle</dt>
 * <dd>Move body to 35 mm from the ground (walk pos) && back to the ground</dd>
 * <dt>D-Pad up</dt>
 * <dd>Body up 10 mm</dd>
 * <dt>D-Pad down</dt>
 * <dd>Body down 10 mm</dd>
 * <dt>D-Pad left</dt>
 * <dd>decrease speed with 50mS</dd>
 * <dt>D-Pad right</dt>
 * <dd>increase speed with 50mS</dd>
 * </dl>
 * </dd>
 * <dt>[Walk Controls]</dt>
 * <dd>
 * <dl>
 * <dt>Select</dt>
 * <dd>Switch gaits</dd>
 * <dt>Left Stick</dt>
 * <dd>
 * <dl>
 * <dt>(Walk mode 1)</dt>
 * <dd>Walk/Strafe</dd>
 * <dt>(Walk mode 2)</dt>
 * <dd>Disable</dd>
 * </dl>
 * </dd>
 * <dt>Right Stick</dt>
 * <dd>
 * <dl>
 * <dt>(Walk mode 1)</dt>
 * <dd>Rotate</dd>
 * <dt>(Walk mode 2)</dt>
 * <dd>Walk/Rotate</dd>
 * </dl>
 * </dd>
 * <dt>R1</dt>
 * <dd>Toggle Double gait travel speed</dd>
 * <dt>R2</dt>
 * <dd>Toggle Double gait travel length</dd>
 * </dl>
 * </dd>
 * <dt>[Shift Controls]</dt>
 * <dd>
 * <dl>
 * <dt>Left Stick</dt>
 * <dd>Shift body X/Z</dd>
 * <dt>Right Stick</dt>
 * <dd>Shift body Y && rotate body Y</dd>
 * </dl>
 * </dd>
 * <dt>[Rotate Controls]</dt>
 * <dd>
 * <dl>
 * <dt>Left Stick</dt>
 * <dd>Rotate body X/Z</dd>
 * <dt>Right Stick</dt>
 * <dd>Rotate body Y</dd>
 * </dl>
 * </dd>
 * <dt>[Single leg Controls]</dt>
 * <dd>
 * <dl>
 * <dt>Select</dt>
 * <dd>Switch legs</dd>
 * <dt>Left Stick</dt>
 * <dd>Move Leg X/Z (relative)</dd>
 * <dt>Right Stick</dt>
 * <dd>Move Leg Y (absolute)</dd>
 * <dt>R2</dt>
 * <dd>Hold/release leg position</dd>
 * </dl>
 * </dd>
 * <dt>[GP Player Controls]</dt>
 * <dd>
 * <dl>
 * <dt>Select</dt>
 * <dd>Switch Sequences</dd>
 * <dt>R2</dt>
 * <dd>Start Sequence</dd>
 * </dl>
 * </dd>
 * </dl>
 * </p>
 * ====================================================================
 */
public class PhoenixControlPs2 implements Controller {

	private final static Logger log = LoggerFactory.getLogger(PhoenixControlPs2.class);

	private final Gait gait;

	// [CONSTANTS]
	private int WalkMode = 0;
	private int TranslateMode = 1;
	private int RotateMode = 2;
	private int SingleLegMode = 3;
	private int GPPlayerMode = 4;
	// --------------------------------------------------------------------
	// [PS2 Physical Pins The Controller Is Connected To]
	static int PS2DAT = 13; // PS2 Controller DAT (Brown)
	static int PS2CMD = 12; // PS2 controller CMD (Orange)
	static int PS2SEL = 10; // PS2 Controller SEL (Blue)
	static int PS2CLK = 14; // PS2 Controller CLK (White)
//	static int PS2DAT = 21; // PS2 Controller DAT (Brown)
//	static int PS2CMD = 19; // PS2 controller CMD (Orange)
//	static int PS2SEL = 24; // PS2 Controller SEL (Blue)
//	static int PS2CLK = 23; // PS2 Controller CLK (White)
	@SuppressWarnings("unused")
	private byte PadMode = 0x79;
	// --------------------------------------------------------------------
	// [Ps2 Controller Variables]
	private short[] DualShock = new short[7];
	private short[] LastButton = new short[] { 0, 0 };
	// PS2Index = None
	private int BodyYOffset = 0;
	private int BodyYShift = 0;
	private int _ControlMode = 0;
	private boolean _DoubleHeightOn = false;
	private int _DoubleTravelOn = 0;
	private int _WalkMethod = 0;
	// --------------------------------------------------------------------
	// [POSITIONS SINGLE LEG CONTROL]
	public static boolean SLHold = false; // Single leg control mode
	public static double SLLegX = 0;
	public static double SLLegY = 0;
	public static double SLLegZ = 0;

	// --------------------------------------------------------------------
	// [GLOABAL]
	private boolean PrevHexOn = false; // Previous loop state
	private boolean HexOn = false; // Switch to turn on Phoenix

	private int InputTimeDelay = 0; // Delay that depends on the input to get the "sneaking" effect

	boolean _IsWiringpiInitialised = false;

	private Ps2Controller ps2Controller;

	PhoenixControlPs2(Gait gait, GpioWiring wiringPi) {
		this.gait = gait;
		ps2Controller = new Ps2Controller(wiringPi);
	}

	// --------------------------------------------------------------------
	// [InitController] Initialize the PS2 controller
	/*
	 * (non-Javadoc)
	 * 
	 * @see tomrad.spider.Controller#InitController()
	 */
	@Override
	public boolean InitController() {

		log.debug("InitController: setting up pins");
		ps2Controller.setupPins(PS2CMD, PS2DAT, PS2CLK, PS2SEL);

		log.debug("InitController: trying to set mode");
		if (ps2Controller.initializeController() != 1) {
			log.debug("InitController: failed to set mode");			
			return false;
		}
		
		// sound P9,[100\4000, 100\4500, 100\5000]

		_ControlMode = WalkMode;
		_WalkMethod = 0;
		_DoubleTravelOn = 0;
		_DoubleHeightOn = false;

		// goto InitController // Check if the remote is initialized correctly
		log.debug("InitController: successfully initialized");			
		return true;
	}

	// --------------------------------------------------------------------
	// [ControlInput] reads the input data from the PS2 controller && processes the
	// data to the parameters.
	/*
	 * (non-Javadoc)
	 * 
	 * @see tomrad.spider.Controller#ControlInput(tomrad.spider.Gait.TravelLength)
	 */
	@Override
	public TravelLength ControlInput(TravelLength input) {
		log.debug("ControlInput: LastButton[0]={}, LastButton[1]={}\n", format("%x", LastButton[0]),
				format("%x", LastButton[1]));

		double TravelLengthX = input.lengthX;
		double TravelRotationY = input.rotationY;
		double TravelLengthZ = input.lengthZ;

		short[] ps2Data = ps2Controller.readPS2();
		
		if (log.isDebugEnabled()) {
			log.debug("ControlInput: received1={}", byteArrayToString(ps2Data));
		}

		DualShock = Arrays.copyOfRange(ps2Data, 2, 9);

		if (log.isDebugEnabled())
			log.debug("ControlInput: DualShock[0:7]={}", Arrays.toString(Arrays.copyOfRange(DualShock, 0, 7)));

		// Switch bot on/off
		if ((DualShock[1] & 0x08) == 0 && (LastButton[0] & 0x08) != 0) // Start Button test bit3
		{
			log.debug("ControlInput: Start button: HexOn={}, Prev_HexOn={}\n", HexOn, PrevHexOn);
			if (HexOn) {
				// Turn off
				BodyPosX = 0;
				BodyPosY = 0;
				BodyPosZ = 0;
				BodyRotX = 0;
				BodyRotY = 0;
				BodyRotZ = 0;
				TravelLengthX = 0;
				TravelLengthZ = 0;
				TravelRotationY = 0;
				BodyYOffset = 0;
				BodyYShift = 0;
				SelectedLeg = 255;

				HexOn = false;
			} else {
				// Turn on
				HexOn = true;
			}
		}

		log.debug("ControlInput: HexOn={}", HexOn);

		if (HexOn) {
			// [SWITCH MODES]

			// Translate mode
			if ((DualShock[2] & 0x04) == 0 && (LastButton[1] & 0x04) != 0) // L1 Button test bit2
			{
				// sound p9, [50\4000]
				if (_ControlMode != TranslateMode) {
					_ControlMode = TranslateMode;
				} else {
					if (SelectedLeg == 255) {
						_ControlMode = WalkMode;
					} else {
						_ControlMode = SingleLegMode;
					}
				}
			}

			// Rotate mode
			if ((DualShock[2] & 0x01) == 0 && (LastButton[1] & 0x01) != 0) // L2 Button test bit0
			{
				// sound p9, [50\4000]
				if (_ControlMode != RotateMode) {
					_ControlMode = RotateMode;
				} else {
					if (SelectedLeg == 255) {
						_ControlMode = WalkMode;
					} else {
						_ControlMode = SingleLegMode;
					}
				}
			}

			// Single leg mode
			if ((DualShock[2] & 0x20) == 0 && (LastButton[1] & 0x20) != 0) // Circle Button test bit5
			{
				if (Math.abs(TravelLengthX) < cTravelDeadZone //
						&& Math.abs(TravelLengthZ) < cTravelDeadZone //
						&& Math.abs(TravelRotationY * 2) < cTravelDeadZone) //
				{
					// Sound P9,[50\4000]
					if (_ControlMode != SingleLegMode) {
						_ControlMode = SingleLegMode;
						if (SelectedLeg == 255) // Select leg if none is selected
						{
							SelectedLeg = cRF; // Startleg
						}
					} else {
						_ControlMode = WalkMode;
						SelectedLeg = 255;
					}
				}
			}

			// GP Player mode
			if ((DualShock[2] & 0x40) == 0 && (LastButton[1] & 0x40) != 0) // Cross Button test bit6
			{
				// Sound P9,[50\4000]
				if (_ControlMode != GPPlayerMode) {
					_ControlMode = GPPlayerMode;
					GPSeq = 0;
				} else {
					_ControlMode = WalkMode;
				}
			}

			// [Common functions]
			// Switch Balance mode on/off
			if ((DualShock[2] & 0x80) == 0 && (LastButton[1] & 0x80) != 0) // Square Button test bit7
			{
				BalanceMode = !BalanceMode;
				if (BalanceMode) {
					// sound P9,[250\3000]
				} else {
					// sound P9,[100\4000, 50\8000]
				}
			}

			// Stand up, sit down
			if ((DualShock[2] & 0x10) == 0 && (LastButton[1] & 0x10) != 0) // Triangle Button test bit4
			{
				if (BodyYOffset > 0) {
					BodyYOffset = 0;
				} else {
					BodyYOffset = 35;
				}
			}

			if ((DualShock[1] & 0x10) == 0 && (LastButton[0] & 0x10) != 0) // D-Up Button test bit4
			{
				BodyYOffset += 10;
			}

			if ((DualShock[1] & 0x40) == 0 && (LastButton[0] & 0x40) != 0) // D-Down Button test bit6
			{
				BodyYOffset -= 10;
			}

			if ((DualShock[1] & 0x20) == 0 && (LastButton[0] & 0x20) != 0) // D-Right Button test bit5
			{
				if (SpeedControl > 0) {
					SpeedControl -= 50;
					// sound p9, [50\4000]
				}
			}

			if ((DualShock[1] & 0x80) == 0 && (LastButton[0] & 0x80) != 0) // D-Left Button test bit7
			{
				if (SpeedControl < 2000) {
					SpeedControl += 50;
					// sound p9, [50\4000]
				}
			}

			// [Walk functions]
			if (_ControlMode == WalkMode) {
				log.debug("ControlInput: _ControlMode == WalkMode");
				// Switch gates
				if (((DualShock[1] & 0x01) == 0 && (LastButton[0] & 0x01) != 0 // Select Button test bit0
						&& Math.abs(TravelLengthX) < cTravelDeadZone // No movement
						&& Math.abs(TravelLengthZ) < cTravelDeadZone
						&& Math.abs(TravelRotationY * 2) < cTravelDeadZone)) {
					if (GaitType < 7) {
						// Sound P9,[50\4000]
						GaitType += 1;
					} else {
						// Sound P9,[50\4000, 50\4500]
						GaitType = 0;
					}

					// Sound P9,[50\4000+Gaittype*500]
					// DTMFOUT2 9,[GaitType]
					gait.GaitSelect(GaitType);
				}

				// Double leg lift height
				if ((DualShock[2] & 0x08) == 0 && (LastButton[1] & 0x08) != 0) // R1 Button test bit3
				{
					// sound p9, [50\4000]
					_DoubleHeightOn = !_DoubleHeightOn;
					if (_DoubleHeightOn) {
						LegLiftHeight = 80;
					} else {
						LegLiftHeight = 50;
					}
				}

				// Double Travel Length
				if ((DualShock[2] & 0x02) == 0 && (LastButton[1] & 0x02) != 0) // R2 Button test bit1
				{
					// sound p9, [50\4000]
					_DoubleTravelOn ^= 1;
				}

				// Switch between Walk method 1 && Walk method 2
				if ((DualShock[1] & 0x04) == 0 && (LastButton[0] & 0x04) != 0) // R3 Button test bit2
				{
					// sound p9, [50\4000]
					_WalkMethod ^= 1;
				}

				log.debug("ControlInput: _WalkMethod={}", _WalkMethod);

				// Walking
				if (_WalkMethod == 1) // (Walk Methode)
				{
					TravelLengthZ = (DualShock[4] - 128); // Right Stick Up/Down
				} else {
					TravelLengthX = -(DualShock[5] - 128);
					TravelLengthZ = (DualShock[6] - 128);
				}

				if (_DoubleTravelOn == 0) // (Double travel length)
				{
					TravelLengthX /= 2;
					TravelLengthZ /= 2;
				}

				TravelRotationY = -(DualShock[3] - 128) / 4; // Right Stick Left/Right

				log.debug("ControlInput: TravelLengthX={}, TravelLengthZ={}, TravelRotationY={}", TravelLengthX,
						TravelLengthZ, TravelRotationY);
				log.debug("ControlInput: _DoubleTravelOn={}", _DoubleTravelOn);
			}

			// [Translate functions]
			// BodyYShift = 0
			if (_ControlMode == TranslateMode) {
				BodyPosX = (DualShock[5] - 128) / 2;
				BodyPosZ = -(DualShock[6] - 128) / 3;
				BodyRotY = (DualShock[3] - 128) / 6;
				BodyYShift = (-(DualShock[4] - 128) / 2);
			}

			// [Rotate functions]
			if (_ControlMode == RotateMode) {
				BodyRotX = (DualShock[6] - 128) / 8;
				BodyRotY = (DualShock[3] - 128) / 6;
				BodyRotZ = (DualShock[5] - 128) / 8;
				BodyYShift = (-(DualShock[4] - 128) / 2);
			}

			// [Single leg functions]
			if (_ControlMode == SingleLegMode) {
				// Switch leg for single leg control
				if ((DualShock[1] & 0x01) == 0 && (LastButton[0] & 0x01) != 0) // Select Button test bit0
				{
					// Sound P9,[50\4000]
					if (SelectedLeg < 5) {
						SelectedLeg += 1;
					} else {
						SelectedLeg = 0;
					}
				}

				// Single Leg Mode
				if (_ControlMode == SingleLegMode) {
					SLLegX = (DualShock[5] - 128) / 2; // Left Stick Right/Left
					SLLegY = (DualShock[4] - 128) / 10; // Right Stick Up/Down
					SLLegZ = (DualShock[6] - 128) / 2; // Left Stick Up/Down
				}

				// Hold single leg in place
				if ((DualShock[2] & 0x02) == 0 && (LastButton[1] & 0x02) != 0) // R2 Button test bit1
				{
					// sound p9, [50\4000]
					SLHold = !SLHold;
				}
			}

			// [Single leg functions]
			if (_ControlMode == GPPlayerMode) {
				// Switch between sequences
				if ((DualShock[1] & 0x01) == 0 && (LastButton[0] & 0x01) != 0) // Select Button test bit0
				{
					if (GPStart == 0) {
						if (GPSeq < 5) // Max sequence
						{
							// sound p9, [50\3000]
							GPSeq += 1;
						} else {
							// Sound P9,[50\4000, 50\4500]
							GPSeq = 0;
						}
					}
				}

				// Start Sequence
				if ((DualShock[2] & 0x02) == 0 && (LastButton[1] & 0x02) != 0) // R2 Button test bit1
				{
					GPStart = 1;
				}
			}

			// Calculate walking time delay
			InputTimeDelay = 128 - Math.max(Math.max(Math.abs(DualShock[5] - 128), Math.abs(DualShock[6] - 128)),
					Math.abs(DualShock[3] - 128));

		}

		// Calculate BodyPosY
		BodyPosY = Math.max((BodyYOffset + BodyYShift), 0);
		log.debug("ControlInput: BodyPosY={}", BodyPosY);

		// Store previous state
		LastButton[0] = DualShock[1];
		LastButton[1] = DualShock[2];

		log.debug("ControlInput: LastButton[0]={}, LastButton[1]={}\n", format("%x", LastButton[0]),
				format("%x", LastButton[1]));
		return new Gait.TravelLength(TravelLengthX, TravelLengthZ, TravelRotationY);
	}

	@Override
	public boolean isHexOn() {
		return HexOn;
	}

	@Override
	public void setHexOn(boolean hexOn) {
		HexOn = hexOn;
	}

	@Override
	public boolean isPrevHexOn() {
		return PrevHexOn;
	}

	@Override
	public void setPrevHexOn(boolean prevHexOn) {
		PrevHexOn = prevHexOn;
	}

	@Override
	public int getInputTimeDelay() {
		return InputTimeDelay;
	}

	@Override
	public void setInputTimeDelay(int inputTimeDelay) {
		InputTimeDelay = inputTimeDelay;
	}

}
