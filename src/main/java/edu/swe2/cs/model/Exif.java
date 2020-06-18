package edu.swe2.cs.model;

import java.util.Date;

public class Exif {

    private int id;
    private String camera;
    private String lens;
    private Date captureDate;

    /**
     * Constructs a new Exif with the specified camera, lens and capture date
     *
     */
    public Exif(String camera, String lens, Date captureDate) {
        this.camera = camera;
        this.lens = lens;
        this.captureDate = captureDate;
    }

    /**
     * Constructs a new Exif with the specified id, camera, lens and capture date
     *
     */
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

    public void setId(int id) {
        this.id = id;
    }
}
