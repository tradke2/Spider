package tomrad.spider;

import static tomrad.spider.IkRoutines.BalanceMode;
import static tomrad.spider.IkRoutines.GaitPosX;
import static tomrad.spider.IkRoutines.GaitPosY;
import static tomrad.spider.IkRoutines.GaitPosZ;
import static tomrad.spider.IkRoutines.LegPosX;
import static tomrad.spider.IkRoutines.LegPosY;
import static tomrad.spider.IkRoutines.LegPosZ;
import static tomrad.spider.IkRoutines.cInitPosY;
import static tomrad.spider.IkRoutines.cOffsetX;
import static tomrad.spider.IkRoutines.cOffsetZ;
import static tomrad.spider.IkRoutines.DEGREES_PER_RADIANT;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Balance {

	@Autowired
	private Logger logger;

	@Autowired
	private Trig trig;

	public static class BalanceValue {
		public double totalTransX;
		public double totalTransY;
		public double totalTransZ;
		public double totalYBal;
		public double totalZBal;
		public double totalXBal;

		public BalanceValue(double totalTransX, double totalTransY, double totalTransZ, double totalYBal,
				double totalZBal, double totalXBal) {
			this.totalTransX = totalTransX;
			this.totalTransY = totalTransY;
			this.totalTransZ = totalTransZ;
			this.totalXBal = totalXBal;
			this.totalYBal = totalYBal;
			this.totalZBal = totalZBal;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("BalanceValue [totalTransX=").append(totalTransX).append(", totalTransY=")
					.append(totalTransY).append(", totalTransZ=").append(totalTransZ).append(", totalYBal=")
					.append(totalYBal).append(", totalZBal=").append(totalZBal).append(", totalXBal=").append(totalXBal)
					.append("]");
			return builder.toString();
		}
	}

	// --------------------------------------------------------------------
	// [BalCalcOneLeg]
	private BalanceValue BalCalcOneLeg(double PosX, double PosZ, double PosY, BalanceValue input, int BalLegNr) {

		// Calculating totals from center of the body to the feet
		double TotalZ = cOffsetZ[BalLegNr] + PosZ;
		double TotalX = cOffsetX[BalLegNr] + PosX;
		double TotalY = 150 + PosY; // using the value 150 to lower the centerpoint of rotation //BodyPosY +
		input.totalTransY = input.totalTransY + PosY;
		input.totalTransZ = input.totalTransZ + TotalZ;
		input.totalTransX = input.totalTransX + TotalX;

		double Atan = trig.getBoogTan(TotalX, TotalZ);
		input.totalYBal = input.totalYBal + Atan * DEGREES_PER_RADIANT;

		Atan = trig.getBoogTan(TotalX, TotalY);
		input.totalZBal = input.totalZBal + Atan * DEGREES_PER_RADIANT;

		Atan = trig.getBoogTan(TotalZ, TotalY);
		input.totalXBal = input.totalXBal + Atan * DEGREES_PER_RADIANT;

		logger.debug("BalCalcOneLeg: PosX={}, PosZ={}, TotalXTransZ={}", PosX, PosY, PosZ, input.totalTransZ);
		return input;
	}

	// --------------------------------------------------------------------
	// [BalanceBody]
	private BalanceValue BalanceBody(BalanceValue input) {
		input.totalTransZ = input.totalTransZ / 6;
		input.totalTransX = input.totalTransX / 6;
		input.totalTransY = input.totalTransY / 6;

		if (input.totalYBal > 0) // Rotate balance circle by +/- 180 deg
		{
			input.totalYBal = input.totalYBal - 180;
		} else {
			input.totalYBal = input.totalYBal + 180;
		}

		if (input.totalZBal < -180) // Compensate for extreme balance positions that causes owerflow
		{
			input.totalZBal = input.totalZBal + 360;
		}

		if (input.totalXBal < -180) // Compensate for extreme balance positions that causes owerflow
		{
			input.totalXBal = input.totalXBal + 360;
		}

		// Balance rotation
		input.totalYBal = -input.totalYBal / 6;
		input.totalXBal = -input.totalXBal / 6;
		input.totalZBal = input.totalZBal / 6;

		return input;
	}

	// --------------------------------------------------------------------
	// [CalcBalance]
	public BalanceValue CalcBalance() {

		// reset values used for calculation of balance
		BalanceValue result = new BalanceValue(0, 0, 0, 0, 0, 0);
		if (BalanceMode) {
			for (int LegIndex = 0; LegIndex < 3; LegIndex++) // balance calculations for all Right legs
			{
				result = BalCalcOneLeg(-LegPosX[LegIndex] + GaitPosX[LegIndex], LegPosZ[LegIndex] + GaitPosZ[LegIndex],
						LegPosY[LegIndex] - cInitPosY[LegIndex] + GaitPosY[LegIndex], result, LegIndex);
			}

			for (int LegIndex = 3; LegIndex < 6; LegIndex++) // balance calculations for all Left legs
			{
				result = BalCalcOneLeg(LegPosX[LegIndex] + GaitPosX[LegIndex], LegPosZ[LegIndex] + GaitPosZ[LegIndex],
						LegPosY[LegIndex] - cInitPosY[LegIndex] + GaitPosY[LegIndex], result, LegIndex);
			}

			result = BalanceBody(result);
		}

		return result;
	}

}
