package com.tradeshield;

import com.tradeshield.ui.DashboardView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Starts the TradeShield desktop application.
 *
 * <p>JavaFX calls {@link #start(Stage)} after it has prepared the UI toolkit.</p>
 */
public class TradeShieldApp extends Application {

    /**
     * Creates the first application window and displays the Stage 1 dashboard.
     *
     * @param primaryStage the main window supplied by JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        DashboardView dashboardView = new DashboardView();
        Scene scene = new Scene(dashboardView.create(), 1280, 800);

        scene.getStylesheets().add(
                getClass().getResource("/com/tradeshield/styles/dashboard.css").toExternalForm()
        );

        primaryStage.setTitle("TradeShield | Order Safety Console");
        primaryStage.setMinWidth(1080);
        primaryStage.setMinHeight(700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** Starts JavaFX when this class is launched by Maven. */
    public static void main(String[] args) {
        launch(args);
    }
}
