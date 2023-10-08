package ru.phestrix.storage.repositories;

import ru.phestrix.dto.StatDto;
import ru.phestrix.storage.databaseConnection.DatabaseConnection;
import ru.phestrix.storage.entity.Purchase;

import java.sql.*;
import java.util.ArrayList;

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

    public Integer findTotalExpenses() {
        Integer total = 0;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select sum(p.price) from customer c " +
                            "join purchase pu on c.id = pu.customer_id " +
                            "join product p on pu.product_id = p.id "
            );

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            total = resultSet.getInt("sum");
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public Double findAverageExpenses() {
        Double avg = 0.0;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select c.name, c.surname, AVG(p.price) as average_expenses\n" +
                            "from customer c " +
                            "join purchase pu ON c.id = pu.customer_id " +
                            "join product p ON pu.product_id = p.id " +
                            "group by c.id"
            );

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            avg = resultSet.getDouble("average_expenses");
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avg;
    }

    public ArrayList<StatDto> getStatistic(Date startDate, Date endDate) {
        ArrayList<StatDto> statDtos = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "select c.surname || ' ' || c.name as fullName, p.name as productName, " +
                            "count(sub.product_id) * p.price as expenses from " +
                            "(select * from purchase where date between ? and ? " +
                            "and customer_id in (select id from customer) order by product_id) as sub " +
                            "join product p on sub.product_id = p.id join customer c on sub.customer_id = c.id " +
                            "group by p.name, p.price, sub.customer_id, c.name, c.surname, sub.product_id " +
                            "order by expenses desc;"

            );
            statement.setDate(1, startDate);
            statement.setDate(2, endDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                statDtos.add(
                        new StatDto(
                                resultSet.getString("fullName"),
                                resultSet.getString("productName"),
                                resultSet.getInt("expenses")
                        )
                );
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statDtos;
    }
}
