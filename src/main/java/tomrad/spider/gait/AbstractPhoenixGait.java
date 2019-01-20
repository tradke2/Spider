package tomrad.spider.gait;

public abstract class AbstractPhoenixGait {

	/** Nominal speed of the gait */
	public final int NomGaitSpeed; 
	
	/** Number of steps in gait */
	public final int StepsInGait; 
	
	/** Number of steps that a single leg is lifted (1-3) */
	public final int NrLiftedPos; 
	
	/** Where the leg should be put down to ground */
	//public final byte FrontDownPos; 
	
	/** Normaly: 2, when NrLiftedPos=5: 4 */
	public final int LiftDivFactor; 
	
	/** Number of steps that a leg is on the floor while walking */
	public final int TLDivFactor;
	
	/** If TRUE the outer positions of the ligted legs will be half height */
	public final int HalfLiftHeight; // How high to lift at halfway up.

	/** Init position of the leg. Indices: RR, RM, RF, LR, LM, LF */
	public final byte GaitLegNr[];
	
	/** The gait name */
	public final String Name; 

	protected AbstractPhoenixGait(int nomGaitSpeed, int stepsInGait, int nrLiftedPos, /*byte frontDownPos,*/
			int tLDivFactor, int halfLiftHeight, byte[] gaitLegNr, String pszName) {
		this.NomGaitSpeed = nomGaitSpeed;
		this.StepsInGait = stepsInGait;
		this.NrLiftedPos = nrLiftedPos;
		//this.FrontDownPos = frontDownPos;
		this.LiftDivFactor = nrLiftedPos == 5 ? 4 : 2;
		this.TLDivFactor = tLDivFactor;
		this.HalfLiftHeight = halfLiftHeight;
		this.GaitLegNr = gaitLegNr;
		this.Name = pszName;
	}

}
