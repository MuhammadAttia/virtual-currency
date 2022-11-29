package com.attia.vc.service;


import com.attia.vc.mapper.UserMapper;
import com.attia.vc.model.User;
import com.attia.vc.repository.UserRepository;
import com.attia.vc.util.Util;
import org.openapitools.model.UserRequest;
import org.openapitools.model.UserResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository=userRepository;
        this.userMapper=userMapper;
    }


    public UserResponse addUser(UserRequest userRequest) {
        Optional<User> byUsername = userRepository.findByUsername(userRequest.getUsername());
        if (byUsername.isEmpty()) {
            User user = new User(userRequest.getUsername(),userRequest.getEmail(), Util.generateUUID());
            user = userRepository.save(user);
            return userMapper.mapUserToUserResponse(user);
        }
        return null;
    }
}
