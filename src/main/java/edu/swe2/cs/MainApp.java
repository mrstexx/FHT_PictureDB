package edu.swe2.cs;

import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.views.MainWindowView;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainApp extends Application {

    private static final Logger LOG = LogManager.getLogger(MainApp.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LOG.info("Starting application...");
        PictureBL.getInstance().sync();
        MainWindowView mainWindowView = new MainWindowView();
        mainWindowView.start(primaryStage);
    }
}
