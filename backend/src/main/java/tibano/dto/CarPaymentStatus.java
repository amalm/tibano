package tibano.dto;

public class CarPaymentStatus {
    private String licensePlate;
    private boolean paying;

    public CarPaymentStatus(String licensePlate, boolean paying) {
        this.licensePlate = licensePlate;
        this.paying = paying;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public boolean isPaying() {
        return paying;
    }
}
