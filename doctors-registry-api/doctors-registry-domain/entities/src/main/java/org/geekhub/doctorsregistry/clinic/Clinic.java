package org.geekhub.doctorsregistry.clinic;

public class Clinic {

    private final Integer id;
    private final String name;
    private final String address;
    private final String email;

    public Clinic(
        Integer id,
        String name,
        String address,
        String email
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }
}
