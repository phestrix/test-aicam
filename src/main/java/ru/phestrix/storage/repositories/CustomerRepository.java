package ru.phestrix.storage.repositories;

import ru.phestrix.storage.databaseConnection.DatabaseConnection;
import ru.phestrix.storage.entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerRepository {
    private final Connection connection = DatabaseConnection.getConnection();

    public void update(Customer customer) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "update customer set name = ?, surname = ? where id = ?"
            );
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getSurname());
            statement.setInt(3, customer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(Customer customer) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into customer (name, surname) VALUES (?,?)"
            );
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getSurname());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Customer customer) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "delete from customer where id = ?"
            );
            statement.setInt(1, customer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Customer> findById(Integer id) {
        Optional<Customer> customer = Optional.of(new Customer());
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from customer where id = ?"
            );
            statement.setInt(1, id);
            createOptionalCustomerFromResultSet(customer, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public List<Customer> findBySurname(String surname) {
        List<Customer> customers = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from customer where surname = ?"
            );
            statement.setString(1, surname);

            try {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    customers.add(new Customer(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("surname")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    private void createOptionalCustomerFromResultSet(Optional<Customer> customer, PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        customer.ifPresent(it -> {
            try {
                it.setId(resultSet.getInt("id"));
                it.setName(resultSet.getString("name"));
                it.setSurname(resultSet.getString("surname"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        resultSet.close();
    }
}
