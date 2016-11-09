package tibano.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ParkingTransaction {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@ManyToOne
	@JoinColumn(name="AREA_ID", nullable=false)
	private Area area;
	
	private final LocalDateTime start;
	private LocalDateTime end;
	
	//Hibernate
	ParkingTransaction()
	{
		this(null);
	}

	public ParkingTransaction(Area area) {
		this.area = area;
		start = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public Area getArea() {
		return area;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public LocalDateTime getEnd() {
		return end;
	}
	
}
