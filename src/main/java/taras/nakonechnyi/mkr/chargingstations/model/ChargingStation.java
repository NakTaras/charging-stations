package taras.nakonechnyi.mkr.chargingstations.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "charging_stations")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ChargingStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parameters_id", nullable = false)
    private Parameter parameters;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "features_id", nullable = true) // nullable true для features
    private Feature features;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "station_class_id", nullable = false)
    private StationClass stationClass;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "purchase_link", nullable = false)
    private String purchaseLink;

    @Column(name = "image_link", nullable = false)
    private String imageLink;
}

