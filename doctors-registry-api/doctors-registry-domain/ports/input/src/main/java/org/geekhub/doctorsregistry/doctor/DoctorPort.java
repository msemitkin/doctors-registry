package org.geekhub.doctorsregistry.doctor;

import java.util.List;

public interface DoctorPort {

    Doctor getById(int id);

    List<Doctor> getAllByClinicId(int clinicId);

    void register(RegisterDoctorEntity doctor);

}
