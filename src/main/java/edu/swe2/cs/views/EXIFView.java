package edu.swe2.cs.views;

import edu.swe2.cs.viewmodel.EXIFViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EXIFView {

    private EXIFViewModel viewModel;

    @FXML
    TextField cameraTextField;
    @FXML
    TextField lensTextField;
    @FXML
    TextField dateField;

    @FXML
    public void initialize() {
        cameraTextField.textProperty().bindBidirectional(viewModel.cameraProperty());
        lensTextField.textProperty().bindBidirectional(viewModel.lensProperty());
        dateField.textProperty().bindBidirectional(viewModel.dateProperty());
    }

    public EXIFView() {
        this.viewModel = new EXIFViewModel();
    }
}
