package org.geekhub.doctorsregistry.clinic;

import java.util.List;

public interface ClinicPort {

    Clinic getById(int id);

    List<Clinic> getAll();

    void register();
}
