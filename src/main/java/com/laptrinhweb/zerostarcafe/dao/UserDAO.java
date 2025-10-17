package com.laptrinhweb.zerostarcafe.dao;

import com.laptrinhweb.zerostarcafe.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Register
    public boolean register(User user) {
        String sql = "INSERT INTO users (email, username, password_hash, is_super_admin) VALUES (?, ?, ?, FALSE)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Login
    public User login(String username, String password) {
        String sql = """
                    SELECT u.*, COALESCE(r.role_code, 'customer') AS role_code
                    FROM users u
                    LEFT JOIN user_store_roles r ON u.id = r.user_id
                    WHERE u.username = ? AND u.password_hash = ?
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getLong("id"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setSuperAdmin(rs.getBoolean("is_super_admin"));
                u.setRoleCode(rs.getString("role_code"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

