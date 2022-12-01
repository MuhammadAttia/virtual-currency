package com.attia.vc.controller;


import com.attia.vc.service.TransactionService;
import org.openapitools.api.TransactionsApi;
import org.openapitools.model.BeneficiaryDetails;
import org.openapitools.model.TransactionResponse;
import org.openapitools.model.TransactionResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @Override
    public ResponseEntity<List<TransactionResponseStatus>> createTransaction(UUID userUUID, @Valid List<BeneficiaryDetails> beneficiaryDetails) {
        List<TransactionResponseStatus> transaction = transactionService.createTransaction(userUUID, beneficiaryDetails);
        return ResponseEntity.status(getHttpResponseFromResult(transaction)).body(transaction);
    }

    private HttpStatus getHttpResponseFromResult(List<TransactionResponseStatus> transaction) {
        if(CollectionUtils.isEmpty(transaction) || transaction.stream().allMatch(transactionResponseStatus -> TransactionResponseStatus.TransactionStatusEnum.CREATED.equals(transactionResponseStatus.getTransactionStatus()))) {
            return HttpStatus.OK;
        }else if (transaction.stream().allMatch(transactionResponseStatus -> TransactionResponseStatus.TransactionStatusEnum.ERROR.equals(transactionResponseStatus.getTransactionStatus()))) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.MULTI_STATUS;
    }
}
