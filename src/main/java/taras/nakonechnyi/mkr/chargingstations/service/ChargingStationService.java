package taras.nakonechnyi.mkr.chargingstations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taras.nakonechnyi.mkr.chargingstations.dto.GetBestChargingStationDto;
import taras.nakonechnyi.mkr.chargingstations.dto.SaveChargingStationDto;
import taras.nakonechnyi.mkr.chargingstations.model.*;
import taras.nakonechnyi.mkr.chargingstations.repository.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChargingStationService {

    private final ChargingStationRepository chargingStationRepository;
    private final CountryRepository countryRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final StationClassRepository stationClassRepository;
    private final FeatureRepository featureRepository;
    private final ParameterRepository parameterRepository;

    private final Random rand = new Random();
    StationClassEnum[] stationClassEnums = StationClassEnum.values();

    public Map<ChargingStation, Double> getAllChargingStations() {
        AtomicReference<Double> rating = new AtomicReference<>(9.5);
        return chargingStationRepository.findAll().stream().collect(Collectors.toMap(
                key -> key,
                value -> rating.getAndSet(rating.get() - 1.1)
        ));
    }

    public List<ChargingStation> getChargingStationsByClass(String className) {
        var stationsClass = stationClassRepository.findByClassName(className)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        return chargingStationRepository.findAllByStationClass(stationsClass);
    }

    public List<ChargingStation> getBestChargingStations(GetBestChargingStationDto getBestChargingStationDto) {
        var stations = chargingStationRepository.findAll();

        Collections.shuffle(stations);

        return  stations.stream().limit(3).collect(Collectors.toList());
    }

    @Transactional
    public ChargingStation addChargingStation(SaveChargingStationDto dto) {
        var country = countryRepository.findByCountryName(dto.getCountryName())
                .orElseGet(() -> countryRepository.save(
                        Country.builder()
                                .countryName(dto.getCountryName())
                                .build()));

        var manufacturer = manufacturerRepository.findByManufacturerName(dto.getManufacturerName())
                .orElseGet(() -> manufacturerRepository.save(
                        Manufacturer.builder()
                                .manufacturerName(dto.getManufacturerName())
                                .country(country)
                                .build()));

        var parameter = parameterRepository.save(Parameter.builder()
                .capacityWh(dto.getCapacityWh())
                .outputPowerW(dto.getOutputPowerW())
                .chargingTimeHours(dto.getChargingTimeHours())
                .weightKg(dto.getWeightKg())
                .peakPowerW(dto.getPeakPowerW())
                .build());

        var feature = featureRepository.save(Feature.builder()
                .solarPanelSupport(dto.getSolarPanelSupport())
                .wirelessCharging(dto.getWirelessCharging())
                .build());

        var stationClass = stationClassRepository.findById(stationClassEnums[
                rand.nextInt(stationClassEnums.length)].getId())
                .orElseThrow(() -> new RuntimeException("Station Class not found"));

        var chargingStation = ChargingStation.builder()
                .modelName(dto.getModelName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .purchaseLink(dto.getPurchaseLink())
                .imageLink(dto.getImageLink())
                .manufacturer(manufacturer)
                .parameters(parameter)
                .features(feature)
                .stationClass(stationClass)
                .build();

        return chargingStationRepository.save(chargingStation);
    }

    @Transactional
    public ChargingStation updateChargingStation(Integer id, SaveChargingStationDto dto) {
        var chargingStation = chargingStationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Charging station not fount"));

        var country = countryRepository.findByCountryName(dto.getCountryName())
                .orElseGet(() -> countryRepository.save(
                        Country.builder()
                                .countryName(dto.getCountryName())
                                .build()));

        var manufacturer = manufacturerRepository.findByManufacturerName(dto.getManufacturerName())
                .orElseGet(() -> manufacturerRepository.save(
                        Manufacturer.builder()
                                .manufacturerName(dto.getManufacturerName())
                                .country(country)
                                .build()));

        var parameter = parameterRepository.save(Parameter.builder()
                .id(chargingStation.getParameters().getId())
                .capacityWh(dto.getCapacityWh())
                .outputPowerW(dto.getOutputPowerW())
                .chargingTimeHours(dto.getChargingTimeHours())
                .weightKg(dto.getWeightKg())
                .peakPowerW(dto.getPeakPowerW())
                .build());

        var feature = featureRepository.save(Feature.builder()
                .id(chargingStation.getFeatures().getId())
                .solarPanelSupport(dto.getSolarPanelSupport())
                .wirelessCharging(dto.getWirelessCharging())
                .build());

        var stationClass = stationClassRepository.findById(stationClassEnums[
                        rand.nextInt(stationClassEnums.length)].getId())
                .orElseThrow(() -> new RuntimeException("Station Class not found"));

        chargingStation.setModelName(dto.getModelName());
        chargingStation.setDescription(dto.getDescription());
        chargingStation.setPrice(dto.getPrice());
        chargingStation.setPurchaseLink(dto.getPurchaseLink());
        chargingStation.setImageLink(dto.getImageLink());
        chargingStation.setManufacturer(manufacturer);
        chargingStation.setParameters(parameter);
        chargingStation.setFeatures(feature);
        chargingStation.setStationClass(stationClass);


        return chargingStationRepository.save(chargingStation);
    }

    @Transactional
    public void deleteChargingStation(Integer id) {
        var chargingStation = chargingStationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Charging station not fount"));

        chargingStationRepository.deleteById(id);
        featureRepository.deleteById(chargingStation.getFeatures().getId());
        parameterRepository.deleteById(chargingStation.getParameters().getId());
    }
}
