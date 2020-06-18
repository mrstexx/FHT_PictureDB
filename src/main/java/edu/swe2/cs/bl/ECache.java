package edu.swe2.cs.bl;

public enum ECache {
    PICTURES("Picture"),
    PHOTOGRAPHERS("Photographer"),
    EXIFS("Exif"),
    IPTCS("Iptc");

    private final String className;

    ECache(String className) {
        this.className = className;
    }


    /**
     * Get classname
     *
     * @return Class name associated with enum
     */
    public String getClassName() {
        return className;
    }
}
