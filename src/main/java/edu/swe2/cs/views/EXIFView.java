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
        cameraTextField.textProperty().bindBidirectional(viewModel.getCamera());
        lensTextField.textProperty().bindBidirectional(viewModel.getLens());
        dateField.textProperty().bindBidirectional(viewModel.getDate());
    }

    public EXIFView() {
        this.viewModel = new EXIFViewModel();
    }
}
