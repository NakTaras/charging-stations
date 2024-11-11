package taras.nakonechnyi.mkr.chargingstations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taras.nakonechnyi.mkr.chargingstations.dto.ChargingStationRating;
import taras.nakonechnyi.mkr.chargingstations.dto.GetBestChargingStationDto;
import taras.nakonechnyi.mkr.chargingstations.dto.SaveChargingStationDto;
import taras.nakonechnyi.mkr.chargingstations.model.ChargingStation;
import taras.nakonechnyi.mkr.chargingstations.service.ChargingStationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/charging-stations")
@RequiredArgsConstructor
public class ChargingStationController {

    private final ChargingStationService chargingStationService;

    @GetMapping
    public List<ChargingStationRating> getAllChargingStations() {
        return chargingStationService.getAllChargingStations().entrySet().stream()
                .map(entry -> ChargingStationRating.builder()
                        .chargingStation(entry.getKey())
                        .rating(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/filter")
    public List<ChargingStation> getChargingStationsByClass(@RequestParam("className") String className) {
        return chargingStationService.getChargingStationsByClass(className);
    }

    @GetMapping("/best")
    public List<ChargingStation> getBestChargingStations(@RequestBody GetBestChargingStationDto dto) {
        return chargingStationService.getBestChargingStations(dto);
    }

    @PostMapping
    public ResponseEntity<ChargingStation> addChargingStation(@RequestBody SaveChargingStationDto dto) {
        var chargingStation = chargingStationService.addChargingStation(dto);
        return new ResponseEntity<>(chargingStation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ChargingStation updateChargingStation(@PathVariable("id") Integer id,
            @RequestBody SaveChargingStationDto dto) {
        return chargingStationService.updateChargingStation(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteChargingStation(@PathVariable("id") Integer id) {
        chargingStationService.deleteChargingStation(id);
    }
}
