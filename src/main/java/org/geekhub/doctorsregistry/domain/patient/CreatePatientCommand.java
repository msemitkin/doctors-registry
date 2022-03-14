package org.geekhub.doctorsregistry.domain.patient;

public record CreatePatientCommand(String email, String firstName, String lastName, String password) {
}
