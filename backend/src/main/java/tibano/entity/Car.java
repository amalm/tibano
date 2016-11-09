package tibano.entity;


import javax.persistence.*;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String licensePlate;

    @ManyToOne
    @JoinColumn(name="USER_ID", nullable=false)
    private User user;

    private Car() {
        // hibernate only
    }

    public Car(String licensePlate, User user) {
        this.licensePlate = licensePlate;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", licensePlate='" + licensePlate + '\'' +
                ", user=" + user +
                '}';
    }
}
