package com.tradeshield.service;

import com.tradeshield.dao.BatchTraceDao;
import com.tradeshield.model.AffectedOrder;
import com.tradeshield.model.Batch;
import com.tradeshield.util.TablePrinter;

import java.sql.SQLException;
import java.util.List;

public class BatchTraceService {
    private final BatchTraceDao dao = new BatchTraceDao();

    public void showAffected(int batchId) throws SQLException {
        Batch batch = dao.findBatch(batchId);
        if (batch == null) {
            System.out.println("Batch not found.");
            return;
        }

        System.out.println("Batch: " + batch.getCode()
                + " | Remaining warehouse stock: " + batch.getQuantity());
        List<AffectedOrder> orders = dao.affectedOrders(batchId);
        if (orders.isEmpty()) {
            System.out.println("No affected customer orders.");
        } else {
            TablePrinter.show(orders);
        }
    }
}
