package com.reynanfc.hydrotrack.ui.home.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class HomeDateFormatter {

    private final static String PATTERN_DATE_PT_BR = "'Hoje', d 'de' MMMM";
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private HomeDateFormatter() {
    }

    public static String formatToday() {
        LocalDate today = LocalDate.now();
        Locale portugueseBrazil = Locale.forLanguageTag("pt-BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                PATTERN_DATE_PT_BR,
                portugueseBrazil
        );

        return today.format(formatter);
    }
}
