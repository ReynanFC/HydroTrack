package com.reynanfc.hydrotrack.ui.home.utils;

import java.text.NumberFormat;
import java.util.Locale;

public final class VolumeFormatter {

    private static final Locale PORTUGUESE_BRAZIL = Locale.forLanguageTag("pt-BR");

    private VolumeFormatter() {
    }

    public static String format(int ml) {
        return NumberFormat.getIntegerInstance(PORTUGUESE_BRAZIL).format(ml);
    }
}
