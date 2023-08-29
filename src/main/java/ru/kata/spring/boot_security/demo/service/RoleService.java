package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void addRole(Role role) {
        roleRepository.save(role);
    }

    public Set<Role> getOriginalRoles(Set<Role> rolesIn) {
        Set<Role> roles = new HashSet<>();
        for (Role i : rolesIn) {
            Role role = roleRepository.findRoleByName(i.getName());
            if (role == null) {
                roleRepository.save(i);
                roles.add(i);
            } else {
                roles.add(role);
            }
        }
        return roles;
    }
}
