package com.tradeshield.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

/** Builds an empty feature page while its implementation is pending. */
public class PlaceholderPageView {

    private final String title;
    private final Consumer<String> pageNavigator;

    public PlaceholderPageView(String title, Consumer<String> pageNavigator) {
        this.title = title;
        this.pageNavigator = pageNavigator;
    }

    public BorderPane create() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-shell");
        root.setLeft(createSidebar());

        Label pageTitle = new Label(title);
        pageTitle.getStyleClass().add("page-title");
        Label message = new Label("No work assigned yet.");
        message.getStyleClass().add("page-description");
        VBox workspace = new VBox(12, pageTitle, message);
        workspace.getStyleClass().add("workspace");
        workspace.setPadding(new Insets(36));
        root.setCenter(workspace);
        return root;
    }

    private VBox createSidebar() {
        Label brand = new Label("TradeShield");
        brand.getStyleClass().add("brand");
        Label subtitle = new Label("ORDER SAFETY CONSOLE");
        subtitle.getStyleClass().add("brand-subtitle");

        VBox navigation = new VBox(8,
                navigationButton("Dashboard"),
                navigationButton("Simulated Orders"),
                navigationButton("Risk Controls"),
                navigationButton("Audit History")
        );
        navigation.getStyleClass().add("navigation");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Label environment = new Label("SIMULATION ENVIRONMENT");
        environment.getStyleClass().add("environment-label");

        VBox sidebar = new VBox(8, brand, subtitle, navigation, spacer, environment);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPadding(new Insets(32, 22, 28, 22));
        return sidebar;
    }

    private Button navigationButton(String page) {
        Button button = new Button(page);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add(page.equals(title)
                ? "navigation-button-active" : "navigation-button");
        button.setOnAction(event -> pageNavigator.accept(page));
        return button;
    }
}
