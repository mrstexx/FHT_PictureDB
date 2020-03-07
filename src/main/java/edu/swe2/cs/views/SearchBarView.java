package edu.swe2.cs.views;

import edu.swe2.cs.viewmodel.SearchBarViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SearchBarView {

    private SearchBarViewModel viewModel = new SearchBarViewModel();

    @FXML
    public TextField searchTextField;
    @FXML
    public Button searchButton;

    public void onSearch() {
        // call method from view model here
    }
}
