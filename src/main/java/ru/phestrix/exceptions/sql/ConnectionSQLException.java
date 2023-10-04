package ru.phestrix.exceptions.sql;

import java.sql.SQLException;

public class ConnectionSQLException extends SQLException {
    public ConnectionSQLException(String message){
        super(message);
    }
}
