package com.vkevvinn.couchcast.backend;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class PasswordWrapper {

    public static boolean isValid(String password, String salt, String hashedPassword) {
        return hashedPassword.equals(hashPassword(password, salt));
    }

    public static String hashPassword(String password, String salt) {
        return Hashing.sha256()
                .hashString(password + salt, StandardCharsets.UTF_8)
                .toString();
    }
}
