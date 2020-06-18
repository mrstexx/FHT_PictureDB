package edu.swe2.cs.reporting;

import edu.swe2.cs.model.Picture;

public class ImageReport implements IReport {

    private Picture picture;

    public ImageReport(Picture picture) {
        this.picture = picture;
    }

    /**
     * @return Picture of report
     */
    public Picture getPicture() {
        return picture;
    }

    @Override
    public String getFileName() {
        return picture.getFileName();
    }

}
