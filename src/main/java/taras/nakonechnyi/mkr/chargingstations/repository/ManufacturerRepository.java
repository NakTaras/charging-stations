package taras.nakonechnyi.mkr.chargingstations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taras.nakonechnyi.mkr.chargingstations.model.Manufacturer;

import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {
    Optional<Manufacturer> findByManufacturerName(String manufacturerName);
}
