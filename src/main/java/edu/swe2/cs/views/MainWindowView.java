package edu.swe2.cs.views;

import edu.swe2.cs.eventbus.EventBusFactory;
import edu.swe2.cs.eventbus.IEvent;
import edu.swe2.cs.eventbus.IEventBus;
import edu.swe2.cs.eventbus.ISubscriber;
import edu.swe2.cs.model.Picture;
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

public class MainWindowView {

    private final static Logger LOG = LogManager.getLogger(MainWindowView.class);
    MainWindowViewModel viewModel;

    public MainWindowView() {
        viewModel = new MainWindowViewModel();
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

}
