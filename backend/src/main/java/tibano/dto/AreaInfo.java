package tibano.dto;

public class AreaInfo {
	private final String name;
    private final Long capacity;
    private final Long occupied;
    private final Long runningPayments;
	private final Double longitude;
	private final Double latitude;
	private final Long id;

	public AreaInfo(Long id, String name, Long capacity, Long occupied, Long runningPayments, Double longitude, Double latitude) {
		super();
		this.id = id;
		this.name = name;
		this.capacity = capacity;
		this.occupied = occupied;
		this.runningPayments = runningPayments;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Long getId() {
		return id;
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

	public Double getLongitude() {
		return longitude;
	}

	public Double getLatitude() {
		return latitude;
	}
}
