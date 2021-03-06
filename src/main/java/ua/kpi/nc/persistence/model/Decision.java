package ua.kpi.nc.persistence.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Decision implements Serializable {

	private static final long serialVersionUID = 8612505383687321937L;

	private int softMark;

	private int techMark;

	private int finalMark;

	private int scale;

	public Decision() {
	}

	public Decision(int scale, int finalMark, int techMark, int softMark) {
		this.scale = scale;
		this.finalMark = finalMark;
		this.techMark = techMark;
		this.softMark = softMark;
	}

	public int getSoftMark() {
		return softMark;
	}

	public void setSoftMark(int softMark) {
		this.softMark = softMark;
	}

	public int getTechMark() {
		return techMark;
	}

	public void setTechMark(int techMark) {
		this.techMark = techMark;
	}

	public int getFinalMark() {
		return finalMark;
	}

	public void setFinalMark(int finalMark) {
		this.finalMark = finalMark;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	@Override
	public String toString() {
		return "Decision{" +
				"softMark=" + softMark +
				", techMark=" + techMark +
				", finalMark=" + finalMark +
				", scale=" + scale +
				'}';
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(softMark).append(techMark).append(finalMark).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Decision other = (Decision) obj;
		return new EqualsBuilder().append(softMark, other.softMark).append(techMark, other.techMark)
				.append(finalMark, other.finalMark).isEquals();
	}

}
