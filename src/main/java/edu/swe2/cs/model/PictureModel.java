package edu.swe2.cs.model;

import edu.swe2.cs.bl.PictureBL;

import java.util.List;

public class PictureModel {

    private Picture picture;
    private List<Picture> pictures;

    public Picture getPicture() {
        if (picture == null) {
            this.pictures = this.getPictures();
            if (this.pictures.size() > 0) {
                this.picture = this.pictures.get(0);
            }
        }
        return picture;
    }

    public List<Picture> getPictures() {
        if (this.pictures == null) {
            this.pictures = PictureBL.getInstance().getAllPictures();
        }
        return this.pictures;
    }

}
