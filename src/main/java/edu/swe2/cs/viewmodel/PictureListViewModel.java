package edu.swe2.cs.viewmodel;

import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.model.PictureModel;
import edu.swe2.cs.util.URLBuilder;
import edu.swe2.cs.viewmodel.events.OnPictureSelectEvent;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.List;

public class PictureListViewModel {

    private List<Picture> pictures;
    private PictureModel pictureModel;
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();

    public PictureListViewModel() {
        pictureModel = new PictureModel();
        this.pictures = pictureModel.getPictures();
    }

    public List<Node> getImageViews(ReadOnlyDoubleProperty heightProperty) {
        List<Node> nodeList = new ArrayList<>();
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
            HBox.setHgrow(imageView, Priority.ALWAYS);
            imageView.setImage(image);
            nodeList.add(imageView);
        }
        return nodeList;
    }

    private void onImageClick(MouseEvent mouseEvent) {
        Object sourceData = mouseEvent.getSource();
        eventBus.fire(new OnPictureSelectEvent((Picture) ((ImageView) sourceData).getUserData()));
    }
}
