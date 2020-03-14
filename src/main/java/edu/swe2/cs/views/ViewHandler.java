package edu.swe2.cs.views;

import edu.swe2.cs.model.Picture;
import edu.swe2.cs.viewmodel.ViewModelFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ViewHandler {

    private static final Logger LOG = LogManager.getLogger(ViewHandler.class);

    Stage primaryStage;
    ViewModelFactory viewModelFactory;

    public ViewHandler(Stage primaryStage, ViewModelFactory viewModelFactory) {
        this.primaryStage = primaryStage;
        this.viewModelFactory = viewModelFactory;
    }

    public void start() {
        Parent root;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainWindow.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initViews();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Picture Database");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../icon.png")));
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.show();
    }

    private void initViews() {
        try {
            initPictureView();
        } catch (IOException e) {
            LOG.error("Error occurred while initializing views", e);
        }
    }

    private void initPictureView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PictureView.fxml"));
        loader.load();
        PictureView pictureView = (PictureView) loader.getController();
        pictureView.init(viewModelFactory.getPictureViewModel());
    }

}
