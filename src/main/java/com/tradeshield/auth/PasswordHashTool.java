package com.tradeshield.auth;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Console;
import java.util.Arrays;

/** Small command-line helper for generating a BCrypt hash for a pre-created test user. */
public final class PasswordHashTool {

    private PasswordHashTool() {
    }

    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            System.err.println("Run this helper from a terminal so the password can be entered without echo.");
            return;
        }

        char[] password = console.readPassword("Password to hash: ");
        if (password == null || password.length == 0) {
            System.err.println("Password cannot be empty.");
            return;
        }

        try {
            System.out.println(BCrypt.hashpw(new String(password), BCrypt.gensalt(12)));
        } finally {
            Arrays.fill(password, '\0');
        }
    }
}
