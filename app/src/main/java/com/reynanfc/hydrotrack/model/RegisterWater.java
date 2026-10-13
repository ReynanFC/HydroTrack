package com.reynanfc.hydrotrack.model;

import java.time.LocalDateTime;

/** Um registro de consumo: volume informado e o instante em que foi feito. */
public record RegisterWater(int consumeMl, LocalDateTime time) {
}
