package com.attia.vc.mapper;


import com.attia.vc.model.User;
import org.openapitools.model.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public UserResponse mapUserToUserResponse(User user){

        UserResponse userResponse = new UserResponse();
        return userResponse.username(user.getUsername())
                .email(user.getEmail())
                .userId(user.getUuid());
    }
}
