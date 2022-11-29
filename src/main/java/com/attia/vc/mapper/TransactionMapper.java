package com.attia.vc.mapper;

import com.attia.vc.model.Transaction;
import org.openapitools.model.TransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionResponse mapTransactionToTransactionResponse(Transaction transaction) {
        TransactionResponse transactionResponse= new TransactionResponse();
       transactionResponse.ref(transaction.getReferenceNumber()).amount(String.valueOf(transaction.getAmount()))
                .from(transaction.getSenderUUID()).to(transaction.getReceiverUUID());
        return transactionResponse;
    }

}
