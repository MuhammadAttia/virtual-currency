package com.attia.vc.service;


import com.attia.vc.exception.BadRequestException;
import com.attia.vc.exception.NotFoundException;
import com.attia.vc.mapper.UserMapper;
import com.attia.vc.model.User;
import com.attia.vc.repository.UserRepository;
import com.attia.vc.util.UUIDUtil;
import org.openapitools.model.UserRequest;
import org.openapitools.model.UserResponse;
import org.openapitools.model.WalletDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

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
        validateUserRequestData(userRequest);
        Optional<User> byUsername = userRepository.findByUsername(userRequest.getUsername());
        if (byUsername.isEmpty()) {
            if(userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
                throw new BadRequestException(String.format("Email: '%s' is already Exist please choose another one", userRequest.getEmail()));
            }
            User user = new User(userRequest.getUsername(),userRequest.getEmail(),bcryptEncoder.encode(userRequest.getPassword()), UUIDUtil.generateUUID());
            user = userRepository.save(user);
            WalletDetails walletDetails = walletService.createWallet(user);
            UserResponse userResponse = userMapper.mapUserToUserResponse(user);
            userResponse.setWalletDetails(walletDetails);
            return userResponse;
        }
        throw new BadRequestException(String.format("this username: '%s' is already Exist please choose another one", userRequest.getUsername()));

    }

    private void validateUserRequestData(UserRequest userRequest) {
        if(StringUtils.isEmpty(userRequest.getEmail()) || StringUtils.isEmpty(userRequest.getUsername()) || StringUtils.isEmpty(userRequest.getPassword())) {
            throw new BadRequestException("Username, Password and Email must not be empty");
        }
    }

    public UserResponse getUser(UUID userId) {
        Optional<User> user = userRepository.findByUuid(userId.toString());
        if (user.isPresent()) {
            return userMapper.mapUserToUserResponse(user.get());
        }
        throw new NotFoundException("This user is not exist");
    }
}
