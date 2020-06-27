package com.olympus.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.olympus.data.model.Permission;
import com.olympus.data.model.User;
import com.olympus.exception.ResourceNotFoundException;
import com.olympus.repository.PermissionRepository;
import com.olympus.repository.UserRepository;
import com.olympus.security.AccountCredentialsVO;

@Service
public class UserServices implements UserDetailsService, IUserService {

    @Autowired
    UserRepository repository;
    
    @Autowired
    PermissionRepository permissionRepository;

    public UserServices(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = repository.findByUsername(username);
        if(user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
    }
    
    @Override
    public User findByUsernameOrEmail(String username, String email) {
        User user = repository.findByUsernameOrEmail(username, email);
        if(user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Username " + username + " or email " + email + " not found");
        }
    }
    
    @Override
    public User save(AccountCredentialsVO vo) {
        User user = new User();
        user.setEmail(vo.getEmail());
        user.setFullName(vo.getFullname());
        user.setUserName(vo.getUsername());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(vo.getPassword());
        user.setPassword(encryptedPassword);

        Permission admin = permissionRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        Permission manager = permissionRepository.findById(2L).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        List<Permission> permissions = new ArrayList<>();
        permissions.add(admin);
        permissions.add(manager);

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setPermissions(permissions);
        return repository.save(user);
    }
    
}
