package org.geekhub.doctorsregistry.domain.datime;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class ZonedTime {

    private final String timeZone;

    public ZonedTime(String timeZone) {
        this.timeZone = timeZone;
    }

    public LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of(timeZone));
    }

}
