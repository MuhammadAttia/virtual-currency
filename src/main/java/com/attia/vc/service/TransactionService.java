package com.attia.vc.service;


import com.attia.vc.exception.ApiException;
import com.attia.vc.exception.BadRequestException;
import com.attia.vc.exception.NotFoundException;
import com.attia.vc.mapper.TransactionMapper;
import com.attia.vc.model.*;
import com.attia.vc.repository.TransactionRepository;
import com.attia.vc.repository.UserHasTransactionRepository;
import com.attia.vc.repository.UserRepository;
import com.attia.vc.util.Util;
import org.openapitools.model.BeneficiaryDetails;
import org.openapitools.model.TransactionResponse;
import org.openapitools.model.TransactionResponseStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final UserHasTransactionRepository userHasTransactionRepository;
    private final WalletService walletService;

    public TransactionService(UserRepository userRepository, TransactionMapper transactionMapper, TransactionRepository transactionRepository, UserHasTransactionRepository userHasTransactionRepository, WalletService walletService) {
        this.userRepository = userRepository;
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
        this.userHasTransactionRepository = userHasTransactionRepository;
        this.walletService = walletService;
    }


    public List<TransactionResponse> getTransactionByUserUUID(String userUUID) {

        Optional<User> usr = userRepository.findByUuid(userUUID);
        if (usr.isPresent()) {
            User user = usr.get();
            Set<UserHasTransaction> userHasTransaction = user.getUserHasTransaction();
            return userHasTransaction.stream().map(uht -> {
                TransactionResponse transactionResponse = transactionMapper.mapTransactionToTransactionResponse(uht.getTransaction());
                transactionResponse.setTranactionType(uht.getType().toString());
                return transactionResponse;
            }).collect(Collectors.toList());
        }
        throw new NotFoundException(String.format("user '%s' does not exist", userUUID));
    }

    @Transactional
    public List<TransactionResponseStatus> createTransaction(UUID userUUID, List<BeneficiaryDetails> beneficiaryDetails) {
        validateListSize(beneficiaryDetails.size());

        User user = getUserById(userUUID.toString());

        validateTotalFund(user, beneficiaryDetails);

        List<TransactionResponseStatus> transactionResponseStatusList = new ArrayList<>();
        beneficiaryDetails.forEach(benefData -> {
            TransactionResponseStatus transactionResponseStatus = new TransactionResponseStatus();

            try {
                User beneficiary = getUserById(benefData.getUserId());

                validateUserIsNotBeneficiary(user, beneficiary);

                validateBeneficiaryData(beneficiary, benefData);

                Transaction transaction = createTransaction(user, benefData, beneficiary);

                addTransactionToUser(user, transaction, TransactionType.SEND);

                addTransactionToUser(beneficiary, transaction, TransactionType.RECEIVE);

                transactionResponseStatus = transactionMapper.mapTransactionToTransactionResponseStatus(transaction, TransactionResponseStatus.TransactionStatusEnum.CREATED);

            } catch (Exception e) {
                if (e instanceof ApiException) {
                    transactionResponseStatus.transactionStatus(TransactionResponseStatus.TransactionStatusEnum.ERROR)
                            .errorReason(e.getMessage())
                            .to(benefData.getUserId());
                } else {

                    throw e;
                }
            }

            transactionResponseStatusList.add(transactionResponseStatus);
        });

        return transactionResponseStatusList;

    }

    private void validateBeneficiaryData(User beneficiary, BeneficiaryDetails benefData) {
        if (!beneficiary.getWallet().getUUID().equals(benefData.getWalletId())) {
            throw new BadRequestException("Beneficiary Wallet does not match");
        }

        if (benefData.getAmount().signum() != 1) {
            throw new BadRequestException("Amount must be Positive Number");
        }
    }

    private void validateUserIsNotBeneficiary(User user, User beneficiary) {
        if (user.getUuid().equals(beneficiary.getUuid())) {
            throw new BadRequestException("User can not be the same as Beneficiary");
        }
    }

    private User getUserById(String userID) {
        return userRepository.findByUuid(userID).orElseThrow(() -> new NotFoundException(String.format("user '%s' does not exist", userID)));
    }

    private void validateListSize(int size) {
        if (size < 1 || size > 10) {
            throw new BadRequestException("List size must be from 1 to 10");
        }
    }

    private void validateTotalFund(User user, List<BeneficiaryDetails> beneficiaryDetails) {
        BigDecimal totalBenefListAmount = beneficiaryDetails.stream().map(BeneficiaryDetails::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (user.getWallet().getFund().compareTo(totalBenefListAmount) < 0) {
            throw new BadRequestException("Insufficient Fund. total amount must be less than or equal to Wallet Amount");
        }
    }

    private void addTransactionToUser(User user, Transaction transaction, TransactionType transactionType) {
        UserHasTransaction senderHasTransaction = new UserHasTransaction();
        senderHasTransaction.setUser(user);
        senderHasTransaction.setTransaction(transaction);
        senderHasTransaction.setType(transactionType);

        userHasTransactionRepository.save(senderHasTransaction);
    }

    private Transaction createTransaction(User user, BeneficiaryDetails benefData, User beneficiary) {

        Transaction transaction = new Transaction();
        transaction.setAmount(benefData.getAmount());
        transaction.setSenderUUID(user.getUuid());
        transaction.setReceiverUUID(beneficiary.getUuid());
        transaction.setReferenceNumber(Util.generateUUID());

        transaction = transactionRepository.save(transaction);

        Wallet userWallet = user.getWallet();
        userWallet.setFund(userWallet.getFund().subtract(benefData.getAmount()));

        walletService.updateWallet(userWallet);

        Wallet beneficiaryWallet = beneficiary.getWallet();
        beneficiaryWallet.setFund(beneficiaryWallet.getFund().add(benefData.getAmount()));
        walletService.updateWallet(beneficiaryWallet);

        return transaction;
    }
}
