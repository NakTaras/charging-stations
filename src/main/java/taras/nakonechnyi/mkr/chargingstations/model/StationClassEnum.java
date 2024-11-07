package taras.nakonechnyi.mkr.chargingstations.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StationClassEnum {
    HOME(1, "Home Use"),
    WORK(2, "Work Use"),
    CAMPING(3, "Camping");

    private final Integer id;
    private final String className;
}

