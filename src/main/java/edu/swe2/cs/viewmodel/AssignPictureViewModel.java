package edu.swe2.cs.viewmodel;

import edu.swe2.cs.bl.PhotographerBL;
import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.Picture;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AssignPictureViewModel {

    private Picture picture;
    private Photographer photographer;
    private Photographer oldPhotographer;
    private ObservableList<Photographer> photographers;
    private StringProperty currentPhotographerString = new SimpleStringProperty("None");

    public AssignPictureViewModel() {
        photographers = FXCollections.observableList(PhotographerBL.getAllPhotographers());
    }

    public void setFirstPhotographer() {
        if (picture != null) {
            Photographer currentPhotographer = PictureBL.getInstance().getPhotographerToPicture(picture);
            if (currentPhotographer != null && photographersContainsPhotographer(currentPhotographer)) {
                currentPhotographerString.setValue("Currently assigned to this image: " + currentPhotographer.getId() + ", " + currentPhotographer.getLastName() + " " + currentPhotographer.getFirstName());
                oldPhotographer = currentPhotographer;
            }
        }
    }

    public void setPicture(Picture picture) {
        this.picture =  PictureBL.getInstance().getPicture(picture);
        setFirstPhotographer();
    }

    public Picture getPicture() {
        return picture;
    }

    private boolean photographersContainsPhotographer(Photographer photographer) {
        assert photographers != null;
        for (Photographer elem : photographers) {
            if (elem.getId() == photographer.getId()) {
                return true;
            }
        }
        return false;
    }

    public ObservableList<Photographer> getPhotographers() {
        return photographers;
    }

    public void onPhotographerSelect(Photographer newPhotographer) {
        this.photographer = newPhotographer;
    }

    public boolean isCurrentPhotographer(Photographer photographer) {
        if (picture != null) {
            Photographer currentPhotographer = PictureBL.getInstance().getPhotographerToPicture(picture);
            if (currentPhotographer != null && currentPhotographer.getId() == photographer.getId())
                return true;
        }
        return false;
    }

    public StringProperty currentPhotographerProperty() {
        return currentPhotographerString;
    }

    public Photographer getPhotographer() {
        return photographer;
    }

    public Photographer getOldPhotographer() {
        return oldPhotographer;
    }
}
