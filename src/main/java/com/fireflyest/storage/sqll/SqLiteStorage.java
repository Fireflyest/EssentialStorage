package com.fireflyest.storage.sqll;

import com.fireflyest.essential.api.Storage;
import com.fireflyest.storage.util.SqliteUtils;
import com.fireflyest.storage.util.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fireflyest
 * 2022/1/3 11:11
 */

public class SqLiteStorage implements Storage {

    private final String url;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public SqLiteStorage(String url) {
        this.url = url;
        SqliteUtils.init();
    }

    @Override
    public <T> T inquiry(String s, Class<T> aClass) {
        List<T> list = this.inquiryList(s, aClass);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public <T> List<T> inquiryList(String s, Class<T> aClass) {
        List<T> list = new ArrayList<>();
        try {
            this.connection();
            resultSet = statement.executeQuery(s);
            while (resultSet.next()){
                T t = aClass.getDeclaredConstructor().newInstance();
                for(Field field : ReflectUtils.getClassFields(aClass)){
                    ReflectUtils.invokeSet(t, field.getName(), resultSet.getObject(field.getName()));
//                    System.out.println(field.getName() + " = " + resultSet.getObject(field.getName()));
                    System.out.println("ReflectUtils.invokeGet(t, field.getName()) = " + ReflectUtils.invokeGet(t, field.getName()));
                }
                list.add(t);
            }
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(String s) {
        try {
            this.connection();
            statement.executeUpdate(s);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqliteUtils.close(statement, connection);
        }
    }

    @Override
    public void delete(String s) {
        try {
            this.connection();
            statement.executeUpdate(s);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqliteUtils.close(statement, connection);
        }
    }

    @Override
    public int insert(String s) {
        int id = 0;
        try {
            this.connection();
            PreparedStatement preparedStatement = connection.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()){
                id = resultSet.getInt(1);//返回主键值
            }
            preparedStatement.close();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqliteUtils.close(resultSet, statement, connection);
        }
        return id;
    }

    @Override
    public void createTable(String s) {
        try {
            this.connection();
            statement.execute(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqliteUtils.close(statement, connection);
        }
    }

    private void connection() throws SQLException {
        connection = SqliteUtils.getConnection(url);
        statement = connection.createStatement();
    }

}
