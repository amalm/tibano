package tibano.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import tibano.dto.PaymentInfo;

@Entity
public class ParkingTransaction {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@ManyToOne
	@JoinColumn(name="AREA_ID", nullable=false)
	private Area area;
	
	@ManyToOne
	@JoinColumn(name="CAR_ID", nullable=false)
	private Car car;
	private final LocalDateTime start;
	private LocalDateTime end;
	private String duration;
	private String amount;
	private String loyaltyPoints;
	
	//Hibernate
	ParkingTransaction()
	{
		this(null, null);
	}

	public ParkingTransaction(Area area, Car car) {
		this.area = area;
		this.car = car;
		start = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public Area getArea() {
		return area;
	}

	public Car getCar() {
		return car;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public LocalDateTime getEnd() {
		return end;
	}
	

	public String getDuration() {
		return duration;
	}

	public String getAmount() {
		return amount;
	}

	public String getLoyaltyPoints() {
		return loyaltyPoints;
	}

	public void end(PaymentInfo paymentInfo) {
		end = LocalDateTime.now();
		duration = paymentInfo.getDuration();
		amount = paymentInfo.getAmount();
		loyaltyPoints = paymentInfo.getLoyaltyPoints().toString();
		
	}
	
}
