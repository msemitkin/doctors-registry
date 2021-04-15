package org.geekhub.doctorsregistry.web.dto.doctor;

import java.util.List;

public interface CreateDoctorDTO {
    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    Integer getSpecializationId();

    void setSpecializationId(Integer specializationId);

    Integer getClinicId();

    void setClinicId(Integer clinicId);

    Integer getPrice();

    void setPrice(Integer price);

    List<String> getTimetable();

    void setTimetable(List<String> timetable);
}
