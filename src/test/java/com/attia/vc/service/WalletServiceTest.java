package com.attia.vc.service;

import com.attia.vc.mapper.WalletMapper;
import com.attia.vc.model.User;
import com.attia.vc.model.Wallet;
import com.attia.vc.repository.UserRepository;
import com.attia.vc.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openapitools.model.WalletDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WalletServiceTest {

    @InjectMocks
    WalletService walletService;

    @Mock
    UserRepository userRepository;
    @Mock
    WalletRepository walletRepository;

    @Mock
    WalletMapper walletMapper ;

    @Test
    void createWallet() {
        User user = new User("attia","muhammad.mattia@gmail.com","123");
        when(walletRepository.save(any(Wallet.class))).then(returnsFirstArg());
        when(userRepository.save(any(User.class))).then(returnsFirstArg());

        WalletDetails walletDetails = new WalletDetails();
        when(walletMapper.mapWalletDAOTOWalletDetails(any(Wallet.class))).thenReturn(walletDetails);
        assertThat(walletService.createWallet(user)).isNotNull();

    }
}