package taras.nakonechnyi.mkr.chargingstations.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class GetBestChargingStationDto {
    private BigDecimal price;
    private Integer capacityWh;
    private Integer outputPowerW;
    private Double chargingTimeHours;
    private Double weightKg;
    private Integer peakPowerW;
    private Boolean solarPanelSupport;
    private Boolean wirelessCharging;
}
