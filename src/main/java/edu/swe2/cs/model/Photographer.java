package edu.swe2.cs.model;

import java.time.LocalDate;

public class Photographer {

    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String notes;

    public Photographer(String firstName, String lastName, LocalDate birthdate, String notes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.notes = notes;
    }

    public Photographer(int id, String lastName) {
        this.id = id;
        this.lastName = lastName;
    }

    public int getId() {
        return this.id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setBirthdate(LocalDate birthdate) {
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

    public LocalDate getBirthdate() {
        return this.birthdate;
    }

    public String getNotes() {
        return notes;
    }

    public void setId(int id) {
        this.id = id;
    }
}
