package edu.swe2.cs.views;

public abstract class AbstractView {

    private MainWindowView mainWindowView;

    public void init(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
    }

    public MainWindowView getMainWindowView() {
        return this.mainWindowView;
    }

}
