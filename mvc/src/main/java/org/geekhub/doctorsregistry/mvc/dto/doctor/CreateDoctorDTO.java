package org.geekhub.doctorsregistry.mvc.dto.doctor;

import java.util.List;

public interface CreateDoctorDTO {
    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    Integer getSpecializationId();

    void setSpecializationId(Integer specializationId);

    Integer getPrice();

    void setPrice(Integer price);

    List<String> getTimetable();

    void setTimetable(List<String> timetable);

    String getEmail();

    void setEmail(String email);
}
