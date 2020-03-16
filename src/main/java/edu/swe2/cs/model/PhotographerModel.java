package edu.swe2.cs.model;

import edu.swe2.cs.bl.PhotographerBL;

public class PhotographerModel {

    public static void addPhotographerToModel(Photographer photographer) {
        PhotographerBL.savePhotographer(photographer);
    }


    public static void removePhotographerFromModel() {
    }
}
