package edu.swe2.cs.views;

import edu.swe2.cs.stage.EStage;
import edu.swe2.cs.stage.StageManager;
import edu.swe2.cs.viewmodel.IViewModel;
import edu.swe2.cs.viewmodel.ViewModelFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewManager {

    private static final Logger LOG = LogManager.getLogger(ViewManager.class);
    private static Map<String, IViewModel> viewManager = new HashMap<>();

    Stage primaryStage;
    ViewModelFactory viewModelFactory;

    public ViewManager(Stage primaryStage, ViewModelFactory viewModelFactory) {
        this.primaryStage = primaryStage;
        this.viewModelFactory = viewModelFactory;
        initViews();
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
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Picture Database");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../icon.png")));
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        StageManager.getInstance().addStage(EStage.PRIMARYSTAGE, primaryStage);
        primaryStage.show();
    }

    private void initViews() {
        viewManager.put(PictureView.class.getName(), viewModelFactory.getPictureViewModel());
        viewManager.put(PictureListView.class.getName(), viewModelFactory.getPictureListViewModel());
    }

    public static IViewModel getViewModel(String key) {
        IViewModel viewModel = viewManager.get(key);
        if (viewModel != null) {
            return viewModel;
        }
        LOG.error("Requesting view model that doesn't exist");
        return null;
    }

}
