package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    void add(User user);
    void dell(long id);
//    void update(long id, String firstName, String lastName, String email);
    User findUserById(long id);
    List<User> listUsers();
}
