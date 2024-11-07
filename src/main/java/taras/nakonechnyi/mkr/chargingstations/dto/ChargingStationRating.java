package taras.nakonechnyi.mkr.chargingstations.dto;

import lombok.Builder;
import lombok.Data;
import taras.nakonechnyi.mkr.chargingstations.model.ChargingStation;

@Data
@Builder
public class ChargingStationRating {
    private final ChargingStation chargingStation;
    private final Double rating;
}
