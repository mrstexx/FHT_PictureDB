package edu.swe2.cs.viewmodel;

import edu.swe2.cs.model.ModelFactory;

public class ViewModelFactory {

    private PictureViewModel pictureViewModel;

    public ViewModelFactory(ModelFactory modelFactory) {
        pictureViewModel = new PictureViewModel(modelFactory.getPicture());
    }

    public PictureViewModel getPictureViewModel() {
        return pictureViewModel;
    }

}
