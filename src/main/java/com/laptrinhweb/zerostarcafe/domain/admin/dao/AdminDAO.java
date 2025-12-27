package com.laptrinhweb.zerostarcafe.domain.admin.dao;

import com.laptrinhweb.zerostarcafe.domain.admin.dto.Category;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    //Connection
    public static Connection connection() throws ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306/zerostar_cf?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String username = "root";
        String password = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connected!");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Get all products by store
    public List<Product> getAllProductsByStore(int storeId) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT mi.*, smi.inventory, c.name as cat_name " +
                     "FROM menu_items mi " +
                     "JOIN store_menu_items smi ON mi.id = smi.menu_item_id " +
                     "LEFT JOIN categories c ON mi.category_id = c.id " +
                     "WHERE smi.store_id = ? " +
                     "ORDER BY mi.id ASC";
        try (Connection conn = AdminDAO.connection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();

                    p.setId(rs.getInt("id"));
                    p.setPicUrl(rs.getString("image_url"));
                    p.setName(rs.getString("name"));
                    p.setPrice(rs.getInt("base_price"));
                    p.setUnit(rs.getString("unit"));
                    p.setInventory(rs.getDouble("inventory"));
                    p.setActive(rs.getBoolean("is_active"));
                    p.setCategoryId(rs.getInt("category_id"));
                    p.setCategoryName(rs.getString("cat_name"));
                    list.add(p);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    //Update product
    public boolean updateProduct(Product p, long storeId) throws SQLException {
        boolean rowUpdated = false;

        String sqlMenuItem = "UPDATE menu_items SET name = ?, base_price = ?, unit = ?, image_url = ?, is_active = ? WHERE id = ?";
        String sqlStoreItem = "INSERT INTO store_menu_items (store_id, menu_item_id, inventory) VALUES (?, ?, ?) " +
                              "ON DUPLICATE KEY UPDATE inventory = ?";

        try (Connection conn = AdminDAO.connection()) {
            if (conn == null) return false;

            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlMenuItem);
                 PreparedStatement ps2 = conn.prepareStatement(sqlStoreItem)) {

                ps1.setString(1, p.getName());
                ps1.setDouble(2, p.getPrice());
                ps1.setString(3, p.getUnit());
                ps1.setString(4, p.getPicUrl());
                ps1.setBoolean(5, p.isActive());
                ps1.setInt(6, p.getId());

                int result1 = ps1.executeUpdate();

                ps2.setLong(1, storeId);
                ps2.setInt(2, p.getId());
                ps2.setDouble(3, p.getInventory());
                ps2.setDouble(4, p.getInventory());

                int result2 = ps2.executeUpdate();
                conn.commit();
                rowUpdated = result1 > 0;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rowUpdated;
    }

    //Update product hide status
    public boolean updateProductHideStatus(int id, boolean isActive) {
        String sql = "UPDATE menu_items SET is_active = ? WHERE id = ?";

        try (Connection conn = AdminDAO.connection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, isActive);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Delete product
    public boolean deleteProduct(int id, int storeID) throws SQLException {
        String sql = "DELETE FROM store_menu_items WHERE menu_item_id = ? AND store_id = ?";
        try (Connection conn = AdminDAO.connection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, storeID);

            int rowsAffected = ps.executeUpdate();

            System.out.println("DEBUG DELETE: ID=" + id + ", Store=" + storeID);
            System.out.println("DEBUG DELETE: Số dòng bị xóa = " + rowsAffected);

            return rowsAffected > 0;
        } catch (Exception e) {
            System.err.println("DEBUG DELETE ERROR: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean createProduct(Product p) throws SQLException {
        Connection conn = null;
        PreparedStatement psMenu = null;
        PreparedStatement psStore = null;
        ResultSet rs = null;

        String sqlInsertMenu = "INSERT INTO menu_items (category_id, name, image_url, base_price, unit, is_active) VALUES (?, ?, ?, ?, ?, ?)";

        String sqlInsertStore = "INSERT INTO store_menu_items (store_id, menu_item_id, inventory, in_menu, availability_status) VALUES (?, ?, ?, TRUE, 'available')";

        try {
            conn = AdminDAO.connection();

            conn.setAutoCommit(false);
            psMenu = conn.prepareStatement(sqlInsertMenu, java.sql.Statement.RETURN_GENERATED_KEYS);

            psMenu.setInt(1, p.getCategoryId());
            psMenu.setString(2, p.getName());
            psMenu.setString(3, p.getPicUrl());
            psMenu.setDouble(4, p.getPrice());
            psMenu.setString(5, p.getUnit());
            psMenu.setBoolean(6, p.isActive());

            int affectedRows = psMenu.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Thêm sản phẩm thất bại, không có dòng nào được thêm vào menu_items.");
            }

            long newProductId = 0;
            rs = psMenu.getGeneratedKeys();
            if (rs.next()) {
                newProductId = rs.getLong(1);
            } else {
                throw new SQLException("Thêm sản phẩm thất bại, không lấy được ID.");
            }

            psStore = conn.prepareStatement(sqlInsertStore);
            psStore.setInt(1, 1);
            psStore.setLong(2, newProductId);
            psStore.setDouble(3, p.getInventory());

            psStore.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    System.err.println("Gặp lỗi, đang rollback: " + e.getMessage());
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (rs != null) rs.close();
            if (psMenu != null) psMenu.close();
            if (psStore != null) psStore.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<Category> getAllCategories() throws SQLException, ClassNotFoundException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT id, name FROM categories ORDER BY order_index ASC";
        try (Connection conn = AdminDAO.connection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
        }
        return list;
    }

    public static void main(String[] args) throws SQLException {
        AdminDAO dao = new AdminDAO();
        List<Product> list = dao.getAllProductsByStore(1);
        System.out.println(list.getFirst().getName());
    }
}
