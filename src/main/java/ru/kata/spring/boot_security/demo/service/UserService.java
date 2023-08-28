package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    //private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                mapRoles(user.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> mapRoles(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Transactional
    public boolean add(User user) {
        if (userRepository.findUserByEmail(user.getEmail()) == null) {
            Set<Role> roles = new HashSet<>();
            for (Role i : user.getRoles()) {
                Role role = roleRepository.findRoleByName(i.getName());
                if (role == null) {
                    roleRepository.save(i);
                    roles.add(i);
                } else {
                    roles.add(role);
                }
            }
            user.clearRoles();
            user.setRoles(roles);
            user.setActive(true);
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
//            saveUser.clearRoles();
//            saveUser.addRole(role);
            saveUser.setPassword(user.getPassword());
            userRepository.save(saveUser);
        });
    }
}
