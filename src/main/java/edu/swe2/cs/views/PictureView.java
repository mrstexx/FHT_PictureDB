package edu.swe2.cs.views;

import edu.swe2.cs.stage.EStage;
import edu.swe2.cs.stage.StageManager;
import edu.swe2.cs.viewmodel.PictureViewModel;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class PictureView {
    public static final float IMAGE_OFFSET = 10;
    private static final Logger LOG = LogManager.getLogger(PictureView.class);
    private final PictureViewModel viewModel;

    @FXML
    HBox hBox;
    @FXML
    ImageView currentPicture;

    @FXML
    private void initialize() {
        currentPicture.imageProperty().bindBidirectional(viewModel.getCurrentPicture());
        Tooltip tooltip = new Tooltip("Double Click to assign Photographer");
        tooltip.setShowDelay(Duration.seconds(0.1));
        tooltip.setShowDuration(Duration.seconds(2));
        tooltip.setStyle("-fx-background-color:azure; -fx-text-fill:black;");
        Tooltip.install(hBox, tooltip);
        fitCurrentPicture();
    }

    @FXML
    private void onImageClick(MouseEvent event) {
        if(viewModel.isOnImageClick(event)) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AssignPictureView.fxml"));
                Parent parent = null;
                parent = fxmlLoader.load();
                Scene scene = new Scene(parent, 400, 170);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.setTitle("Assign Picture to Photographer");
                AssignPictureView assignPictureView = fxmlLoader.getController();
                assignPictureView.getViewModel().setPicture(viewModel.getPicture());
                StageManager.getInstance().addStage(EStage.ASSIGN_PICTURE_STAGE, stage);
                stage.showAndWait();
            } catch (IOException e) {
                LOG.error("Failed to load assignPicture View..", e);
            }
        }
    }

    @FXML
    private void onMouseEntered(MouseEvent event) {
        hBox.setStyle("-fx-background-color: azure");
        currentPicture.setEffect(new DropShadow(20, Color.BLACK));
    }

    @FXML
    private void onMouseExited(MouseEvent event) {
        hBox.setStyle("-fx-border-color: white");
        currentPicture.setEffect(null);
    }

    public PictureView() {
        this.viewModel = new PictureViewModel();
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
