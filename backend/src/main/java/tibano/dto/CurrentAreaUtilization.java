package tibano.dto;

public class CurrentAreaUtilization {
    private final Long capacity;
    private final Long occupied;

    public CurrentAreaUtilization(Long capacity, Long occupied) {
        this.capacity = capacity;
        this.occupied = occupied;
    }

    public Long getCapacity() {
        return capacity;
    }

    public Long getOccupied() {
        return occupied;
    }
}
