package edu.swe2.cs.viewmodel;

import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.util.URLBuilder;
import edu.swe2.cs.viewmodel.events.OnPictureSelectEvent;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class PictureListViewModel {

    private List<Picture> pictures;
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();

    public PictureListViewModel() {
        this.pictures = PictureBL.getInstance().getAllPictures();
    }

    /**
     * Create and return image list of image notes for all available pictures
     *
     * @param heightProperty Height to be used for creating images view nodes
     * @return List of prepared image nodes
     */
    public List<Node> getImageViews(ReadOnlyDoubleProperty heightProperty) {
        List<Node> nodeList = new ArrayList<>();
        if (pictures != null) {
            for (Picture pic : pictures) {
                Image image = new Image(URLBuilder.getPreparedImgPath(pic.getFileName()), true);
                ImageView imageView = new ImageView();
                imageView.prefWidth(180);
                imageView.prefHeight(180);
                imageView.setPreserveRatio(true);
                imageView.fitWidthProperty().bind(heightProperty);
                imageView.fitHeightProperty().bind(heightProperty);
                imageView.setCursor(Cursor.HAND);
                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onImageClick);
                imageView.setUserData(pic);
                imageView.setOnMouseEntered(this::onMouseEnter);
                imageView.setOnMouseExited(this::onMouseLeave);
                HBox.setHgrow(imageView, Priority.ALWAYS);
                imageView.setImage(image);
                nodeList.add(imageView);
            }
        }
        return nodeList;
    }

    private void onMouseEnter(MouseEvent event) {
        Object sourceData = event.getSource();
        ImageView imgView = (ImageView) sourceData;
        imgView.setEffect(new DropShadow(5, Color.BLACK));
    }

    private void onMouseLeave(MouseEvent event) {
        Object sourceData = event.getSource();
        ImageView imgView = (ImageView) sourceData;
        imgView.setEffect(null);
    }

    private void onImageClick(MouseEvent mouseEvent) {
        Object sourceData = mouseEvent.getSource();
        eventBus.fire(new OnPictureSelectEvent((Picture) ((ImageView) sourceData).getUserData()));
    }

    /**
     * @param pictures       List of pictures to be created when using search
     * @param heightProperty Height to be used for creating images view nodes
     * @return List of image view nodes
     */
    public List<Node> onSearchPictures(List<Picture> pictures, ReadOnlyDoubleProperty heightProperty) {
        this.pictures = pictures;
        List<Node> imageViews = getImageViews(heightProperty);
        if (pictures != null && !pictures.isEmpty()) {
            eventBus.fire(new OnPictureSelectEvent(pictures.get(0)));
        }
        return imageViews;
    }
}
