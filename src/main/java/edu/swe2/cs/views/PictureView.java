package edu.swe2.cs.views;

import edu.swe2.cs.viewmodel.PictureViewModel;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PictureView {

    public static final float IMAGE_OFFSET = 10;

    PictureViewModel viewModel = new PictureViewModel();

    @FXML
    HBox hBox;
    @FXML
    ImageView currentPicture;

    @FXML
    public void initialize() {
        fitCurrentPicture();
    }

    private void fitCurrentPicture() {
        currentPicture.setPreserveRatio(true);
        hBox.widthProperty().addListener(this::resizeWidthListener);
        hBox.heightProperty().addListener(this::resizeHeightListener);
    }

    private void resizeWidthListener(Observable observable, Number oldValue, Number newValue) {
        float width = calculateWidth(oldValue.floatValue(), newValue.floatValue());
        currentPicture.setFitWidth(width);
    }

    private void resizeHeightListener(Observable observable, Number oldValue, Number newValue) {
        float height = calculateHeight(oldValue.floatValue(), newValue.floatValue());
        currentPicture.setFitHeight(height);
    }

    private float calculateWidth(float oldWidth, float newWidth) {
        double imageWidth = currentPicture.getImage().getWidth();
        if (imageWidth < newWidth) {
            return (float) imageWidth;
        }
        return newWidth - IMAGE_OFFSET;
    }

    private float calculateHeight(float oldHeight, float newHeight) {
        double imageHeight = currentPicture.getImage().getHeight();
        if (imageHeight < newHeight) {
            return (float) imageHeight;
        }
        return newHeight - IMAGE_OFFSET;
    }
}
