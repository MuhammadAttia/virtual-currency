package com.attia.vc.service;

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
        this.userRepository=userRepository;
        this.transactionMapper=transactionMapper;
        this.transactionRepository=transactionRepository;
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
        return Collections.emptyList();
    }

    @Transactional
    public List<TransactionResponseStatus> createTransaction(UUID userUUID, List<BeneficiaryDetails> beneficiaryDetails) {
        Optional<User> byUuid = userRepository.findByUuid(userUUID.toString());
        if (byUuid.isPresent()) {
            User user = byUuid.get();
            List<TransactionResponseStatus> transactionResponseStatusList = new ArrayList<>();
            beneficiaryDetails.forEach(benefData -> {

                Optional<User> beneficiaryOptional = userRepository.findByUuid(benefData.getUserId());
                TransactionResponseStatus transactionResponseStatus = new TransactionResponseStatus();
                if (beneficiaryOptional.isPresent()) {
                    User beneficiary = beneficiaryOptional.get();
                    if (!user.getUuid().equals(beneficiary.getUuid())) {
                        if (beneficiary.getWallet().getUUID().equals(benefData.getWalletId())) {

                            if (benefData.getAmount().signum() == 1) {
                                Transaction transaction = createTransaction(user, benefData, beneficiary);

                                addTransactionToUser(user, transaction, TransactionType.SEND);

                                addTransactionToUser(beneficiary, transaction, TransactionType.RECEIVE);
                                transactionResponseStatus = transactionMapper.mapTransactionToTransactionResponseStatus(transaction, TransactionResponseStatus.TransactionStatusEnum.CREATED);
                            }else {
                                transactionResponseStatus.transactionStatus(TransactionResponseStatus.TransactionStatusEnum.ERROR)
                                        .errorReason("Amount must be Positive Number")
                                        .to(benefData.getUserId())
                                        .amount(benefData.getAmount().toString());
                            }

                        } else {
                            transactionResponseStatus.transactionStatus(TransactionResponseStatus.TransactionStatusEnum.ERROR)
                                    .errorReason("Beneficiary Wallet does not match")
                                    .to(benefData.getUserId());
                        }
                    } else {
                        transactionResponseStatus.transactionStatus(TransactionResponseStatus.TransactionStatusEnum.ERROR)
                                .errorReason("User can not be the same as Beneficiary")
                                .to(benefData.getUserId());
                    }
                } else {
                    transactionResponseStatus.transactionStatus(TransactionResponseStatus.TransactionStatusEnum.ERROR)
                            .errorReason("Beneficiary User ID not found")
                            .to(benefData.getUserId());
                }
                transactionResponseStatusList.add(transactionResponseStatus);
            });

            return transactionResponseStatusList;
        }
        return null;
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
