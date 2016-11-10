package tibano.dto;

public class AreaInfo {
	private final String name;
    private final Long capacity;
    private final Long occupied;
    private final Long runningPayments;
	private final Double longitute;
	private final Double latitude;

	public AreaInfo(String name, Long capacity, Long occupied, Long runningPayments, Double longitude, Double latitude) {
		super();
		this.name = name;
		this.capacity = capacity;
		this.occupied = occupied;
		this.runningPayments = runningPayments;
		this.longitute = longitude;
		this.latitude = latitude;
	}
	public String getName() {
		return name;
	}
	public Long getCapacity() {
		return capacity;
	}
	public Long getOccupied() {
		return occupied;
	}
	public Long getRunningPayments() {
		return runningPayments;
	}

	public Double getLongitute() {
		return longitute;
	}

	public Double getLatitude() {
		return latitude;
	}
}
