package taras.nakonechnyi.mkr.chargingstations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taras.nakonechnyi.mkr.chargingstations.model.StationClass;

import java.util.Optional;

public interface StationClassRepository extends JpaRepository<StationClass, Integer> {
    Optional<StationClass> findByClassName(String className);
}
