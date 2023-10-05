package ru.phestrix.storage.dao;

import ru.phestrix.exceptions.sql.CreateSQLException;
import ru.phestrix.storage.databaseConnection.DatabaseConnection;
import ru.phestrix.storage.domain.Customer;

import java.sql.SQLException;

public class CustomerDAO {
    public void save(Customer customer) {
        try {
            customer.save();
        } catch (CreateSQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Customer customer) {
        try {
            customer.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Customer customer) {
        try {
            customer.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer getById(Integer id) {
        Customer customer = null;
        try {
            customer = new Customer(DatabaseConnection.getConnection(), id, null, null).getCustomerById();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
}
