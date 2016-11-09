package tibano.entity;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car, Long> {
	
	Car findByLicensePlate(String licensePlate);

	@Query("select c.licensePlate from Car c where c.user.id = ?1")
	Set<String> findLicensePlateByUserId(Long userId);
}
