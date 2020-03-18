package edu.swe2.cs.views;

import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.eventbus.ISubscriber;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.viewmodel.PictureViewModel;
import edu.swe2.cs.viewmodel.events.OnPictureSelectEvent;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.HashSet;
import java.util.Set;

public class PictureView implements IView, ISubscriber {
    public static final float IMAGE_OFFSET = 10;
    private final PictureViewModel viewModel;
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();

    @FXML
    HBox hBox;
    @FXML
    ImageView currentPicture;

    @FXML
    private void initialize() {
        currentPicture.imageProperty().bindBidirectional(viewModel.imageObjectProperty());
        fitCurrentPicture();
    }

    public PictureView() {
        this.viewModel = new PictureViewModel();
        eventBus.register(this);
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

    @Override
    public void handle(IEvent<?> event) {
        currentPicture.setImage(new Image((String) event.getData()));
    }

    @Override
    public Set<Class<?>> supports() {
        Set<Class<?>> supportedEvents = new HashSet<>();
        supportedEvents.add(OnPictureSelectEvent.class);
        return supportedEvents;
    }
}
