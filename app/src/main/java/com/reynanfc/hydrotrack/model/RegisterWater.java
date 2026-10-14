package com.reynanfc.hydrotrack.model;

import java.time.LocalDateTime;
import java.util.UUID;

/** Um registro de consumo: identificador, volume informado e o instante em que foi feito. */
public record RegisterWater(String id, int consumeMl, LocalDateTime time) {

    public RegisterWater(int consumeMl, LocalDateTime time) {
        this(UUID.randomUUID().toString(), consumeMl, time);
    }
}
