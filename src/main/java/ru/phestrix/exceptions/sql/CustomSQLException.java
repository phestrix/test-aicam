package ru.phestrix.exceptions.sql;

import java.sql.SQLException;

public class CustomSQLException extends SQLException {
   public CustomSQLException(String message){
   super(message);
    }
}
