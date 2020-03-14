package edu.swe2.cs.model;

public class Picture {

    private int id;
    private String fileName;
    private Photographer photographer = null;
    private Exif exif = null; // exif data always gets created together with picture
    private Iptc iptc = null; // iptc data can get added afterwards

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

}
