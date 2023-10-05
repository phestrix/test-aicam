package ru.phestrix.storage.dao;

import ru.phestrix.exceptions.sql.CreateSQLException;
import ru.phestrix.storage.databaseConnection.DatabaseConnection;
import ru.phestrix.storage.domain.Customer;
import ru.phestrix.storage.domain.Purchase;

import java.sql.SQLException;

public class PurchaseDAO {
    public void save(Purchase purchase) {
        try {
            purchase.save();
        } catch (CreateSQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Purchase purchase) {
        try {
            purchase.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Purchase purchase) {
        try {
            purchase.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Purchase getById(Integer id) {
        Purchase purchase = null;
        try {
            purchase = new Purchase(DatabaseConnection.getConnection(), id, null, null, null).getPurchaseById();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchase;
    }
}