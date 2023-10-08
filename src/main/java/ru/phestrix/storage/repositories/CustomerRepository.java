package ru.phestrix.storage.repositories;

import ru.phestrix.storage.databaseConnection.DatabaseConnection;
import ru.phestrix.storage.entity.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public Customer findById(Integer id) {
        Customer customer = null;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from customer where id = ?"
            );
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                customer = new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname")
                );
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public List<Customer> findByLastName(String surname) {
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
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public Customer findByFullName(String fullName) {
        Customer customer = null;
        String lastName = fullName.substring(0, fullName.indexOf(" "));
        String name = fullName.substring(fullName.indexOf(" ") + 1);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from customer where customer.name = ? and customer.surname = ?"
            );
            statement.setString(1, name);
            statement.setString(2, lastName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                customer = new Customer(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname")
                );
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public ArrayList<Integer> findCustomersIdWithCostOfBuysInInterval(Integer minCost, Integer maxCost) {
        ArrayList<Integer> listOfCustomerIds = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select customer_id from customer" +
                            " inner join purchase p on customer.id = p.customer_id" +
                            " inner join product g on p.product_id = g.id" +
                            " group by customer_id" +
                            " having sum(g.price)>=? and sum(g.price)<=?"
            );
            statement.setInt(1, minCost);
            statement.setInt(2, maxCost);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                listOfCustomerIds.add(resultSet.getInt("customer_id"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfCustomerIds;
    }

    public ArrayList<Integer> findCustomerIdsWithMinQuantityOfPurchases(Integer countOfCustomers) {
        ArrayList<Integer> listOfCustomerIds = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select customer_id from customer" +
                            " inner join purchase on customer.id = purchase.customer_id" +
                            " group by customer_id" +
                            " order by count(purchase.id)" +
                            " limit ?"
            );
            statement.setInt(1, countOfCustomers);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                listOfCustomerIds.add(resultSet.getInt("customer_id"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfCustomerIds;
    }
}
