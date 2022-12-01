package com.attia.vc.mapper;


import com.attia.vc.model.User;
import org.openapitools.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    WalletMapper walletMapper;

    public UserMapper(WalletMapper walletMapper) {
        this.walletMapper = walletMapper;
    }

    public UserResponse mapUserToUserResponse(User user){

        UserResponse userResponse = new UserResponse();
        return userResponse.username(user.getUsername())
                .email(user.getEmail())
                .userId(user.getUuid())
                .walletDetails(walletMapper.mapWalletDAOTOWalletDetails(user.getWallet()));
    }
}
