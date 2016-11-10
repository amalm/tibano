package tibano.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Area {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	private String name;
	private Long capacity;
	private Long occupied;
	private Double longitude;
	private Double latitude;
	
	// For Hiberante only
	Area()
	{
		this(null, null);
	}

	public Area(String name, Long capacity) {
		this.name = name;
		this.capacity = capacity;
		longitude = 0.0;
		latitude = 0.0;
	}

	public Area(String name, Long capacity, Long occupied) {
		this(name, capacity);
		this.occupied = occupied;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public Long getCapacity() {
		return capacity;
	}
	public void setCapacity(Long capacity) {
		this.capacity = capacity;
	}
	public Long getOccupied() {
		return occupied;
	}
	public void setOccupied(Long occupied) {
		this.occupied = occupied;
	}

	public Double getLongitude() {
		return longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	@Override
	public String toString() {
		return "Area{" +
				"id=" + id +
				", name='" + name + '\'' +
				", capacity=" + capacity +
				", occupied=" + occupied +
				'}';
	}

	public void incrementOccupied() {
		this.occupied = this.occupied + 1;
	}

	public void decrementOccupied() {
		this.occupied = occupied - 1;
		if (occupied < 0)
			occupied = 0l;
	}
}
