package com.attia.vc.repository;

import com.attia.vc.model.UserHasTransaction;
import com.attia.vc.model.UserHasTransactionId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public  interface UserHasTransactionRepository extends CrudRepository<UserHasTransaction, UserHasTransactionId> {
}
