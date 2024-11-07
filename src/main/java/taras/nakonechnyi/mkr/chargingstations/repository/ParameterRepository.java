package taras.nakonechnyi.mkr.chargingstations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taras.nakonechnyi.mkr.chargingstations.model.Parameter;

public interface ParameterRepository extends JpaRepository<Parameter, Integer> {
}
