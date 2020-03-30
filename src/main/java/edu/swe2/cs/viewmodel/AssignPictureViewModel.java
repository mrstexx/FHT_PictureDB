package edu.swe2.cs.viewmodel;

import edu.swe2.cs.model.Photographer;
import edu.swe2.cs.model.PhotographerModel;
import edu.swe2.cs.model.Picture;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class AssignPictureViewModel {

    private Picture picture;
    private Photographer photographer;
    private PhotographerModel photographerModel;
    private ObservableList<Photographer> photographers;
    private StringProperty currentPhotographerString = new SimpleStringProperty("None");

    public AssignPictureViewModel(){
        photographerModel = new PhotographerModel();
        photographers = FXCollections.observableList(photographerModel.getPhotographers());
    }

    public void setFirstPhotographer(){
        if(picture != null){
            Photographer currentPhotographer = picture.getPhotographer();
            if(currentPhotographer != null && photographers.contains(currentPhotographer)){
                currentPhotographerString.setValue("Currently assigned to this image: " + photographer.getId() + ", " + photographer.getLastName() + " " + photographer.getFirstName());
            }
        }
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
        setFirstPhotographer();
    }

    public Picture getPicture() {
        return picture;
    }

    public ObservableList<Photographer> getPhotographers(){
        return photographers;
    }

    public void onPhotographerSelect(Photographer newPhotographer) {
        this.photographer = newPhotographer;
    }

    public boolean isCurrentPhotographer(Photographer photographer){
        if(picture != null){
            Photographer currentPhotographer = picture.getPhotographer();
            if(currentPhotographer != null && currentPhotographer.getId() == photographer.getId())
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
}
