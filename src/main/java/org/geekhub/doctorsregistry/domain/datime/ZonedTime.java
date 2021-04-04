package org.geekhub.doctorsregistry.domain.datime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZonedTime {

    private final String timeZone;

    public ZonedTime(String timeZone) {
        this.timeZone = timeZone;
    }

    public LocalDateTime now() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(timeZone));
        return LocalDateTime.of(
            now.getYear(),
            now.getMonth(),
            now.getDayOfMonth(),
            now.getHour(),
            now.getMinute(),
            now.getSecond()
        );
    }

}
