package org.geekhub.doctorsregistry.specialization;

public class Specialization {
    private final Integer id;
    private final String name;

    public Specialization(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
