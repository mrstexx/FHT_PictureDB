package edu.swe2.cs.viewmodel;

import edu.swe2.cs.model.Picture;
import edu.swe2.cs.util.URLBuilder;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

public class PictureViewModel {

    private Picture picture;
    private ObjectProperty<Image> imageObjectProperty;

    public PictureViewModel(Picture picture) {
        this.picture = picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public ObjectProperty<Image> imageObjectProperty() {
        if (imageObjectProperty == null) {
            Image img = new Image(getPicturePath());
            imageObjectProperty = new SimpleObjectProperty<>(img);
        }
        return imageObjectProperty;
    }

    public String getPicturePath() {
        return URLBuilder.getPreparedImgPath("img-3.jpg");
    }
}
