package com.tradeshield;

import com.tradeshield.auth.LoginService;
import com.tradeshield.auth.SignupService;
import com.tradeshield.ui.DashboardView;
import com.tradeshield.ui.LoginView;
import com.tradeshield.ui.PlaceholderPageView;
import com.tradeshield.ui.SignupView;
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
        primaryStage.setTitle("TradeShield | Order Safety Console");
        primaryStage.setMinWidth(720);
        primaryStage.setMinHeight(600);
        showLogin(primaryStage, null);
        primaryStage.show();
    }

    // Yashika[2510991179] - Signup
    private void showLogin(Stage primaryStage, String notice) {
        LoginView loginView = new LoginView(new LoginService(),
                email -> showDashboard(primaryStage),
                () -> showSignup(primaryStage),
                notice);
        primaryStage.setScene(createStyledScene(loginView.create(), 960, 680));
        primaryStage.setMinWidth(720);
        primaryStage.setMinHeight(600);
    }

    private void showSignup(Stage primaryStage) {
        SignupView signupView = new SignupView(new SignupService(),
                () -> showLogin(primaryStage, null),
                successMessage -> showLogin(primaryStage, successMessage));
        primaryStage.setScene(createStyledScene(signupView.create(), 960, 760));
        primaryStage.setMinWidth(720);
        primaryStage.setMinHeight(700);
    }

    private void showDashboard(Stage primaryStage) {
        showPage(primaryStage, "Dashboard");
    }

    private void showPage(Stage primaryStage, String page) {
        javafx.scene.Parent root = switch (page) {
            case "Dashboard" -> new DashboardView(nextPage -> showPage(primaryStage, nextPage)).create();
            case "Simulated Orders", "Risk Controls", "Audit History" ->
                    new PlaceholderPageView(page, nextPage -> showPage(primaryStage, nextPage)).create();
            default -> throw new IllegalArgumentException("Unknown page: " + page);
        };
        primaryStage.setScene(createStyledScene(root, 1280, 800));
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
