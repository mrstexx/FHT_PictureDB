package edu.swe2.cs.model;

import java.util.Date;

public class Photographer {

    private int id;
    private String firstName;
    private String lastName;
    private Date birthdate;
    private String notes;

    public Photographer(int id, String lastName) {
        this.id = id;
        this.lastName = lastName;
    }

    public int getId() {
        return this.id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
}
