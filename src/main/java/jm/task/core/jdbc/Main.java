package jm.task.core.jdbc;


import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {

        User user1 = new User("Ivan", "Ivanov", (byte) 18);
        User user2 = new User("Irina", "Popova", (byte) 19);
        User user3 = new User("Mariya", "Romanova", (byte) 20);
        User user4 = new User("Vladimir", "Bochurin", (byte) 21);

        UserDaoJDBCImpl userDao = new UserDaoJDBCImpl();
        UserServiceImpl userService = new UserServiceImpl(userDao);
        userService.createUsersTable();

        userService.saveUser(user1.getName(), user1.getLastName(), user1.getAge());
        userService.saveUser(user2.getName(), user2.getLastName(), user2.getAge());
        userService.saveUser(user3.getName(), user3.getLastName(), user3.getAge());
        userService.saveUser(user4.getName(), user4.getLastName(), user4.getAge());
        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}