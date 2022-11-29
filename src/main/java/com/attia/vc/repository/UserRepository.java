package com.attia.vc.repository;

import com.attia.vc.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public  interface UserRepository extends CrudRepository<com.attia.vc.model.User, Long> {
    Optional<User> findByUuid(String UUID);
    Optional<User> findByUsername(String username);
}
