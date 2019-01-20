package tomrad.spider;

import static tomrad.spider.Config_Ch3.cCoxaLength;
import static tomrad.spider.Config_Ch3.cFemurLength;
import static tomrad.spider.Config_Ch3.cLFCoxaAngle;
import static tomrad.spider.Config_Ch3.cLFInitPosX;
import static tomrad.spider.Config_Ch3.cLFInitPosY;
import static tomrad.spider.Config_Ch3.cLFInitPosZ;
import static tomrad.spider.Config_Ch3.cLFOffsetX;
import static tomrad.spider.Config_Ch3.cLFOffsetZ;
import static tomrad.spider.Config_Ch3.cLMCoxaAngle;
import static tomrad.spider.Config_Ch3.cLMInitPosX;
import static tomrad.spider.Config_Ch3.cLMInitPosY;
import static tomrad.spider.Config_Ch3.cLMInitPosZ;
import static tomrad.spider.Config_Ch3.cLMOffsetX;
import static tomrad.spider.Config_Ch3.cLMOffsetZ;
import static tomrad.spider.Config_Ch3.cLRCoxaAngle;
import static tomrad.spider.Config_Ch3.cLRInitPosX;
import static tomrad.spider.Config_Ch3.cLRInitPosY;
import static tomrad.spider.Config_Ch3.cLRInitPosZ;
import static tomrad.spider.Config_Ch3.cLROffsetX;
import static tomrad.spider.Config_Ch3.cLROffsetZ;
import static tomrad.spider.Config_Ch3.cRFCoxaAngle;
import static tomrad.spider.Config_Ch3.cRFInitPosX;
import static tomrad.spider.Config_Ch3.cRFInitPosY;
import static tomrad.spider.Config_Ch3.cRFInitPosZ;
import static tomrad.spider.Config_Ch3.cRFOffsetX;
import static tomrad.spider.Config_Ch3.cRFOffsetZ;
import static tomrad.spider.Config_Ch3.cRMCoxaAngle;
import static tomrad.spider.Config_Ch3.cRMInitPosX;
import static tomrad.spider.Config_Ch3.cRMInitPosY;
import static tomrad.spider.Config_Ch3.cRMInitPosZ;
import static tomrad.spider.Config_Ch3.cRMOffsetX;
import static tomrad.spider.Config_Ch3.cRMOffsetZ;
import static tomrad.spider.Config_Ch3.cRRCoxaAngle;
import static tomrad.spider.Config_Ch3.cRRInitPosX;
import static tomrad.spider.Config_Ch3.cRRInitPosY;
import static tomrad.spider.Config_Ch3.cRRInitPosZ;
import static tomrad.spider.Config_Ch3.cRROffsetX;
import static tomrad.spider.Config_Ch3.cRROffsetZ;
import static tomrad.spider.Config_Ch3.cTibiaLength;

import java.util.Arrays;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tomrad.spider.Balance.BalanceValue;
import tomrad.spider.Trig.SinCos;

@Component
public class IkRoutines {

	@Autowired
	private Logger logger;

	@Autowired
	private Trig trig;

	// Coxa angle offset
	private int[] cCoxaAngle = new int[] { cRRCoxaAngle, cRMCoxaAngle, cRFCoxaAngle, cLRCoxaAngle, cLMCoxaAngle,
			cLFCoxaAngle };

	// Body Offsets (distance between the center of the body and the center of the
	// coxa)
	public static double[] cOffsetX = new double[] { cRROffsetX, cRMOffsetX, cRFOffsetX, cLROffsetX, cLMOffsetX,
			cLFOffsetX };
	public static double[] cOffsetZ = new double[] { cRROffsetZ, cRMOffsetZ, cRFOffsetZ, cLROffsetZ, cLMOffsetZ,
			cLFOffsetZ };

	// Start positions for the leg
	public static double[] cInitPosX = new double[] { cRRInitPosX, cRMInitPosX, cRFInitPosX, cLRInitPosX, cLMInitPosX,
			cLFInitPosX };
	public static double[] cInitPosY = new double[] { cRRInitPosY, cRMInitPosY, cRFInitPosY, cLRInitPosY, cLMInitPosY,
			cLFInitPosY };
	public static double[] cInitPosZ = new double[] { cRRInitPosZ, cRMInitPosZ, cRFInitPosZ, cLRInitPosZ, cLMInitPosZ,
			cLFInitPosZ };

