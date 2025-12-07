package com.laptrinhweb.zerostarcafe.domain.admin.AdminDAO;

import com.laptrinhweb.zerostarcafe.domain.admin.dto.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

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

    public List<Product> getAllProductsByStore(long storeId) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT \n" +
                     "                        mi.id,\n" +
                     "                        mi.image_url,\n" +
                     "                        mi.name,\n" +
                     "                        mi.base_price,\n" +
                     "                        mi.unit,\n" +
                     "                        mi.is_active,\n" +
                     "                        COALESCE(smi.inventory, 0) AS inventory\n" +
                     "                    FROM menu_items mi\n" +
                     "                    LEFT JOIN store_menu_items smi\n" +
                     "                        ON mi.id = smi.menu_item_id\n" +
                     "                        AND smi.store_id = ?\n" +
                     "                    ORDER BY mi.id DESC";

        try (Connection conn = AdminDAO.connection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();

                    p.setId(rs.getInt("id"));
                    p.setPicUrl(rs.getString("image_url"));
                    p.setName(rs.getString("name"));
                    p.setPrice(rs.getInt("base_price"));
                    p.setUnit(rs.getString("unit"));
                    p.setInventory(rs.getDouble("inventory"));
                    p.setIs_active(rs.getBoolean("is_active"));

                    list.add(p);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) throws SQLException {
        AdminDAO dao = new AdminDAO();
        List<Product> list = dao.getAllProductsByStore(1);
        System.out.println(list.getFirst().getName());
    }
}
