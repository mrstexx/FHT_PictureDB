package edu.swe2.cs.views;

import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
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

    public void addNewPicture() {

    }

    public void addNewPhotographer() {

    }

    public void exportToPDFReport() {

    }

    public void assignPictures() {

    }

    public void editPhotograph() {

    }
}
