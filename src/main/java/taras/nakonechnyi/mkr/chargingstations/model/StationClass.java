package taras.nakonechnyi.mkr.chargingstations.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "station_classes")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class StationClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "class_name", nullable = false)
    private String className;
}

