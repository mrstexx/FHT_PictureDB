package edu.swe2.cs;

import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.dal.DALFactory;
import edu.swe2.cs.dal.DBManager;
import edu.swe2.cs.model.Picture;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class MainWindowViewModel extends Application {

    private static final Logger LOG = LogManager.getLogger(MainWindowViewModel.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LOG.info("Starting application...");
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Picture Database");
        primaryStage.getIcons().add(new Image(MainWindowViewModel.class.getResourceAsStream("icon.png")));
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.show();

        PictureBL.getInstance().sync();
    }
}
