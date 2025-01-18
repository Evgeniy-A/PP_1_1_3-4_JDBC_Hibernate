package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connection;

    public UserDaoJDBCImpl() {
        connection = Util.getConnection();
    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(50),
                        last_name VARCHAR(50),
                        age TINYINT 
                    );
                    """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    DROP TABLE IF EXISTS users;
                    """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement prestatement = connection.prepareStatement("""
                INSERT INTO users (name, last_name, age)
                VALUES (?, ?, ?);
                """);) {
            prestatement.setString(1, name);
            prestatement.setString(2, lastName);
            prestatement.setByte(3, age);
            prestatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement prestatement = connection.prepareStatement("""
                DELETE FROM users 
                WHERE id = ?;
                """)) {
            prestatement.setLong(1, id);
            prestatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("""
                     SELECT id, name, last_name, age FROM users
                     """)) {
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
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    DELETE FROM users;
                    """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}