package com.tradeshield.ui;

import com.tradeshield.auth.LoginService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/** Builds the Stage 3A login screen and reports the authenticated email on success. */
public class LoginView {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final LoginService loginService;
    private final Consumer<String> onLoginSuccess;
    private final Runnable onSignupRequested;
    private final String notice;
    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label message = new Label();

    public LoginView(LoginService loginService, Consumer<String> onLoginSuccess,
                     Runnable onSignupRequested, String notice) {
        this.loginService = loginService;
        this.onLoginSuccess = onLoginSuccess;
        this.onSignupRequested = onSignupRequested;
        this.notice = notice;
    }

    public Parent create() {
        Label brand = new Label("TradeShield");
        brand.getStyleClass().add("login-brand");

        Label subtitle = new Label("ORDER SAFETY CONSOLE");
        subtitle.getStyleClass().add("brand-subtitle");

        Label title = new Label("Welcome back");
        title.getStyleClass().add("login-title");
        Label description = new Label("Sign in to continue to your secure trading workspace.");
        description.getStyleClass().add("page-description");

        emailField.setPromptText("you@example.com");
        emailField.setAccessibleText("Email address");
        passwordField.setPromptText("Your password");
        passwordField.setAccessibleText("Password");

        Button loginButton = new Button("Log in");
        loginButton.getStyleClass().add("primary-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(event -> attemptLogin());
        passwordField.setOnAction(event -> attemptLogin());

        // Yashika[2510991179] - Signup
        Button signupButton = new Button("Create an account");
        signupButton.getStyleClass().add("signup-link");
        signupButton.setOnAction(event -> onSignupRequested.run());

        message.getStyleClass().add("login-message");
        message.setWrapText(true);
        if (notice != null && !notice.isBlank()) {
            message.setText(notice);
            message.getStyleClass().add("login-message-success");
        }

        VBox card = new VBox(14, brand, subtitle, title, description,
                labeledField("EMAIL ADDRESS", emailField),
                labeledField("PASSWORD", passwordField),
                message, loginButton, signupButton);
        card.getStyleClass().add("login-card");
        card.setMaxWidth(440);
        card.setPadding(new Insets(36));

        VBox screen = new VBox(card);
        screen.getStyleClass().add("login-screen");
        screen.setAlignment(Pos.CENTER);
        screen.setPadding(new Insets(24));
        return screen;
    }

    private VBox labeledField(String title, TextField field) {
        Label label = new Label(title);
        label.getStyleClass().add("form-label");
        VBox wrapper = new VBox(7, label, field);
        wrapper.getStyleClass().add("form-field");
        return wrapper;
    }

    private void attemptLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Enter your email address and password.");
            return;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showError("Enter a valid email address.");
            return;
        }

        try {
            if (loginService.authenticate(email, password)) {
                message.setText("");
                passwordField.clear();
                onLoginSuccess.accept(email);
            } else {
                showError("Email or password is incorrect.");
            }
        } catch (SQLException exception) {
            showError("Unable to connect to the login database. Check the database settings and try again.");
        } finally {
            passwordField.clear();
        }
    }

    private void showError(String text) {
        message.setText(text);
        message.getStyleClass().remove("login-message-success");
        if (!message.getStyleClass().contains("login-message-error")) {
            message.getStyleClass().add("login-message-error");
        }
    }
}
