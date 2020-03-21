package edu.swe2.cs;

import edu.swe2.cs.model.Photographer;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class PhotographerCellFactory implements Callback<ListView<Photographer>, ListCell<Photographer>> {

    @Override
    public ListCell<Photographer> call(ListView<Photographer> listView) {
        return new PhotographerCell();
    }
}
