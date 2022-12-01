package com.attia.vc.controller;

import com.attia.vc.service.UserService;
import org.openapitools.api.UsersApi;

import org.openapitools.model.UserRequest;
import org.openapitools.model.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserController implements UsersApi {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserResponse> addUser(UserRequest userRequest) {
        UserResponse userResponse = userService.addUser(userRequest);
        return  ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @Override
    public ResponseEntity<UserResponse> getUser(UUID userId) {
        UserResponse userResponse = userService.getUser(userId);
        return  ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }
}
