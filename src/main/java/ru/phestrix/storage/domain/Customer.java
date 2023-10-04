package ru.phestrix.storage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Customer {
    private Connection connection;
    private Integer id;
    private String name;
    private String surname;

    public void update() throws UpdateSQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "update customer set name = ?, surname = ? where id = ?"
            );
            statement.setString(1, name);
            statement.setString(2, surname);
            statement.setInt(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new UpdateSQLException("failure with update");
        }
    }

    public void save() throws CreateSQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into customer (name, surname) VALUES (?,?)"
            );
            statement.setString(1, name);
            statement.setString(2, surname);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CreateSQLException("failure with creation");
        }
    }

    public Customer getGoodById() throws ReadSQLException {
        Customer customer = null;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from customer where id = ?"
            );
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                customer = new Customer();
                customer.setId(result.getInt("id"));
                customer.setName(result.getString("name"));
                customer.setSurname(result.getString("surname"));

            }
        } catch (SQLException e) {
            throw new ReadSQLException("failure with reading");
        }
        return customer;
    }

    public void delete() throws DeleteSQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "delete from customer where id = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteSQLException("failure with deletion");
        }
    }
}
