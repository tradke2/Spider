package tomrad.spider;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Trig {

	@Autowired
	private Logger logger;

	public static class SinCos {
		public final double sin;
		public final double cos;

		public SinCos(double sin, double cos) {
			super();
			this.sin = sin;
			this.cos = cos;
		}
	}

	/**
	 * Get the sinus and cosinus from the angle +/- multiple circles
	 * <ul>
	 * <li>AngleDeg - Input Angle in degrees</li>
	 * <li>Sin - Output Sinus of the given Angle</li>
	 * <li>Cos - Output Cosinus of the given Angle</li>
	 * </ul>
	 * 
	 * @param angleDegrees
	 *            angle in degrees
	 * @return sinus and cosinus of the given angle
	 */
	public SinCos getSinCos(double angleDegrees) {
		logger.trace("AngleDeg={}", angleDegrees);
		double angleRadiants = Math.toRadians(angleDegrees);
		SinCos result = new SinCos(Math.sin(angleRadiants), Math.cos(angleRadiants));
		logger.trace("Sin={}, Cos={}", result.sin, result.cos);
		return result;
	}

	/**
	 * Gets the Inverse Tangus from X/Y with the where Y can be zero or negative
	 * <ul>
	 * <li>BoogTanX - Input X</li>
	 * <li>BoogTanY - Input Y</li>
	 * <li>BoogTan - Output BOOGTANs(X/Y)</li>
	 * </ul>
	 * 
	 * @param x 
	 * @param y
	 * @return the inverse tangus of x/y
	 */
	public double getBoogTan(double x, double y) {
		logger.trace("BoogTanX={}, BoogTanY={}", x, y);
		double result = Math.atan2(x, y);
		logger.trace("BoogTan= {}", result);
		return result;
	}

}
