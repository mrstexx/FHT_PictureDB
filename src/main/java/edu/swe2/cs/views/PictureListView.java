package edu.swe2.cs.views;

import edu.swe2.cs.viewmodel.PictureListViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

public class PictureListView {

    PictureListViewModel viewModel;

    @FXML
    ScrollPane scrollPane;
    @FXML
    HBox hBox;

    @FXML
    private void initialize() {
        hBox.getChildren().addAll(viewModel.getImageViews(hBox.heightProperty()));
        hBox.setMinHeight(0);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
    }

    public PictureListView() {
        viewModel = new PictureListViewModel();
    }
}
