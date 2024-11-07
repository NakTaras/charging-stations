package taras.nakonechnyi.mkr.chargingstations.dto;

import lombok.Builder;
import lombok.Data;
import taras.nakonechnyi.mkr.chargingstations.model.ChargingStation;

import java.math.BigDecimal;

@Data
@Builder
public class SaveChargingStationDto {
    private String modelName;
    private String description;
    private BigDecimal price;
    private String purchaseLink;
    private String imageLink;
    private String manufacturerName;
    private String countryName;
    private Integer capacityWh;
    private Integer outputPowerW;
    private Double chargingTimeHours;
    private Double weightKg;
    private Integer peakPowerW;
    private Boolean solarPanelSupport;
    private Boolean wirelessCharging;
    private String className;
}
