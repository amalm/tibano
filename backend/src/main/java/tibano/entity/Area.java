package tibano.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.core.style.ToStringCreator;

@Entity
public class Area {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	private String name;
	private Long capacity;
	private Long occupied;
	
	// For Hiberante only
	Area()
	{
		this(null, null);
	}
	
	public Area(String name, Long capacity) {
		this.name = name;
		this.capacity = capacity;
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
	@Override
	public String toString() {
		
		return new ToStringCreator(this).append(name).append(capacity).append(occupied).toString();
	}
	
	
}
