package org.johnm.vehicle.survey.analysis;

import org.johnm.vehicle.survey.Direction;

public class VehicleCountMapKey {
	private int year;
	private int month;
	private int day;
	private int periodInDay;
	private Direction vehicleDirection;
	
	public VehicleCountMapKey(final int yearParam, final int monthParam, final int dayParam, final int periodInDayParam,
			final Direction vehicleDirectionParam) {
		this.year = yearParam;
		this.month = monthParam;
		this.day = dayParam;
		this.periodInDay = periodInDayParam;
		this.vehicleDirection = vehicleDirectionParam;
	}
	
	public boolean isMaxVolumePeriodKey() {
		return year == 0 && month == 0 && day == 0 && vehicleDirection == null;
	}
	
	public boolean isTotalForPeriodKey() {
		 return year == 0 && month == 0 && day == 0 && vehicleDirection != null;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public int getPeriodInDay() {
		return periodInDay;
	}

	public Direction getVehicleDirection() {
		return vehicleDirection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		result = prime * result + month;
		result = prime * result + periodInDay;
		result = prime
				* result
				+ ((vehicleDirection == null) ? 0 : vehicleDirection.hashCode());
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final VehicleCountMapKey other = (VehicleCountMapKey) obj;
		if (day != other.day)
			return false;
		if (month != other.month)
			return false;
		if (periodInDay != other.periodInDay)
			return false;
		if (vehicleDirection != other.vehicleDirection)
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "year=" + year + ", month=" + month
				+ ", day=" + day + ", periodInDay=" + periodInDay
				+ ", vehicleDirection=" + vehicleDirection;
	}
	
}