	// Body Inverse Kinematics
	/** Global Input pitch of the body */
	public static double BodyRotX = 0;
	/** Global Input rotation of the body */
	public static double BodyRotY = 0;
	/** Global Input roll of the body */
	public static double BodyRotZ = 0;

	// Gait
	/** Array containing Relative X position corresponding to the Gait */
	public static double[] GaitPosX = new double[] { 0, 0, 0, 0, 0, 0 };

	/** Array containing Relative Y position corresponding to the Gait */
	public static double[] GaitPosY = new double[] { 0, 0, 0, 0, 0, 0 };

	/** Array containing Relative Z position corresponding to the Gait */
	public static double[] GaitPosZ = new double[] { 0, 0, 0, 0, 0, 0 };

	/** Array containing Relative Y rotation corresponding to the Gait */
	public static double[] GaitRotY = new double[] { 0, 0, 0, 0, 0, 0 };

	// Body position
	public static double BodyPosX = 0; // Global Input for the position of the body
	public static double BodyPosY = 0;
	public static double BodyPosZ = 0;

	/** Actual X Posion of the Leg */
	public static double[] LegPosX = new double[] { 0, 0, 0, 0, 0, 0 };

	/** Actual Y Posion of the Leg */
	public static double[] LegPosY = new double[] { 0, 0, 0, 0, 0, 0 };

	/** Actual Z Posion of the Leg */
	public static double[] LegPosZ = new double[] { 0, 0, 0, 0, 0, 0 };

