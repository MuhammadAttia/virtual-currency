package com.attia.vc.repository;

import com.attia.vc.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public  interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
