package edu.swe2.cs.views;

import edu.swe2.cs.bl.PhotographerBL;
import edu.swe2.cs.stage.EStage;
import edu.swe2.cs.stage.StageManager;
import edu.swe2.cs.viewmodel.PhotographerViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddPhotographerView {

    private final static Logger LOG = LogManager.getLogger(AddPhotographerView.class);
    private PhotographerViewModel photographerViewModel = new PhotographerViewModel();

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
        firstName.textProperty().bindBidirectional(photographerViewModel.getFirstName());
        lastName.textProperty().bindBidirectional(photographerViewModel.getLastName());
        birthDate.valueProperty().bindBidirectional(photographerViewModel.getBirthDate());
        notes.textProperty().bindBidirectional(photographerViewModel.getNotes());
        birthDate.getEditor().setDisable(true);
        addListeners();
    }

    public void addListeners() {
        lastName.textProperty().addListener((observableValue, s, t1) -> {
            if (!photographerViewModel.isLastName()) {
                labelMandatoryLastName();
            } else {
                unlabelMandatoryLastName();
            }
        });

        birthDate.valueProperty().addListener((observableValue, s, t1) -> {
            if (!photographerViewModel.isValidDate(t1)) {
                labelInvalidBirthDate();
            } else {
                unlabelInvalidBirthDate();
            }
        });
    }

    public void savePhotographer(ActionEvent actionEvent) {
        if (photographerViewModel.isValid()) {
            lockInputFields();
            PhotographerBL.savePhotographer(photographerViewModel.getNewPhotographer());
            LOG.info("Open new alert dialog - valid photographer form");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add new Photographer");
            alert.setHeaderText("Successfully added Photographer!");
            alert.show();
            StageManager.getInstance().closeStage(EStage.ADDPHOTOGRAPHERSTAGE);
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
