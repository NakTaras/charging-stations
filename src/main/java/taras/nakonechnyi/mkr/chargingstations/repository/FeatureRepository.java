package taras.nakonechnyi.mkr.chargingstations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taras.nakonechnyi.mkr.chargingstations.model.Feature;

public interface FeatureRepository extends JpaRepository<Feature, Integer> {
}
