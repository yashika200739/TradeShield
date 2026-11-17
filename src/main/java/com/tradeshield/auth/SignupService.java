package com.tradeshield.auth;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

// Yashika[2510991179] - Signup
/** Registers users with BCrypt password hashes in the existing MySQL users table. */
public class SignupService {

    private static final String FIND_EMAIL = "SELECT id FROM users WHERE email = ? LIMIT 1";
    private static final String INSERT_USER =
            "INSERT INTO users (full_name, email, password_hash) VALUES (?, ?, ?)";

    /** Returns true when the account is created, or false when the email is already registered. */
    public boolean register(String fullName, String email, String password) throws SQLException {
        if (emailExists(email)) {
            return false;
        }

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_USER)) {
            statement.setString(1, fullName);
            statement.setString(2, email);
            statement.setString(3, passwordHash);
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException duplicateEmail) {
            // The unique email index also protects against two simultaneous signup attempts.
            return false;
        }
    }

    private boolean emailExists(String email) throws SQLException {
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_EMAIL)) {
            statement.setString(1, email);
            try (ResultSet results = statement.executeQuery()) {
                return results.next();
            }
        }
    }
}
