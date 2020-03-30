package edu.swe2.cs.model;

public class Iptc {

    private int id = -1;
    private String title;
    private String caption;
    private String city;

    public Iptc() {
    }

    public Iptc(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getCaption() {
        return this.caption;
    }

    public String getCity() {
        return this.city;
    }

    public void setID(int ID) {
        this.id = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
