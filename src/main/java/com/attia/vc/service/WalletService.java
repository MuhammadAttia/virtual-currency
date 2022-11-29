package com.attia.vc.service;


import com.attia.vc.mapper.WalletMapper;
import com.attia.vc.model.User;
import com.attia.vc.model.Wallet;
import com.attia.vc.repository.UserRepository;
import com.attia.vc.repository.WalletRepository;
import com.attia.vc.util.Util;
import org.openapitools.model.WalletDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final UserRepository userRepository;

    public WalletService(WalletRepository walletRepository, WalletMapper walletMapper, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
        this.userRepository = userRepository;
    }


    public WalletDetails createWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setFund(BigDecimal.ZERO);
        wallet.setUser(user);
        wallet.setUUID(Util.generateUUID());
        wallet.setLastAccrueDate(LocalDateTime.now());

        wallet = walletRepository.save(wallet);

        user.setWallet(wallet);
        user = userRepository.save(user);

        return walletMapper.mapWalletDAOTOWalletDetails(wallet);
    }

    public void updateWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }
}