	// ====================================================================
	// [ANGLES]
	/** Actual Angle of the horizontal hip */
	private int[] CoxaAngle = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
			Integer.MAX_VALUE, Integer.MAX_VALUE };
	/** Actual Angle of the vertical hip */
	private int[] FemurAngle = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
			Integer.MAX_VALUE, Integer.MAX_VALUE };
	/** Actual Angle of the knee */
	private int[] TibiaAngle = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
			Integer.MAX_VALUE, Integer.MAX_VALUE };

	// --------------------------------------------------------------------
	// [Balance]
	public static boolean BalanceMode = false;

	static final double DEGREES_PER_RADIANT = 180.0 / Math.PI;

	static class BodyIkResult {
		public final double x;
		public final double y;
		public final double z;

		public BodyIkResult(double x, double y, double z) {
			super();
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	/**
	 * [BODY INVERSE KINEMATICS]
	 * <ul>
	 * <li>BodyRotX - Global Input pitch of the body</li>
	 * <li>BodyRotY - Global Input rotation of the body</li>
	 * <li>BodyRotZ - Global Input roll of the body</li>
	 * <li>GaitRotY - Input for rotation of a single feet for the gait</li>
	 * <li>PosX - Input position of the feet X</li>
	 * <li>PosY - Input position of the feet Y</li>
	 * <li>PosZ - Input position of the feet Z</li>
	 * <li>SinB - Sin buffer for BodyRotX</li>
	 * <li>CosB - Cos buffer for BodyRotX</li>
	 * <li>SinG - Sin buffer for BodyRotZ</li>
	 * <li>CosG - Cos buffer for BodyRotZ</li>
	 * <li>BodyIKPosX - Output Position X of feet with Rotation</li>
	 * <li>BodyIKPosY - Output Position Y of feet with Rotation</li>
	 * <li>BodyIKPosZ - Output Position Z of feet with Rotation</li>
	 * </ul>
	 * 
	 * @param balanceValue
	 *            TODO
	 */
	protected BodyIkResult BodyIK(double PosX, double PosZ, double PosY, BalanceValue balanceValue, double GaitRotY,
			int BodyIKLeg) {

		logger.debug("BodyIK: PosX={}, PosY={}, PosZ={}", PosX, PosY, PosZ);
		logger.debug("BodyIK: BalanceValue={}", balanceValue);
		logger.debug("BodyIK: BodyRotX={}, BodyRotY={}, BodyRotZ={}", BodyRotX, BodyRotY, BodyRotZ);
		logger.debug("BodyIK: GaitRotY={}, BodyIKLeg={}", GaitRotY, BodyIKLeg);

		// Calculating totals from center of the body to the feet
		double TotalZ = cOffsetZ[BodyIKLeg] + PosZ; // Total Z distance between the center of the body and the feet
		double TotalX = cOffsetX[BodyIKLeg] + PosX; // Total X distance between the center of the body and the feet
		double TotalY = PosY; // PosY are equal to a "TotalY"

		// Successive global rotation matrix:
		// Math shorts for rotation: Alfa (A) = Xrotate, Beta (B) = Zrotate, Gamma (G) =
		// Yrotate
		// Sinus Alfa = sinA, cosinus Alfa = cosA. and so on...

		// First calculate sinus and cosinus for each rotation:
		SinCos sinCosG = trig.getSinCos(BodyRotX + balanceValue.totalXBal);
		SinCos sinCosB = trig.getSinCos(BodyRotZ + balanceValue.totalZBal);
		SinCos sinCosA = trig.getSinCos(BodyRotY + GaitRotY + balanceValue.totalYBal);

		// Calcualtion of rotation matrix:
		// BodyIKPosX = TotalX - (TotalX*CosA*CosB - TotalZ*CosB*SinA + PosY*SinB)
		// BodyIKPosZ = TotalZ - (TotalX*CosG*SinA + TotalX*CosA*SinB*SinG +
		// TotalZ*CosA*CosG - TotalZ*SinA*SinB*SinG
		// - PosY*CosB*SinG)
		// BodyIKPosY = PosY - (TotalX*SinA*SinG - TotalX*CosA*CosG*SinB +
		// TotalZ*CosA*SinG + TotalZ*CosG*SinA*SinB
		// + PosY*CosB*CosG)
		double BodyIKPosX = TotalX - ( //
		TotalX * sinCosA.cos * sinCosB.cos //
				- TotalZ * sinCosB.cos * sinCosA.sin //
				+ TotalY * sinCosB.sin);
		double BodyIKPosZ = TotalZ - ( //
		TotalX * sinCosG.cos * sinCosA.sin //
				+ TotalX * sinCosA.cos * sinCosB.sin * sinCosG.sin + TotalZ * sinCosA.cos * sinCosG.cos //
				- TotalZ * sinCosA.sin * sinCosB.sin * sinCosG.sin - TotalY * sinCosB.cos * sinCosG.sin);
		double BodyIKPosY = TotalY - ( //
		TotalX * sinCosA.sin * sinCosG.sin //
				- TotalX * sinCosA.cos * sinCosG.cos * sinCosB.sin + TotalZ * sinCosA.cos * sinCosG.sin //
				+ TotalZ * sinCosG.cos * sinCosA.sin * sinCosB.sin + TotalY * sinCosB.cos * sinCosG.cos);

		logger.debug("BodyIK: BodyIKPosX={}, BodyIKPosY={}, BodyIKPosZ={}", BodyIKPosX, BodyIKPosY, BodyIKPosZ);
		return new BodyIkResult(BodyIKPosX, BodyIKPosY, BodyIKPosZ);
	}

	static class LegIkResult {
		public final int femurAngle;
		public final int tibiaAngle;
		public final int coxaAngle;

		public LegIkResult(int coxaAngle, int femurAngle, int tibiaAngle) {
			this.coxaAngle = coxaAngle;
			this.femurAngle = femurAngle;
			this.tibiaAngle = tibiaAngle;
		}
	}

	/**
	 * [LEG INVERSE KINEMATICS]
	 * <p>
	 * Calculates the angles of the coxa, femur and tibia for the given position of
	 * the feet.
	 * </p>
	 * <ul>
	 * <li>IKFeetPosX - Input position of the Feet X</li>
	 * <li>IKFeetPosY - Input position of the Feet Y</li>
	 * <li>IKFeetPosZ - Input Position of the Feet Z</li>
	 * <li>FemurAngle1 - Output Angle of Femur in degrees</li>
	 * <li>TibiaAngle1 - Output Angle of Tibia in degrees</li>
	 * <li>CoxaAngle1 - Output Angle of Coxa in degrees</li>
	 * </ul>
	 * 
	 * @param IKFeetPosX
	 *            Input position of the Feet X
	 * @param IKFeetPosY
	 *            Input position of the Feet Y
	 * @param IKFeetPosZ
	 *            Input Position of the Feet Z
	 * @return Values of angles of coxa, femur and tibia
	 * @throws IKSolutionError If there is no solution for the given arguments.
	 */
	protected LegIkResult LegIK(double IKFeetPosX, double IKFeetPosY, double IKFeetPosZ) throws IKSolutionError {

		logger.debug("LegIK: IKFeetPosX={}, IKFeetPosY={}, IKFeetPosZ={}", IKFeetPosX, IKFeetPosY, IKFeetPosZ);

		// Length between the Coxa and Feet
		double IKFeetPosXZ = Math.sqrt(IKFeetPosX * IKFeetPosX + IKFeetPosZ * IKFeetPosZ) - cCoxaLength;
		logger.trace("LegIK: IKFeetPosXZ={}", IKFeetPosXZ);

		// IKSW - Length between shoulder and wrist
		double IKSW = Math.sqrt(IKFeetPosXZ * IKFeetPosXZ + IKFeetPosY * IKFeetPosY);
		logger.trace("LegIK: IKSW={}", IKSW);

		// IKA1 - Angle between SW line and the ground in rad
		double IKA1 = trig.getBoogTan(IKFeetPosXZ, IKFeetPosY);
		logger.trace("LegIK: IKA1={}", IKA1);

		// IKA2 - Angle of the line S>W with respect to the femur in radians
		double Temp1 = (cFemurLength * cFemurLength) - (cTibiaLength * cTibiaLength) + (IKSW * IKSW);
		double Temp2 = (2 * cFemurLength) * IKSW;
		logger.trace("LegIK: Temp1={}, Temp2={}", Temp1, Temp2);

		// Angle of the line S>W with respect to the femur in radians, decimals = 4
		double IKA2 = Math.acos(Temp1 / Temp2);
		logger.trace("LegIK: IKA2={}", IKA2);

		// IKFemurAngle
		double femurAngle = 90 - ((IKA1 + IKA2) * DEGREES_PER_RADIANT);
		logger.trace("LegIK: FemurAngle={}", femurAngle);

		// IKTibiaAngle
		Temp1 = (((cFemurLength * cFemurLength) + (cTibiaLength * cTibiaLength)) - (IKSW * IKSW));
		Temp2 = (2 * cFemurLength * cTibiaLength);
		logger.trace("LegIK: Temp1={}, Temp2={}", Temp1, Temp2);
		double tibiaAngle = Math.acos(Temp1 / Temp2) * DEGREES_PER_RADIANT - 90;
		logger.trace("LegIK: TibiaAngle={}", tibiaAngle);

		// IKCoxaAngle
		double BoogTan = trig.getBoogTan(IKFeetPosZ, IKFeetPosX);
		double coxaAngle = BoogTan * DEGREES_PER_RADIANT;
		logger.trace("LegIK: CoxaAngle={}", coxaAngle);

		boolean IKSolution = false;
		boolean IKSolutionWarning = false;
		boolean IKSolutionError = false;

		// Set the Solution quality
		if (Double.isNaN(coxaAngle) || Double.isNaN(femurAngle) || Double.isNaN(tibiaAngle)) {
			throw new IKSolutionError(coxaAngle, femurAngle, tibiaAngle, IKFeetPosX, IKFeetPosY, IKFeetPosZ);
		}

		logger.debug("LegIK: IKSolution={}, IKSolutionWarning={}, IKSolutionError={}", IKSolution, IKSolutionWarning,
				IKSolutionError);
		logger.debug("LegIK: CoxaAngle={}, FemurAngle={}, TibiaAngle={}", coxaAngle, femurAngle, tibiaAngle);
		return new LegIkResult((int) Math.rint(coxaAngle), (int) Math.rint(femurAngle), (int) Math.rint(tibiaAngle));
	}

	public static class CalcIkResult {
		public final int[] coxaAngle;
		public final int[] femurAngle;
		public final int[] tibiaAngle;

		public CalcIkResult(int[] coxaAngle, int[] femurAngle, int[] tibiaAngle) {
			this.coxaAngle = coxaAngle;
			this.femurAngle = femurAngle;
			this.tibiaAngle = tibiaAngle;
		}
	}

	// --------------------------------------------------------------------
	// [CALC INVERSE KINEMATIC] Calculates inverse kinematic
	public CalcIkResult CalcIK(BalanceValue balanceValue) throws IKSolutionError {

		logger.debug("CalcIK: LegPosX={}", Arrays.toString(LegPosX));
		logger.debug("CalcIK: LegPosY={}", Arrays.toString(LegPosY));
		logger.debug("CalcIK: LegPosZ={}", Arrays.toString(LegPosZ));
		logger.debug("CalcIK: BodyPos(XYZ): {}, {}, {}", BodyPosX, BodyPosY, BodyPosZ);
		logger.debug("CalcIK: balanceValue: {}", balanceValue.toString());
		logger.debug("CalcIK: GaitPosX={}", Arrays.toString(GaitPosX));
		logger.debug("CalcIK: GaitPosY={}", Arrays.toString(GaitPosY));
		logger.debug("CalcIK: GaitPosZ={}", Arrays.toString(GaitPosZ));

		// Do IK for all Right legs
		for (int LegIndex = 0; LegIndex < 3; LegIndex++) {
			logger.debug("-----");
			logger.debug("CalcIK: LegIndex={}", LegIndex);
			BodyIkResult bodyIkPos = BodyIK(//
					-LegPosX[LegIndex] + BodyPosX + GaitPosX[LegIndex] - balanceValue.totalTransX,
					LegPosZ[LegIndex] + BodyPosZ + GaitPosZ[LegIndex] - balanceValue.totalTransZ,
					LegPosY[LegIndex] + BodyPosY + GaitPosY[LegIndex] - balanceValue.totalTransY, balanceValue,
					GaitRotY[LegIndex], LegIndex);
			LegIkResult legIk = LegIK(//
					LegPosX[LegIndex] - BodyPosX + bodyIkPos.x - GaitPosX[LegIndex] + balanceValue.totalTransX,
					LegPosY[LegIndex] + BodyPosY - bodyIkPos.y + GaitPosY[LegIndex] - balanceValue.totalTransY,
					LegPosZ[LegIndex] + BodyPosZ - bodyIkPos.z + GaitPosZ[LegIndex] - balanceValue.totalTransZ);
			CoxaAngle[LegIndex] = legIk.coxaAngle + cCoxaAngle[LegIndex];
			FemurAngle[LegIndex] = legIk.femurAngle;
			TibiaAngle[LegIndex] = legIk.tibiaAngle;
		}

		// Do IK for all Left legs
		for (int LegIndex = 3; LegIndex < 6; LegIndex++) {
			logger.debug("-----");
			logger.debug("CalcIK: LegIndex={}", LegIndex);
			BodyIkResult bodyIkPos = BodyIK(
					LegPosX[LegIndex] - BodyPosX + GaitPosX[LegIndex] - balanceValue.totalTransX,
					LegPosZ[LegIndex] + BodyPosZ + GaitPosZ[LegIndex] - balanceValue.totalTransZ,
					LegPosY[LegIndex] + BodyPosY + GaitPosY[LegIndex] - balanceValue.totalTransY, balanceValue,
					GaitRotY[LegIndex], LegIndex);
			LegIkResult legIk = LegIK(
					LegPosX[LegIndex] + BodyPosX - bodyIkPos.x + GaitPosX[LegIndex] - balanceValue.totalTransX,
					LegPosY[LegIndex] + BodyPosY - bodyIkPos.y + GaitPosY[LegIndex] - balanceValue.totalTransY,
					LegPosZ[LegIndex] + BodyPosZ - bodyIkPos.z + GaitPosZ[LegIndex] - balanceValue.totalTransZ);
			CoxaAngle[LegIndex] = legIk.coxaAngle + cCoxaAngle[LegIndex];
			FemurAngle[LegIndex] = legIk.femurAngle;
			TibiaAngle[LegIndex] = legIk.tibiaAngle;
		}

		// Write IK errors to leds
		// LedC = IKSolutionWarning;
		// LedA = IKSolutionError;

		logger.debug("CalcIK: CoxaAngle={}", Arrays.toString(CoxaAngle));
		logger.debug("CalcIK: FemurAngle={}", Arrays.toString(FemurAngle));
		logger.debug("CalcIK: TibiaAngle={}", Arrays.toString(TibiaAngle));

		return new CalcIkResult(CoxaAngle, FemurAngle, TibiaAngle);
	}

//	private String[] round(double[] input) {
//		DoubleStream of = Arrays.stream(input);
//		Stream<String> s = of.mapToObj(d -> {
//			return String.format("%.2f", d);
//		});
//		List<String> x = s.collect(Collectors.toList());
//		return x.toArray(new String[0]);
//	}

	// --------------------------------------------------------------------
	// [INIT INVERSE KINEMATICS] Sets body position and rotation to 0
	public void InitIK() {
		// Body Positions
		BodyPosX = 0;
		BodyPosY = 0;
		BodyPosZ = 0;

		// Body Rotations
		BodyRotX = 0;
		BodyRotY = 0;
		BodyRotZ = 0;
		BalanceMode = false;
		logger.debug("InitIK: Body-positions set to 0");
	}
}
