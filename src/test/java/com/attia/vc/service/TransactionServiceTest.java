package com.attia.vc.service;

import com.attia.vc.exception.BadRequestException;
import com.attia.vc.exception.NotFoundException;
import com.attia.vc.model.User;
import com.attia.vc.model.Wallet;
import com.attia.vc.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.BeneficiaryDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    TransactionService transactionService;

    @Test
    void createTransactionTrowsUserNotFound() {
        var userId = UUID.randomUUID();
        when(userRepository.findByUuid(userId.toString())).thenReturn(Optional.empty());

        ArrayList<BeneficiaryDetails> beneficiaryDetails = new ArrayList<>();
        beneficiaryDetails.add(new BeneficiaryDetails());

        assertThatThrownBy(() -> {
            transactionService.createTransaction(userId , beneficiaryDetails);
        })
                .isInstanceOf(NotFoundException.class)
                .hasMessage("user '" + userId + "' does not exist");
    }

    @Test
    void createTransactionBeneficiaryListBetween1And10() {
        var userId = UUID.randomUUID();
        ArrayList<BeneficiaryDetails> beneficiaryDetails = new ArrayList<>();

        assertThatThrownBy(() -> {
            transactionService.createTransaction(userId , beneficiaryDetails);
        })
                .isInstanceOf(BadRequestException.class)
                .hasMessage("List size must be from 1 to 10");

        for (int i = 0; i <= 10; i++) {
            beneficiaryDetails.add(new BeneficiaryDetails());
        }

        assertThatThrownBy(() -> {
            transactionService.createTransaction(userId , beneficiaryDetails);
        })
                .isInstanceOf(BadRequestException.class)
                .hasMessage("List size must be from 1 to 10");
    }
/**/
    @Test
    void creatTransactionValidateTotalFund() {
        var userId = UUID.randomUUID();
        User user = new User();
        Wallet wallet = new Wallet();
        wallet.setFund(BigDecimal.ZERO);
        user.setWallet(wallet);

        when(userRepository.findByUuid(userId.toString())).thenReturn(Optional.of(user));

        ArrayList<BeneficiaryDetails> beneficiaryDetails = new ArrayList<>();
        beneficiaryDetails.add(new BeneficiaryDetails().amount(new BigDecimal("100")));

        assertThatThrownBy(() -> {
            transactionService.createTransaction(userId , beneficiaryDetails);
        })
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Insufficient Fund. total amount must be less than or equal to Wallet Amount");
    }
}
