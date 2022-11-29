package com.attia.vc.controller;


import com.attia.vc.service.TransactionService;
import org.openapitools.api.TransactionsApi;
import org.openapitools.model.TransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class TransactionController implements TransactionsApi {

    TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public ResponseEntity<List<TransactionResponse>> getTransactionsByUserId(UUID userId) {
        List<TransactionResponse> transactionByUserID = transactionService.getTransactionByUserUUID(userId.toString());
        return  ResponseEntity.status(HttpStatus.OK).body(transactionByUserID);
    }

}
