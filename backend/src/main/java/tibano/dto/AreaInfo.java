package tibano.dto;

public class AreaInfo {
	private final String name;
    private final Long capacity;
    private final Long occupied;
    private final Long runningPayments;
	public AreaInfo(String name, Long capacity, Long occupied, Long runningPayments) {
		super();
		this.name = name;
		this.capacity = capacity;
		this.occupied = occupied;
		this.runningPayments = runningPayments;
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
    
}
