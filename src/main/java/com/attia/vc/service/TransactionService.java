package com.attia.vc.service;



import com.attia.vc.mapper.TransactionMapper;
import com.attia.vc.model.*;
import com.attia.vc.repository.UserRepository;
import org.openapitools.model.TransactionResponse;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;
    public TransactionService(UserRepository userRepository, TransactionMapper transactionMapper) {
        this.userRepository=userRepository;
        this.transactionMapper=transactionMapper;
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
        return null;
    }
}
