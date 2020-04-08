package edu.swe2.cs.model;

import edu.swe2.cs.bl.PhotographerBL;

import java.util.List;

//TODO: Delete class
public class PhotographerModel {

    private Photographer photographer;
    private List<Photographer> photographers;


    public void addPhotographerToModel(Photographer photographer) {
        PhotographerBL.savePhotographer(photographer);
    }


    public Photographer getPhotographer() {
        if (photographers == null) {
            getPhotographers();
            if (this.photographers != null) {
                photographer = photographers.get(0);
            }
        }
        return photographer;
    }

    public List<Photographer> getPhotographers() {
        if (this.photographers == null) {
            photographers = PhotographerBL.getAllPhotographers();
        }
        return photographers;
    }

    public void removePhotographerFromModel(Photographer photographer) {
        PhotographerBL.removePhotographer(photographer);
    }

    public void updatePhotographerModel(Photographer photographer) {
        PhotographerBL.updatePhotographer(photographer);
    }
}
