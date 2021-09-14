package org.geekhub.doctorsregistry.domain.schedule;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DayTimeSpliterator {

    public List<DayTime> splitToDayTime(List<String> dayTimeStrings) {
        List<DayTime> dayTimes = new ArrayList<>();
        String spliterator = "&";
        for (String dayTimeString : dayTimeStrings) {
            String[] splitedEntries = dayTimeString.split(spliterator);
            if (splitedEntries.length != 2) {
                throw new IllegalArgumentException("Failed to parse day and time: " + dayTimeString);
            }
            DayOfWeek day = DayOfWeek.valueOf(splitedEntries[0]);
            LocalTime time = LocalTime.parse(splitedEntries[1]);
            dayTimes.add(new DayTime(day, time));
        }
        return dayTimes;
    }

}
