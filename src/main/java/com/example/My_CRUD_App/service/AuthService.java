package com.example.My_CRUD_App.service;

import com.example.My_CRUD_App.dto.request.LoginRequest;
import com.example.My_CRUD_App.dto.request.RegisterRequest;
import com.example.My_CRUD_App.dto.response.LoginResponse;
import com.example.My_CRUD_App.entity.User;
import com.example.My_CRUD_App.enums.Roles;
import com.example.My_CRUD_App.security.Jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;


    public LoginResponse register(RegisterRequest request){
        if(userService.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email is already used !");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        System.out.println("PasswordEncoder instance: " + passwordEncoder.hashCode());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        Set<Roles> defaultRoles = new HashSet<>();
        defaultRoles.add(Roles.USER);  // assuming this is one of your enum values
        user.setRoles(defaultRoles);
        userService.createUser(user);
        String token = jwtService.generateToken(user);
        LoginResponse response = new LoginResponse(token, jwtService.extractExpiration(token), LocalDateTime.now());
        return response;
    }

    public LoginResponse login(LoginRequest request){
        User user = userService.getUserByEmail(request.getEmail());

        System.out.println("PasswordEncoder instance: " + passwordEncoder.hashCode());
        System.out.println("Plain: " + request.getPassword());
        System.out.println("Encoded in DB: " + user.getPassword());
        System.out.println("Match: " + passwordEncoder.matches(request.getPassword(), user.getPassword()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials! -from login method-");
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(token, jwtService.extractExpiration(token), LocalDateTime.now());
    }

}
