package edu.swe2.cs.views;

import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.eventbus.ISubscriber;
import edu.swe2.cs.stage.EStage;
import edu.swe2.cs.stage.StageManager;
import edu.swe2.cs.viewmodel.MainWindowViewModel;
import edu.swe2.cs.viewmodel.events.OnPictureDoubleClickEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MainWindowView implements ISubscriber {

    private final static Logger LOG = LogManager.getLogger(MainWindowView.class);
    MainWindowViewModel viewModel;
    private IEventBus eventBus = EventBusFactory.createSharedEventBus();

    public MainWindowView() {
        viewModel = new MainWindowViewModel();
        eventBus.register(this);
    }

    @FXML
    public void initialize() {
        viewModel.setFirstPicture();
    }

    public void start(Stage primaryStage) {
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainWindow.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Picture Database");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../icon.png")));
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        StageManager.getInstance().addStage(EStage.PRIMARYSTAGE, primaryStage);
        primaryStage.show();
    }

    //TODO: Fix exception
    @Override
    public void handle(IEvent<?> event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AssignPictureView.fxml"));
            Parent parent = null;
            parent = fxmlLoader.load();
            Scene scene = new Scene(parent, 600, 400);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Assign Picture to Photographer");
            StageManager.getInstance().addStage(EStage.ASSIGNPICTURESTAGE, stage);
            stage.showAndWait();
        } catch (IOException e) {
            LOG.error("Failed to load assignPicture View..", e);
        }
    }

    @Override
    public Set<Class<?>> supports() {
        Set<Class<?>> supportedEvents = new HashSet<>();
        supportedEvents.add(OnPictureDoubleClickEvent.class);
        return supportedEvents;
    }
}
