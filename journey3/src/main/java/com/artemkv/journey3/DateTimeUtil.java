package com.artemkv.journey3;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

final class DateTimeUtil {
    private DateTimeUtil() {}

    public static boolean isSameYear(Instant t1, Instant t2) {
        LocalDateTime ldt1 = LocalDateTime.ofInstant(t1, ZoneOffset.UTC);
        LocalDateTime ldt2 = LocalDateTime.ofInstant(t2, ZoneOffset.UTC);

        return ldt1.getYear() == ldt2.getYear();
    }

    public static boolean isSameMonth(Instant t1, Instant t2) {
        LocalDateTime ldt1 = LocalDateTime.ofInstant(t1, ZoneOffset.UTC);
        LocalDateTime ldt2 = LocalDateTime.ofInstant(t2, ZoneOffset.UTC);

        return ldt1.getYear() == ldt2.getYear() &&
                ldt1.getMonth() == ldt2.getMonth();
    }

    public static boolean isSameDay(Instant t1, Instant t2) {
        LocalDateTime ldt1 = LocalDateTime.ofInstant(t1, ZoneOffset.UTC);
        LocalDateTime ldt2 = LocalDateTime.ofInstant(t2, ZoneOffset.UTC);

        return ldt1.getYear() == ldt2.getYear() &&
                ldt1.getMonth() == ldt2.getMonth() &&
                ldt1.getDayOfMonth() == ldt2.getDayOfMonth();
    }

    public static boolean isSameHour(Instant t1, Instant t2) {
        LocalDateTime ldt1 = LocalDateTime.ofInstant(t1, ZoneOffset.UTC);
        LocalDateTime ldt2 = LocalDateTime.ofInstant(t2, ZoneOffset.UTC);

        return ldt1.getYear() == ldt2.getYear() &&
                ldt1.getMonth() == ldt2.getMonth() &&
                ldt1.getDayOfMonth() == ldt2.getDayOfMonth() &&
                ldt1.getHour() == ldt2.getHour();
    }
}
