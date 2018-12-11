package com.bobocode.persistence;

import com.bobocode.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.function.Predicate;

@Repository
@Transactional
public class CustomUserRepositoryImpl implements CustomUserRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void removeAll(Predicate<User> userPredicate) {
        List<User> users = entityManager
                .createQuery("select u from User u", User.class)
                .getResultList();

        users.stream()
                .filter(userPredicate)
                .forEach(entityManager::remove);
    }
}
