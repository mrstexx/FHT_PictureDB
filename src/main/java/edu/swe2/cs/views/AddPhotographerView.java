package edu.swe2.cs.views;

import edu.swe2.cs.viewmodel.AddPhotographerViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddPhotographerView {

    private final static Logger LOG = LogManager.getLogger(AddPhotographerView.class);
    private AddPhotographerViewModel addPhotographerViewModel = new AddPhotographerViewModel();

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private DatePicker birthDate;

    @FXML
    private TextArea notes;

    @FXML
    private Button save;

    @FXML
    private Label labelBirthDate;

    public AddPhotographerView() {
    }

    @FXML
    public void initialize() {
        firstName.textProperty().bindBidirectional(addPhotographerViewModel.getFirstName());
        lastName.textProperty().bindBidirectional(addPhotographerViewModel.getLastName());
        birthDate.valueProperty().bindBidirectional(addPhotographerViewModel.getBirthDate());
        notes.textProperty().bindBidirectional(addPhotographerViewModel.getNotes());
        labelBirthDate.textProperty().bindBidirectional(addPhotographerViewModel.getLabelBirthDate());
    }

    public void savePhotographer(ActionEvent actionEvent) {
        if (addPhotographerViewModel.isValid()) {
            //TODO: Add photographer
        } else {
            LOG.info("Open new alert dialog - invalid photographer form");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add new Photographer");
            alert.setHeaderText("Failed to add photgrapher..");
            alert.setContentText("Please check birthdate and verify that lastname is not empty");
            alert.show();
        }
    }
}
