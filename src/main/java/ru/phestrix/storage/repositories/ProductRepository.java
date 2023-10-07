package ru.phestrix.storage.repositories;

import ru.phestrix.storage.databaseConnection.DatabaseConnection;
import ru.phestrix.storage.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRepository {
    private Connection connection = DatabaseConnection.getConnection();

    public void update(Product product) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "update product set name = ?, price = ? where id = ?"
            );
            statement.setString(1, product.getName());
            statement.setInt(2, product.getPrice());
            statement.setInt(3, product.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(Product product) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into product (name, price) VALUES (?,?)"
            );
            statement.setString(1, product.getName());
            statement.setInt(2, product.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product getProductById(Integer id) {
        Product product = new Product();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from product where id = ?"
            );
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            product.setId(result.getInt("id"));
            product.setName(result.getString("name"));
            product.setPrice(result.getInt("price"));
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public void delete(Product product) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "delete from product where id = ?"
            );
            statement.setInt(1, product.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer findProductIdByName(String goodName) {
        Integer id = -1;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select product.id from product where name = ?"
            );
            statement.setString(1, goodName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                id = resultSet.getInt("id");
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
