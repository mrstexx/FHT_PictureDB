package edu.swe2.cs.views;

import edu.swe2.cs.bl.PictureBL;
import edu.swe2.cs.reporting.*;
import edu.swe2.cs.stage.EStage;
import edu.swe2.cs.stage.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class MenuBarView {

    private static final Logger LOG = LogManager.getLogger(MenuBarView.class);

    public void exit() {
        LOG.info("Closing application...");
        System.exit(0);
    }

    public void showAboutUs() {
        LOG.info("Open new alert dialog - show about us");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Us");
        alert.setHeaderText("Project Information");
        alert.setContentText(getAboutUsContent());
        alert.show();
    }

    private String getAboutUsContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("FH Technikum Wien\n");
        sb.append("Software Engineering 2 Final Project SS2020\n");
        sb.append("Authors: Leo Gruber & Stefan Miljevic");
        return sb.toString();
    }

    public void showHowToArticle() {
        try {
            Desktop desktop = Desktop.getDesktop();
            if (!Desktop.isDesktopSupported() || desktop == null || !desktop.isSupported(Desktop.Action.OPEN)) {
                LOG.warn("Desktop is not supported. Cannot open how-to document.");
                return;
            }
            URI file = new URL("file://" + getClass().getResource("howto.html").getPath()).toURI();
            desktop.browse(file);
        } catch (IOException | URISyntaxException e) {
            LOG.error("An error occurred when open how-to page", e);
        }
    }


    public void addNewPhotographer() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddPhotographerView.fxml"));
            Parent parent = null;
            parent = fxmlLoader.load();
            Scene scene = new Scene(parent, 350, 400);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Add new Photographer");
            StageManager.getInstance().addStage(EStage.ADD_PHOTOGRAPHER_STAGE, stage);
            stage.showAndWait();
        } catch (IOException e) {
            LOG.error("Failed to load addNewPhotographer View..", e);
        }

    }

    public void exportToPDFReport() {
        String saveDirectory = getTargetPath();
        AbstractReportHandler reportHandler = new ImageReportHandler(saveDirectory);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Picture Report");
        if (saveDirectory.isEmpty()) {
            return;
        }
        if (reportHandler.createPdfReport(new ImageReport(PictureBL.getInstance().getCurrentPicture()))) {
            alert.setHeaderText("Picture has been successfully exported");
        } else {
            alert.setHeaderText("An error occurred while generating a picture report");
            alert.setContentText("Please contact your administrator!");
        }
        alert.show();
    }

    public void exportTagsToPDF() {
        String saveDirectory = getTargetPath();
        AbstractReportHandler reportHandler = new TagReportHandler(saveDirectory);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tag Report");
        if (saveDirectory.isEmpty()) {
            return;
        }
        if (reportHandler.createPdfReport(new TagReport(PictureBL.getInstance().getAllPictures()))) {
            alert.setHeaderText("Tags have been successfully exported");
        } else {
            alert.setHeaderText("An error occurred while generating a tag report");
            alert.setContentText("Please contact your administrator!");
        }
        alert.show();
    }

    private String getTargetPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(StageManager.getInstance().getStage(EStage.PRIMARY_STAGE));
        if (file != null) {
            return file.getAbsolutePath();
        }
        return "";
    }

    //TODO: maybe style
    public void editPhotographer() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditPhotographerView.fxml"));
            Parent parent = null;
            parent = fxmlLoader.load();
            Scene scene = new Scene(parent, 600, 400);
            scene.getStylesheets().add(getClass().getResource("../styles/ListTestTheme.css").toString());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Edit Photographer");
            StageManager.getInstance().addStage(EStage.EDIT_PHOTOGRAPHER_STAGE, stage);
            stage.showAndWait();
        } catch (IOException e) {
            LOG.error("Failed to load editPhotographer View..", e);
        }
    }

    public void sync(ActionEvent actionEvent) {
        int newImages = PictureBL.getInstance().sync();
        LOG.info("Open new alert dialog - show number of new Images");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Image Synchronization");
        alert.setHeaderText(newImages + " Images have been added to the database");
        alert.show();
    }
}
