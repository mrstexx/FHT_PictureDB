package edu.swe2.cs.DAL;

import edu.swe2.cs.Model.Photographer;
import edu.swe2.cs.Model.Picture;

import java.util.List;

public interface IDAL {

    void initialize();

    List<Picture> getPictures();

    List<Photographer> getPhotographers();

    Picture getPicture();

    Photographer getPhotographer();
}
