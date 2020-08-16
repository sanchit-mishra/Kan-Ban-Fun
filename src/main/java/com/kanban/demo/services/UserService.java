package com.kanban.demo.services;

import com.kanban.demo.domain.User;
import com.kanban.demo.exceptions.UsernameException;
import com.kanban.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){
        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setConfirmPassword("");
            newUser.setUsername(newUser.getUsername()); //Exception Handling
            return userRepository.save(newUser);

        } catch (Exception ex){
            throw new UsernameException("Username '" + newUser.getUsername() + "' already exists ");
        }

    }
}
