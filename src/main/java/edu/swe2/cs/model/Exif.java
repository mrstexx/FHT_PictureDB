package edu.swe2.cs.model;

import java.util.Date;

public class Exif {

    private int id;
    private String camera;
    private String lens;
    private Date date;

    public Exif(int id, String camera, String lens, Date date) {
        this.id = id;
        this.camera = camera;
        this.lens = lens;
        this.date = date;
    }

    public int getId() {
        return this.id;
    }

    public String getCamera() {
        return this.camera;
    }

    public String getLens() {
        return this.lens;
    }

    public Date getDate() {
        return this.date;
    }
}
