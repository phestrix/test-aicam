package ru.phestrix.storage.repositories;

import ru.phestrix.storage.databaseConnection.DatabaseConnection;
import ru.phestrix.storage.entity.Purchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PurchaseRepository {
    private final Connection connection = DatabaseConnection.getConnection();

    public void save(Purchase purchase) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into purchase (customer_id, good_id, date)" + "values (?,?,?)"
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
                    "update purchase set customer_id = ?, good_id = ?, date = ? where id = ?"
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

    public Optional<Purchase> getPurchaseById(Integer id) {
        Optional<Purchase> purchase = Optional.of(new Purchase());
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from purchase where id = ?"
            );
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            purchase.ifPresent(it -> {
                try {
                    it.setId(result.getInt("id"));
                    it.setCustomerId(result.getInt("customer_id"));
                    it.setGoodId(result.getInt("good_id"));
                    it.setDate(result.getDate("date"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchase;
    }
}
