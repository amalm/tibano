package tibano.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

public class PaymentInfo {
	static final int MINUTES_PER_HOUR = 60;
	static final int SECONDS_PER_MINUTE = 60;
	static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
	private final String amount;
	private final String duration;
	private final Integer loyaltyPoints;

	public PaymentInfo(Double amount, Duration duration, Double loyaltyPoints) {
		super();
		BigDecimal bd = new BigDecimal(amount);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		this.amount = "€" + Double.toString(bd.doubleValue()).toString();

		long seconds = duration.getSeconds();
		long hours = seconds / SECONDS_PER_HOUR;
		long minutes = ((seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);
		long secs = (seconds % SECONDS_PER_MINUTE);
		StringBuilder bld = new StringBuilder();
		if (hours > 0)
			bld.append(hours).append("h, ").append(minutes).append("min, ");
		else if (minutes > 0)
			bld.append(minutes).append("min, ");
		bld.append(secs).append("secs");
		this.duration = bld.toString();
		bd = new BigDecimal(loyaltyPoints);
		bd = bd.setScale(0, RoundingMode.HALF_UP);
		this.loyaltyPoints = Integer.valueOf(Double.valueOf(bd.doubleValue()).intValue());
	}

	public String getAmount() {
		return amount;
	}

	public String getDuration() {
		return duration;
	}

	public Integer getLoyaltyPoints() {
		return loyaltyPoints;
	}

}
