package edu.swe2.cs.viewmodel;

import edu.swe2.cs.model.ModelFactory;

public class ViewModelFactory {

    private PictureViewModel pictureViewModel;
    private PictureListViewModel pictureListViewModel;

    public ViewModelFactory(ModelFactory modelFactory) {
        pictureViewModel = new PictureViewModel(modelFactory.getPicture());
        pictureListViewModel = new PictureListViewModel(modelFactory.getPictures());
    }

    public PictureViewModel getPictureViewModel() {
        return pictureViewModel;
    }

    public PictureListViewModel getPictureListViewModel() {
        return pictureListViewModel;
    }

}
