package tibano.entity;

import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car, Long> {
	
	Car findByLicensePlate(String licensePlate);
}
