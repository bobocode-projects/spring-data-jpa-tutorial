package com.bobocode.persistence;

import com.bobocode.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    @Query("select u from User u left join fetch u.address left join fetch u.roles order by u.id")
    List<User> findAllFetchAddressAndRoles();

    Optional<User> findByEmail(String email);
}
