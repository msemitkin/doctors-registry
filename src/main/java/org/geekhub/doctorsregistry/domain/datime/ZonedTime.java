package org.geekhub.doctorsregistry.domain.datime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class ZonedTime {

    private final String timeZone;

    public ZonedTime(@Value("${timezone}") String timeZone) {
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
