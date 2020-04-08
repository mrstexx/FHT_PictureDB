package edu.swe2.cs.views;

import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.eventbus.ISubscriber;
import edu.swe2.cs.model.Picture;
import edu.swe2.cs.viewmodel.PictureListViewModel;
import edu.swe2.cs.viewmodel.events.OnSearchPicturesEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PictureListView implements ISubscriber {

    PictureListViewModel viewModel;
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();

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
        eventBus.register(this);
    }

    @Override
    public void handle(IEvent<?> event) {
       hBox.getChildren().clear();
       hBox.getChildren().addAll(viewModel.onSearchPictures((List< Picture>) event.getData(), hBox.heightProperty()));
    }

    @Override
    public Set<Class<?>> supports() {
        Set<Class<?>> supportedEvents = new HashSet<>();
        supportedEvents.add(OnSearchPicturesEvent.class);
        return supportedEvents;
    }
}
