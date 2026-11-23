package com.contoso.orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    public void init() throws SQLException {
        try (Connection c = DbConfig.open(); Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS orders ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                    + "customer VARCHAR(100) NOT NULL,"
                    + "item VARCHAR(100) NOT NULL,"
                    + "quantity INT NOT NULL,"
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        }
    }

    public void save(Order o) throws SQLException {
        String sql = "INSERT INTO orders (customer, item, quantity) VALUES (?, ?, ?)";
        try (Connection c = DbConfig.open(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, o.getCustomer());
            ps.setString(2, o.getItem());
            ps.setInt(3, o.getQuantity());
            ps.executeUpdate();
        }
    }

    public List<Order> findAll() throws SQLException {
        List<Order> result = new ArrayList<>();
        try (Connection c = DbConfig.open();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id, customer, item, quantity, created_at FROM orders ORDER BY id DESC")) {
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getLong("id"));
                o.setCustomer(rs.getString("customer"));
                o.setItem(rs.getString("item"));
                o.setQuantity(rs.getInt("quantity"));
                o.setCreatedAt(rs.getTimestamp("created_at"));
                result.add(o);
            }
        }
        return result;
    }
}
