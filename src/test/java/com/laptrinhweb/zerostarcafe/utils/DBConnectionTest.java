package com.laptrinhweb.zerostarcafe.utils;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.fail;

class DBConnectionTest {

    @Test
    void getConnection() {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Connection successful");
        } catch (SQLException e) {
            fail("Database connection failed: " + e.getMessage());
        }
    }
}