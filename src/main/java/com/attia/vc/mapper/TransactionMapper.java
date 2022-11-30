package com.attia.vc.mapper;

import com.attia.vc.model.Transaction;
import org.openapitools.model.TransactionResponse;
import org.openapitools.model.TransactionResponseStatus;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionResponse mapTransactionToTransactionResponse(Transaction transaction) {
        TransactionResponse transactionResponse= new TransactionResponse();
       transactionResponse.ref(transaction.getReferenceNumber()).amount(String.valueOf(transaction.getAmount()))
                .from(transaction.getSenderUUID()).to(transaction.getReceiverUUID());
        return transactionResponse;
    }

    public TransactionResponseStatus mapTransactionToTransactionResponseStatus(Transaction transaction, TransactionResponseStatus.TransactionStatusEnum transactionStatusEnum) {
        TransactionResponseStatus transactionResponse= new TransactionResponseStatus();
        transactionResponse.ref(transaction.getReferenceNumber()).amount(String.valueOf(transaction.getAmount()))
                .from(transaction.getSenderUUID()).to(transaction.getReceiverUUID())
                .transactionStatus(transactionStatusEnum);
        return transactionResponse;
    }

}
