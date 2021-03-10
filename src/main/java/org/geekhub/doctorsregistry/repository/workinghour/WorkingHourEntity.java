package org.geekhub.doctorsregistry.repository.workinghour;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "working_hour")
public class WorkingHourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalTime time;
    private String dayOfTheWeek;

    protected WorkingHourEntity() {
    }

    public WorkingHourEntity(Integer id, LocalTime time, String dayOfTheWeek) {
        this.id = id;
        this.time = time;
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfWeek) {
        this.dayOfTheWeek = dayOfWeek;
    }
}
