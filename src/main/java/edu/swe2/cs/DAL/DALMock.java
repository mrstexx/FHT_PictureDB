package edu.swe2.cs.DAL;

import edu.swe2.cs.Model.Photographer;
import edu.swe2.cs.Model.Picture;

import java.util.List;

public class DALMock implements IDAL {
    @Override
    public void initialize() {

    }

    @Override
    public List<Picture> getPictures() {
        return null;
    }

    @Override
    public List<Photographer> getPhotographers() {
        return null;
    }

    @Override
    public Picture getPicture() {
        return null;
    }

    @Override
    public Photographer getPhotographer() {
        return null;
    }
}
