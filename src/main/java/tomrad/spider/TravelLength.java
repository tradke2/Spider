package tomrad.spider;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TravelLength {
	
	private static final Logger logger = LoggerFactory.getLogger(TravelLength.class);
	
	public final double lengthX;
	public final double lengthZ;
	public final double rotationY;

	public TravelLength(double travelLengthX, double travelLengthZ, double travelRotationY) {
		this.lengthX = travelLengthX;
		this.lengthZ = travelLengthZ;
		this.rotationY = travelRotationY;
	}

	public TravelLength(TravelLength input) {
		this(input.lengthX, input.lengthZ, input.rotationY);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(lengthX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lengthZ);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(rotationY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TravelLength other = (TravelLength) obj;
		if (Double.doubleToLongBits(lengthX) != Double.doubleToLongBits(other.lengthX))
			return false;
		if (Double.doubleToLongBits(lengthZ) != Double.doubleToLongBits(other.lengthZ))
			return false;
		if (Double.doubleToLongBits(rotationY) != Double.doubleToLongBits(other.rotationY))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
//		StringBuilder builder = new StringBuilder();
//		builder.append("TravelLength [lengthX").append(lengthX).append(", lengthZ=").append(lengthZ)
//				.append(", rotationY=").append(rotationY).append("]");
//		return builder.toString();
	}

	public boolean isInMotion() {
		boolean result = Math.abs(lengthX) > Config_Ch3.cTravelDeadZone || Math.abs(lengthZ) > Config_Ch3.cTravelDeadZone
				|| Math.abs(rotationY * 2) > Config_Ch3.cTravelDeadZone;
		logger.trace("{}: isInMotion={}", this, result);
		return result;
	}
}