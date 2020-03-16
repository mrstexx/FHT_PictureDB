package edu.swe2.cs.views;

import edu.swe2.cs.model.PhotographerModel;
import edu.swe2.cs.viewmodel.AddPhotographerViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
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

    @FXML
    Label labelLastName;

    public AddPhotographerView() {
    }

    @FXML
    public void initialize() {
        firstName.textProperty().bindBidirectional(addPhotographerViewModel.getFirstName());
        lastName.textProperty().bindBidirectional(addPhotographerViewModel.getLastName());
        birthDate.valueProperty().bindBidirectional(addPhotographerViewModel.getBirthDate());
        notes.textProperty().bindBidirectional(addPhotographerViewModel.getNotes());
        addListeners();
    }

    //TODO: Listeners work in ViewModel but not in View
    public void addListeners() {
        lastName.textProperty().addListener((observableValue, s, t1) -> {
            if (!addPhotographerViewModel.isLastName()) {
                labelMandatoryLastName();
            } else {
                unlabelMandatoryLastName();
            }
        });

        birthDate.valueProperty().addListener((observableValue, s, t1) -> {
            if (!addPhotographerViewModel.isValidDate(t1)) {
                labelInvalidBirthDate();
            } else {
                unlabelInvalidBirthDate();
            }
        });
    }

    //TODO: Close stage after adding
    public void savePhotographer(ActionEvent actionEvent) {
        if (addPhotographerViewModel.isValid()) {
            lockInputFields();
            PhotographerModel.addPhotographerToModel(addPhotographerViewModel.getPhotographer());
            LOG.info("Open new alert dialog - valid photographer form");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add new Photographer");
            alert.setHeaderText("Successfully added Photographer!");
            alert.show();
        } else {
            unlockInputFields();
            LOG.info("Open new alert dialog - invalid photographer form");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add new Photographer");
            alert.setHeaderText("Failed to add photgrapher..");
            alert.setContentText("Please check birthdate and verify that lastname is not empty");
            alert.show();
        }
    }

    public void lockInputFields() {
        firstName.setEditable(false);
        lastName.setEditable(false);
        birthDate.setEditable(false);
        notes.setEditable(false);
    }

    public void unlockInputFields() {
        firstName.setEditable(true);
        lastName.setEditable(true);
        birthDate.setEditable(true);
        notes.setEditable(true);
    }

    public void labelMandatoryLastName() {
        labelLastName.textProperty().setValue(labelLastName.textProperty().getValue() + " is mandatory");
        labelLastName.setTextFill(Color.web("#ff0000"));
        lastName.setStyle("-fx-border-color: red");
    }

    public void unlabelMandatoryLastName() {
        labelLastName.textProperty().setValue("Lastname");
        labelLastName.setTextFill(Color.web("#000000"));
        lastName.setStyle("-fx-border-color: green");
    }

    public void labelInvalidBirthDate() {
        labelBirthDate.setText("Warning: Birthdate has to be before today");
        labelBirthDate.setTextFill(Color.web("#ff0000"));
    }

    public void unlabelInvalidBirthDate() {
        labelBirthDate.setText("Birthdate");
        labelBirthDate.setTextFill(Color.web("#000000"));
    }

}
