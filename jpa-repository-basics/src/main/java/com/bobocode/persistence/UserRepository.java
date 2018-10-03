package com.bobocode.persistence;

import com.bobocode.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u join fetch u.address join fetch u.roles order by u.id")
    List<User> findAllFetchAddressAndRoles();

    Optional<User> findByEmail(String email);
}
