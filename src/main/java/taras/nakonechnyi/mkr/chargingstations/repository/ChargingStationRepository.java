package taras.nakonechnyi.mkr.chargingstations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taras.nakonechnyi.mkr.chargingstations.model.ChargingStation;
import taras.nakonechnyi.mkr.chargingstations.model.StationClass;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChargingStationRepository extends JpaRepository<ChargingStation, Integer> {
    List<ChargingStation> findAll();
    List<ChargingStation> findAllByStationClass(StationClass stationClass);
    Optional<ChargingStation> findByModelName(String modelName);
}

