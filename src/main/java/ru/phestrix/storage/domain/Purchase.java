package ru.phestrix.storage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.phestrix.exceptions.sql.CreateSQLException;
import ru.phestrix.exceptions.sql.DeleteSQLException;
import ru.phestrix.exceptions.sql.ReadSQLException;
import ru.phestrix.exceptions.sql.UpdateSQLException;

import java.sql.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    Connection connection;

    private Integer id;
    private Integer customerId;
    private Integer goodId;
    private Date date;

    public void save() throws CreateSQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into purchases (customer_id, good_id, date)" + "values (?,?,?)"
            );
            statement.setLong(1, customerId);
            statement.setLong(2, goodId);
            statement.setDate(3, date);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CreateSQLException("failure with creation");
        }
    }

    public void update() throws UpdateSQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "update purchases set customer_id = ?, good_id = ?, date = ? where id = ?"
            );
            statement.setInt(1, customerId);
            statement.setInt(2, goodId);
            statement.setDate(3, new java.sql.Date(this.date.getTime()));
            statement.setInt(4, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new UpdateSQLException("failure with update");
        }
    }

    public void delete() throws DeleteSQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "delete from purchases where id = ?"
            );
            statement.setInt(1, this.id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteSQLException("failure with deletion");
        }
    }

    public Purchase getPurchaseById() throws ReadSQLException {
        Purchase purchase = null;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select * from purchases where id = ?"
            );
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                purchase = new Purchase();
                purchase.setId(result.getInt("id"));
                purchase.setCustomerId(result.getInt("customer_id"));
                purchase.setGoodId(result.getInt("good_id"));
                purchase.setDate(result.getDate("date"));
            }
        } catch (SQLException e) {
            throw new ReadSQLException("failure with reading");
        }
        return purchase;
    }
}

