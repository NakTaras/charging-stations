package taras.nakonechnyi.mkr.chargingstations.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "manufacturers")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "manufacturer_name", nullable = false)
    private String manufacturerName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;
}

