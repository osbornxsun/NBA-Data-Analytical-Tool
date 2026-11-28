package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewManagerModelListener;

import javax.swing.JPanel;
import java.awt.CardLayout;

/**
 * Listens to the ViewManagerModel and shows the matching card (view) in the CardLayout.
 * There is exactly one ViewManager for the whole app; it is created in AppBuilder.
 */
public class ViewManager implements ViewManagerModelListener {
    private final JPanel cardPanel;
    private final CardLayout cardLayout;
    private final ViewManagerModel viewManagerModel;

    public ViewManager(JPanel cardPanel, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addListener(this);
    }

    @Override
    public void viewChanged() {
        cardLayout.show(cardPanel, viewManagerModel.getCurrentViewName());
    }
}