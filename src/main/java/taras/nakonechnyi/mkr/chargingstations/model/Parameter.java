package taras.nakonechnyi.mkr.chargingstations.model;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "parameters")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "capacity_wh", nullable = false)
    private Integer capacityWh;

    @Column(name = "output_power_w", nullable = false)
    private Integer outputPowerW;

    @Column(name = "charging_time_hours", nullable = false)
    private Double chargingTimeHours;

    @Column(name = "weight_kg", nullable = false)
    private Double weightKg;

    @Column(name = "peak_power_w", nullable = false)
    private Integer peakPowerW;
}

