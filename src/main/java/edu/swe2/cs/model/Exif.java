package edu.swe2.cs.model;

import java.util.Date;

public class Exif {

    private int id;
    private String camera;
    private String lens;
    private Date captureDate;

    public Exif(String camera, String lens, Date captureDate) {
        this.camera = camera;
        this.lens = lens;
        this.captureDate = captureDate;
    }

    public Exif(int id, String camera, String lens, Date captureDate) {
        this.id = id;
        this.camera = camera;
        this.lens = lens;
        this.captureDate = captureDate;
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

    public Date getCaptureDate() {
        return this.captureDate;
    }
}
