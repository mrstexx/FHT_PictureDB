package edu.swe2.cs.views;

import edu.swe2.cs.viewmodel.PictureListViewModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PictureListView {

    PictureListViewModel viewModel;

    @FXML
    HBox hBox;

    @FXML
    private void initialize() {
        hBox.getChildren().addAll(viewModel.getImageViews(hBox.heightProperty()));
//        fitPictureSize();
    }

    public PictureListView() {
        this.viewModel = (PictureListViewModel) ViewManager.getViewModel(PictureListView.class.getName());
    }

    private void fitPictureSize() {
        for (Node node : hBox.getChildren()) {
            if (node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                imageView.setPreserveRatio(true);
                imageView.fitWidthProperty().bind(hBox.heightProperty());
                imageView.fitHeightProperty().bind(hBox.heightProperty());
            }
        }
    }
}
