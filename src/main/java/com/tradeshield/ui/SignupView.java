package com.tradeshield.ui;

import com.tradeshield.auth.SignupService;
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

// Yashika[2510991179] - Signup
/** Builds the Signup screen and validates details before creating an account. */
public class SignupView {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final SignupService signupService;
    private final Runnable onBackToLogin;
    private final Consumer<String> onSignupSuccess;
    private final TextField fullNameField = new TextField();
    private final TextField emailField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final PasswordField confirmPasswordField = new PasswordField();
    private final Label message = new Label();

    public SignupView(SignupService signupService, Runnable onBackToLogin,
                      Consumer<String> onSignupSuccess) {
        this.signupService = signupService;
        this.onBackToLogin = onBackToLogin;
        this.onSignupSuccess = onSignupSuccess;
    }

    public Parent create() {
        Label brand = new Label("TradeShield");
        brand.getStyleClass().add("login-brand");
        Label subtitle = new Label("ORDER SAFETY CONSOLE");
        subtitle.getStyleClass().add("brand-subtitle");
        Label title = new Label("Create your account");
        title.getStyleClass().add("login-title");
        Label description = new Label("Set up your account to access the secure trading workspace.");
        description.getStyleClass().add("page-description");

        fullNameField.setPromptText("Your full name");
        fullNameField.setAccessibleText("Full name");
        emailField.setPromptText("you@example.com");
        emailField.setAccessibleText("Email address");
        passwordField.setPromptText("Create a password");
        passwordField.setAccessibleText("Password");
        confirmPasswordField.setPromptText("Enter your password again");
        confirmPasswordField.setAccessibleText("Confirm password");

        Button createAccountButton = new Button("Create Account");
        createAccountButton.getStyleClass().add("primary-button");
        createAccountButton.setMaxWidth(Double.MAX_VALUE);
        createAccountButton.setOnAction(event -> attemptSignup());
        confirmPasswordField.setOnAction(event -> attemptSignup());

        message.getStyleClass().add("signup-message");
        message.setWrapText(true);

        Button backToLoginButton = new Button("Back to Login");
        backToLoginButton.getStyleClass().add("signup-link");
        backToLoginButton.setOnAction(event -> onBackToLogin.run());

        VBox card = new VBox(12, brand, subtitle, title, description,
                labeledField("FULL NAME", fullNameField),
                labeledField("EMAIL ADDRESS", emailField),
                labeledField("PASSWORD", passwordField),
                labeledField("CONFIRM PASSWORD", confirmPasswordField),
                message, createAccountButton, backToLoginButton);
        card.getStyleClass().add("login-card");
        card.setMaxWidth(480);
        card.setPadding(new Insets(32));

        VBox screen = new VBox(card);
        screen.getStyleClass().add("login-screen");
        screen.setAlignment(Pos.CENTER);
        screen.setPadding(new Insets(20));
        return screen;
    }

    private VBox labeledField(String title, TextField field) {
        Label label = new Label(title);
        label.getStyleClass().add("form-label");
        VBox wrapper = new VBox(6, label, field);
        wrapper.getStyleClass().add("form-field");
        return wrapper;
    }

    private void attemptSignup() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Complete all fields to create your account.");
            return;
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showError("Enter a valid email address.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Password and Confirm Password must match.");
            return;
        }

        try {
            if (!signupService.register(fullName, email, password)) {
                showError("An account with this email already exists.");
                return;
            }

            passwordField.clear();
            confirmPasswordField.clear();
            onSignupSuccess.accept("Account created successfully. You can now log in.");
        } catch (SQLException exception) {
            showError("Unable to create your account. Check the database settings and try again.");
        } finally {
            passwordField.clear();
            confirmPasswordField.clear();
        }
    }

    private void showError(String text) {
        message.setText(text);
        if (!message.getStyleClass().contains("signup-message-error")) {
            message.getStyleClass().add("signup-message-error");
        }
    }
}
