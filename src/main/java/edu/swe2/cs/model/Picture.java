package edu.swe2.cs.model;

public class Picture {

    private int id;
    private String fileName;
    private Photographer photographer = null;
    private Exif exif = null; // exif data always gets created together with picture
    private Iptc iptc = null; // iptc data can get added afterwards
    private int photographer_id;
    private int exif_id;
    private int iptc_id;

    public Picture(int id, String fileName, Exif exif) {
        this.id = id;
        this.fileName = fileName;
        this.exif = exif;
    }

    public Picture() {
    }

    public int getId() {
        return this.id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Exif getExif() {
        return this.exif;
    }

    public Iptc getIptc() {
        return iptc;
    }

    public Photographer getPhotographer() {
        return photographer;
    }

    public void setIptc(Iptc iptc) {
        this.iptc = iptc;
    }

    public void setPhotographer(Photographer photographer) {
        this.photographer = photographer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setExif(Exif exif) {
        this.exif = exif;
    }

    public void setPhotographer_id(int photographer_id) {
        this.photographer_id = photographer_id;
    }

    public void setIptc_id(int iptc_id) {
        this.iptc_id = iptc_id;
    }

    public void setExif_id(int exif_id) {
        this.exif_id = exif_id;
    }

    public int getPhotographer_id() {
        return photographer_id;
    }

    public int getIptc_id() {
        return iptc_id;
    }

    public int getExif_id() {
        return exif_id;
    }
}
