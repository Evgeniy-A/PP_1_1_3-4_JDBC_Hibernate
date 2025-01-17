package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connection;
    private String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(50),
                last_name VARCHAR(50),
                age TINYINT 
            );
            """;
    private String DROP_TABLE = """
            DROP TABLE IF EXISTS users;
            """;
    private String SAVE_USER = """
            INSERT INTO users (name, last_name, age)
            VALUES (?, ?, ?);
            """;
    private String DELETE_USER = """
            DELETE FROM users 
            WHERE id = ?;
            """;
    private String SELECT_ALL_USERS = """
            SELECT id, name, last_name, age FROM users
            """;
    private String DELITE_TABLE = """
            DELETE FROM users;
            """;

    public UserDaoJDBCImpl() {
        connection = Util.getConnection();
    }


    public void createUsersTable() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(DROP_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            PreparedStatement prestatement = connection.prepareStatement(SAVE_USER);
            prestatement.setString(1, name);
            prestatement.setString(2, lastName);
            prestatement.setByte(3, age);
            prestatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    public void removeUserById(long id) {
        try {
            PreparedStatement prestatement = connection.prepareStatement(DELETE_USER);
            prestatement.setLong(1, id);
            prestatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                String name = resultSet.getString(1);
                user.setName(name != null ? name : "");
                String lastName = resultSet.getString(2);
                user.setLastName(lastName != null ? lastName : "");
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public void cleanUsersTable() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(DELITE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
