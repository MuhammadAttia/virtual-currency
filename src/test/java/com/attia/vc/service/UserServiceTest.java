package com.attia.vc.service;

import com.attia.vc.exception.BadRequestException;
import com.attia.vc.mapper.UserMapper;
import com.attia.vc.model.User;
import com.attia.vc.repository.UserRepository;
import com.attia.vc.util.UUIDUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openapitools.model.UserRequest;
import org.openapitools.model.UserResponse;
import org.openapitools.model.WalletDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;
    @Mock
    BCryptPasswordEncoder bcryptEncoder;
    @Mock
    UserMapper userMapper;
    @Mock
    WalletService walletService;


    @Test
    void testValidAddUser() {

        UserRequest userRequest = new UserRequest();
        userRequest.username("attia").email("muhammad.mattia@gmail.com").password("123");
        when(bcryptEncoder.encode(any(String.class))).thenReturn(any(String.class));

        User user = new User(userRequest.getUsername(),userRequest.getEmail(),userRequest.getPassword(), UUIDUtil.generateUUID());
        when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userRequest.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).then(returnsFirstArg());
        when(walletService.createWallet(user)).thenReturn(new WalletDetails());

        UserResponse userResponse = new UserResponse();
        userResponse.username(user.getUsername())
                .email(user.getEmail())
                .userId(user.getUuid());

        when(userMapper.mapUserToUserResponse(any(User.class))).thenReturn(userResponse);

        assertThat(userService.addUser(userRequest)).isNotNull();
    }

    @Test
    void testAddUserEmpty() {
        UserRequest userRequest = new UserRequest();
        assertThatThrownBy(() -> userService.addUser(userRequest)).isInstanceOf(BadRequestException.class). hasMessage("Username, Password and Email must not be empty");
    }


    @Test
    void testAddDuplicatedUserName() {
        UserRequest userRequest = new UserRequest();
        userRequest.username("attia").email("muhammad.mattia@gmail.com").password("123");
        when(bcryptEncoder.encode(any(String.class))).thenReturn(any(String.class));
        User user = new User(userRequest.getUsername(),userRequest.getEmail(),userRequest.getPassword(), UUIDUtil.generateUUID());

        when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.addUser(userRequest)).isInstanceOf(BadRequestException.class).hasMessage("this username: 'attia' is already Exist please choose another one");

    }
}