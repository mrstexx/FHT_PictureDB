package edu.swe2.cs.viewmodel;

import edu.swe2.cs.config.ConfigProperties;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.util.SystemProperties;
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
        // TODO Stefan: Add later picture.getFileName(). Also extract this since we need it also in list view.
        // Maybe move somewhere else this part of code
        return "file:" +
                SystemProperties.FILE_SEPARATOR +
                URLBuilder.buildURLString(new String[]{"src", "main", "resources", ConfigProperties.getProperty("folderName")}) +
                SystemProperties.FILE_SEPARATOR + "img-1.jpg";
    }
}
