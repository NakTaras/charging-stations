package taras.nakonechnyi.mkr.chargingstations.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "features")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "solar_panel_support", nullable = false)
    private Boolean solarPanelSupport;

    @Column(name = "wireless_charging", nullable = false)
    private Boolean wirelessCharging;
}

