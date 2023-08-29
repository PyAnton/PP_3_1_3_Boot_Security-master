package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.configs.WebSecurityConfig;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final WebSecurityConfig webSecurityConfig;

    @Autowired
    public UserService(UserRepository userRepository, WebSecurityConfig webSecurityConfig, RoleService roleService) {
        this.userRepository = userRepository;
        this.webSecurityConfig = webSecurityConfig;
        this.roleService = roleService;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Transactional
    public boolean add(User user) {
        if (userRepository.findUserByEmail(user.getEmail()) == null) {
            user.setRoles(roleService.getOriginalRoles(user.getRoles()));
            user.setActive(true);
            user.setPassword(webSecurityConfig.passwordEncoder().encode(user.getPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean dell(long id) {
        User user = userRepository.findUserById(id);
        if (user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    @Transactional
    public List<User> listUsersCount(int count) {
        List<User> listUsers = listUsers();
        if (count >= 15) {
            return listUsers;
        } else {
            return listUsers.subList(0, Math.min(count, listUsers.size()));
        }
    }

    @Transactional
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void edit(Long id, User user, String role) {
        userRepository.findById(id).ifPresent(saveUser -> {
            saveUser.setEmail(user.getEmail());
            saveUser.setAge(user.getAge());
            saveUser.setActive(user.isActive());
            saveUser.setFirstName(user.getFirstName());
            saveUser.setLastName(user.getLastName());
            saveUser.clearRoles();
            saveUser.addRole(role);
            saveUser.setRoles(roleService.getOriginalRoles(saveUser.getRoles()));
            if (!saveUser.getPassword().equals(user.getPassword())) {
                saveUser.setPassword(webSecurityConfig.passwordEncoder().encode(user.getPassword()));

            }
            userRepository.save(saveUser);
        });
    }
}
