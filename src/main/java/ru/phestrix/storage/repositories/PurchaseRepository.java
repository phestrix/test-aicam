package ru.phestrix.storage.repositories;

import ru.phestrix.storage.databaseConnection.DatabaseConnection;
import ru.phestrix.storage.entity.Purchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class PurchaseRepository {
    private final Connection connection = DatabaseConnection.getConnection();

    public void save(Purchase purchase) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into purchase (customer_id, product_id, date)" + "values (?,?,?)"
            );
            statement.setLong(1, purchase.getCustomerId());
            statement.setLong(2, purchase.getGoodId());
            statement.setDate(3, purchase.getDate());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Purchase purchase) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "update purchase set customer_id = ?, product_id = ?, date = ? where id = ?"
            );
            statement.setInt(1, purchase.getCustomerId());
            statement.setInt(2, purchase.getGoodId());
            statement.setDate(3, purchase.getDate());
            statement.setInt(4, purchase.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Purchase purchase) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "delete from purchase where id = ?"
            );
            statement.setInt(1, purchase.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Purchase getPurchaseById(Integer id) {
        Purchase purchase = new Purchase();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from purchase where id = ?"
            );
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                purchase.setId(result.getInt("id"));
                purchase.setCustomerId(result.getInt("customer_id"));
                purchase.setGoodId(result.getInt("good_id"));
                purchase.setDate(result.getDate("date"));

            }
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchase;
    }

    public ArrayList<Integer> findCustomersIdWhoHasGoodIdCountTimes(Integer goodId, Integer count) {
        ArrayList<Integer> customerIdArray = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select name, surname from customer where " +
                            "id in (select customer_id from (select count(customer_id) as count, " +
                            "customer_id from purchase where product_id " +
                            "= (select id from product where id = ?) " +
                            "group by customer_id) as sub where count > ?)"
            );
            statement.setInt(1, goodId);
            statement.setInt(2, count);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customerIdArray.add(resultSet.getInt("id"));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerIdArray;
    }
}
