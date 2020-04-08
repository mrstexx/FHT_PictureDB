package edu.swe2.cs.bl;

public enum ECache {
    PICTURES("Picture"),
    PHOTOGRAPHERS("Photographer"),
    EXIFS("Exif"),
    IPTCS("Iptc");

    private String className;
    ECache(String className){
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}