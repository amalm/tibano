package tibano.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ParkingTransactionRepository extends CrudRepository<ParkingTransaction, Long> {
	@Query("select pt from ParkingTransaction pt where pt.area.id = ?1 and pt.car.licensePlate = ?2 and pt.end is null")
	ParkingTransaction findOpenTransactionByAreaAndLicensePlate(Long areaId, String licensePlate);

	@Query("select count(pt) from ParkingTransaction pt where pt.area.id = ?1 and pt.end is null")
	Long getOpenTransactionByAreaCount(Long areaId);
}
