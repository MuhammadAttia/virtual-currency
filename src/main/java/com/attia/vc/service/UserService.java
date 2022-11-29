package com.attia.vc.service;


import com.attia.vc.mapper.UserMapper;
import com.attia.vc.model.User;
import com.attia.vc.repository.UserRepository;
import com.attia.vc.util.Util;
import org.openapitools.model.UserRequest;
import org.openapitools.model.UserResponse;
import org.openapitools.model.WalletDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final WalletService walletService;
    private final BCryptPasswordEncoder bcryptEncoder;
    public UserService (UserRepository userRepository,UserMapper userMapper, WalletService walletService, BCryptPasswordEncoder bcryptEncoder) {
        this.userRepository=userRepository;
        this.userMapper=userMapper;
        this.walletService = walletService;
        this.bcryptEncoder = bcryptEncoder;
    }


    public UserResponse addUser(UserRequest userRequest) {
        Optional<User> byUsername = userRepository.findByUsername(userRequest.getUsername());
        if (byUsername.isEmpty()) {
            User user = new User(userRequest.getUsername(),userRequest.getEmail(),bcryptEncoder.encode(userRequest.getPassword()), Util.generateUUID());
            user = userRepository.save(user);
            WalletDetails walletDetails = walletService.createWallet(user);
            UserResponse userResponse = userMapper.mapUserToUserResponse(user);
            userResponse.setWalletDetails(walletDetails);
            return userResponse;
        }
        return null;
    }
}
