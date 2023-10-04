package ru.phestrix.storage.domain;


import lombok.*;
import ru.phestrix.exceptions.sql.CreateSQLException;
import ru.phestrix.exceptions.sql.DeleteSQLException;
import ru.phestrix.exceptions.sql.ReadSQLException;
import ru.phestrix.exceptions.sql.UpdateSQLException;

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

    public void update() throws UpdateSQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "update good set name = ?, price = ? where id = ?"
            );
            statement.setString(1, name);
            statement.setInt(2, price);
            statement.setInt(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new UpdateSQLException("failure with update");
        }
    }

    public void save() throws CreateSQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into good (name, price) VALUES (?,?)"
            );
            statement.setString(1, name);
            statement.setInt(2, price);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CreateSQLException("failure with creation");
        }
    }

    public Good getGoodById() throws ReadSQLException {
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
            throw new ReadSQLException("failure with reading");
        }
        return good;
    }

    public void delete() throws DeleteSQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "delete from good where id = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteSQLException("failure with deletion");
        }
    }
}
