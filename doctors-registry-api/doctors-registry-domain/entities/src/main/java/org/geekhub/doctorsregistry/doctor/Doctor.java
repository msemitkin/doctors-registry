package org.geekhub.doctorsregistry.doctor;

public class Doctor {

    private final Integer id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final int specializationId;
    private final Integer clinicId;
    private final Integer price;

    public Doctor(
        Integer id,
        String firstName,
        String lastName,
        String email,
        int specializationId,
        Integer clinicId,
        Integer price
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.specializationId = specializationId;
        this.clinicId = clinicId;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getSpecializationId() {
        return specializationId;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public Integer getPrice() {
        return price;
    }
}
