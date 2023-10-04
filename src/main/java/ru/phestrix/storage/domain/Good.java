package ru.phestrix.storage.domain;


import lombok.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Good {
    private Connection connection;
    private Integer id;
    private String name;
    private Integer price;

    public void update() {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "update good set name = ?, price = ? where id = ?"
            );
            statement.setString(1, name);
            statement.setInt(2, price);
            statement.setInt(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into good (name, price) VALUES (?,?)"
            );
            statement.setString(1, name);
            statement.setInt(2, price);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Good getGoodById() {
        Good good = null;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from good where id = ?"
            );
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                good = new Good();
                good.setId(result.getInt("id"));
                good.setName(result.getString("name"));
                good.setPrice(result.getInt("price"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return good;
    }

    public void delete() {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "delete from good where id = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
