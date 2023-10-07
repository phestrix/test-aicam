package ru.phestrix.storage.repositories;

import ru.phestrix.storage.databaseConnection.DatabaseConnection;
import ru.phestrix.storage.entity.Good;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class GoodRepository {
    private Connection connection = DatabaseConnection.getConnection();

    public void update(Good good) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "update good set name = ?, price = ? where id = ?"
            );
            statement.setString(1, good.getName());
            statement.setInt(2, good.getPrice());
            statement.setInt(3, good.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(Good good) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into good (name, price) VALUES (?,?)"
            );
            statement.setString(1, good.getName());
            statement.setInt(2, good.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Good> getGoodById(Integer id) {
        Optional<Good> good = Optional.of(new Good());
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from good where id = ?"
            );
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            good.ifPresent(it -> {
                try {
                    it.setId(result.getInt("id"));
                    it.setName(result.getString("name"));
                    it.setPrice(result.getInt("price"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return good;
    }

    public void delete(Good good) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "delete from good where id = ?"
            );
            statement.setInt(1, good.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer findGoodIdByName(String goodName) {
        Integer id = -1;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select good.id from good where name = ?"
            );
            statement.setString(1, goodName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                id = resultSet.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
