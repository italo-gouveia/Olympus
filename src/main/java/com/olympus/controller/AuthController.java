package com.olympus.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olympus.data.model.User;
import com.olympus.repository.UserRepository;
import com.olympus.security.AccountCredentialsVO;
import com.olympus.security.jwt.JwtTokenProvider;
import com.olympus.services.UserServices;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "AuthenticationEndpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserRepository repository;
    
    @Autowired
    UserServices service;

    @ApiOperation(value = "Authenticates a user and returns a token")
    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/signin", produces = { "application/json", "application/xml", "application/x-yaml" },
    	consumes = { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity signin(@RequestBody AccountCredentialsVO data) {
        //TODO: Add type to generic ResponseEntity
    	//TODO: Add Object to a VO and return him
    	//TODO: Add this logic of business to a command handler
    	//TODO: Remove the try catch block
    	try {
        	 var username = data.getUsername();
             var email = data.getEmail();
             var password = data.getPassword();

             var user = service.findByUsernameOrEmail(username, email);

             authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), password));

             var token = "";

             if (user.getUsername() != null) {
                 token = tokenProvider.createToken(user.getUsername(), user.getRoles());
             } else {
                 throw new UsernameNotFoundException("Username " + user.getUsername() + " not found!");
             }

             Map<Object, Object> model = new HashMap<>();
             model.put("username", user.getUsername());
             model.put("token", token);
             return ok(model);
        } catch (AuthenticationException e) {
        	//TODO: Solve the problem that this exception isn't return in the result of request
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }
    
    //This endpoint it's hidden from swagger page to be acessed only by a frontend application
    @ApiIgnore
    @Transactional
    @ApiOperation(value = "Create a user and returns a token")
    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/signup", produces = { "application/json", "application/xml", "application/x-yaml" },
            consumes = { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity signup(@RequestBody AccountCredentialsVO user) {
        //TODO: Add type to generic ResponseEntity
    	//TODO: Add Object to a VO and return him
    	//TODO: Add this logic of business to a command handler
        User data = service.save(user);

        Map<Object, Object> model = new HashMap<>();
        model.put("username", data.getUsername());
        return ok(model);
    }
}