package com.tradeshield;

import com.tradeshield.auth.LoginService;
import com.tradeshield.ui.DashboardView;
import com.tradeshield.ui.LoginView;
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
     * Creates the first application window and displays the Stage 3A login screen.
     *
     * @param primaryStage the main window supplied by JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        LoginView loginView = new LoginView(new LoginService(), email -> showDashboard(primaryStage));
        Scene scene = createStyledScene(loginView.create(), 960, 680);
        primaryStage.setTitle("TradeShield | Order Safety Console");
        primaryStage.setMinWidth(720);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showDashboard(Stage primaryStage) {
        DashboardView dashboardView = new DashboardView();
        primaryStage.setScene(createStyledScene(dashboardView.create(), 1280, 800));
        primaryStage.setMinWidth(1080);
        primaryStage.setMinHeight(700);
    }

    private Scene createStyledScene(javafx.scene.Parent root, double width, double height) {
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(
                getClass().getResource("/com/tradeshield/styles/dashboard.css").toExternalForm()
        );
        return scene;
    }

    /** Starts JavaFX when this class is launched by Maven. */
    public static void main(String[] args) {
        launch(args);
    }
}
