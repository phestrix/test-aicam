package ru.phestrix.storage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public void save() {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into purchases (customer_id, good_id, date)" + "values (?,?,?)"
            );
            statement.setLong(1, customerId);
            statement.setLong(2, goodId);
            statement.setDate(3, date);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update() {
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
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "delete from purchases where id = ?"
            );
            statement.setInt(1, this.id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Purchase loadById(Integer id) {
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
            e.printStackTrace();
        }
        return purchase;
    }
}

