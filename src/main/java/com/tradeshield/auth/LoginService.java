package com.tradeshield.auth;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/** Yashika[2510991179] - Login Authentication */
/** Checks credentials against password hashes stored in the users table. */
public class LoginService {

    private static final String FIND_USER_HASH =
            "SELECT password_hash FROM users WHERE email = ? LIMIT 1";

    public boolean authenticate(String email, String password) throws SQLException {
        try (Connection connection = DatabaseConfig.openConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_HASH)) {
            statement.setString(1, email);

            try (ResultSet results = statement.executeQuery()) {
                if (!results.next()) {
                    return false;
                }

                String storedHash = results.getString("password_hash");
                if (storedHash == null || !storedHash.startsWith("$2")) {
                    return false;
                }

                try {
                    return BCrypt.checkpw(password, storedHash);
                } catch (IllegalArgumentException invalidHash) {
                    return false;
                }
            }
        }
    }
}
