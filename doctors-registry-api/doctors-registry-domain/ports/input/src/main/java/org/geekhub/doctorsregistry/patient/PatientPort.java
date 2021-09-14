package org.geekhub.doctorsregistry.patient;

public interface PatientPort {

    Patient getById(int id);

    void register(RegisterPatientEntity patient);

}
