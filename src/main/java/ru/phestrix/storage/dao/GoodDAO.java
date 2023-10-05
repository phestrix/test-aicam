package ru.phestrix.storage.dao;

import ru.phestrix.storage.databaseConnection.DatabaseConnection;
import ru.phestrix.storage.domain.Good;

import java.sql.SQLException;

public class GoodDAO {
    public void save(Good good){
        try{
            good.save();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void update(Good good){
        try{
            good.update();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(Good good){
        try{
            good.delete();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public Good getById(Integer id){
        Good good = null;
        try{
            good = new Good(DatabaseConnection.getConnection(), id, null, null).getGoodById();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return good;
    }
}
