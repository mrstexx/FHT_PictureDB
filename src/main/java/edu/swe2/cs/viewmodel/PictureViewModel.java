package edu.swe2.cs.viewmodel;

import edu.swe2.cs.model.Picture;
import edu.swe2.cs.util.URLBuilder;

public class PictureViewModel {

    private Picture picture;
    private EXIFViewModel exifViewModel;
    private IPTCViewModel iptcViewModel;

    public PictureViewModel() {
        exifViewModel = new EXIFViewModel();
        iptcViewModel = new IPTCViewModel();
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String getPicturePath() {
        return URLBuilder.getPreparedImgPath("img-3.jpg");
    }
}
