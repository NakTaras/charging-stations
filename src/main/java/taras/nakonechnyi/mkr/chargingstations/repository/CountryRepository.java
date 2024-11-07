package taras.nakonechnyi.mkr.chargingstations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taras.nakonechnyi.mkr.chargingstations.model.Country;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer> {
    Optional<Country> findByCountryName(String countryName);
}
