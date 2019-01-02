package tomrad.spider;

public class IKSolutionError extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final double coxaAngle;
	private final double femurAngle;
	private final double tibiaAngle;
	private final double posX;
	private final double posY;
	private final double posZ;

	public IKSolutionError(double coxaAngle, double femurAngle, double tibiaAngle, double posX, double posY, double posZ) {
		super(String.format("IKSolutionError: CoxaAngle=%f, FemurAngle=%f, TibiaAngle=%f", coxaAngle, femurAngle, tibiaAngle));
		this.coxaAngle = coxaAngle;
		this.femurAngle = femurAngle;
		this.tibiaAngle = tibiaAngle;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	public double getCoxaAngle() {
		return coxaAngle;
	}

	public double getFemurAngle() {
		return femurAngle;
	}

	public double getTibiaAngle() {
		return tibiaAngle;
	}

	public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}

	public double getPosZ() {
		return posZ;
	}

}
