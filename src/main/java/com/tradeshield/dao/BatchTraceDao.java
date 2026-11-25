package com.tradeshield.dao;

import com.tradeshield.db.Database;
import com.tradeshield.model.AffectedOrder;
import com.tradeshield.model.Batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BatchTraceDao {
    public Batch findBatch(int batchId) throws SQLException {
        String sql = "SELECT id,product_id,code,quantity,recalled FROM batches WHERE id=?";
        try (Connection connection = Database.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, batchId);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }
                return new Batch(
                        result.getInt("id"),
                        result.getInt("product_id"),
                        result.getString("code"),
                        result.getInt("quantity"),
                        result.getInt("recalled") == 1);
            }
        }
    }

    public List<AffectedOrder> affectedOrders(int batchId) throws SQLException {
        List<AffectedOrder> orders = new ArrayList<>();
        String sql = "SELECT DISTINCT o.id,u.name,u.email,i.quantity "
                + "FROM order_items i "
                + "JOIN orders o ON i.order_id=o.id "
                + "JOIN users u ON o.user_id=u.id "
                + "WHERE i.batch_id=? ORDER BY o.id";

        try (Connection connection = Database.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, batchId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    orders.add(new AffectedOrder(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getString("email"),
                            result.getInt("quantity")));
                }
            }
        }
        return orders;
    }
}
