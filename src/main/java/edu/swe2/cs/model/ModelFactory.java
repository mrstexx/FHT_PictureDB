package edu.swe2.cs.model;

public class ModelFactory {

    private Picture picture;

    public Picture getPicture() {
        if (picture == null) {
            picture = new Picture();
        }
        return picture;
    }

}
