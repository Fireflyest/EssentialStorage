package com.fireflyest.storage.util;

import java.sql.*;

/**
 * @author Fireflyest
 * 2022/1/4 18:40
 */

public class SqliteUtils {

    private SqliteUtils(){}

    public static void init(){
        try{
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }

    public static void close(ResultSet resultSet, Statement statement, Connection connection){
        try {
            if(resultSet != null)resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(statement != null)statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(connection != null)connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Statement statement, Connection connection){
        close(null, statement, connection);
    }

}
