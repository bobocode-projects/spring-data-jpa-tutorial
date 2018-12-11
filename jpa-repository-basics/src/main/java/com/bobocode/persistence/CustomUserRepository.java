package com.bobocode.persistence;

import com.bobocode.model.User;

import java.util.function.Predicate;

public interface CustomUserRepository {
    void removeAll(Predicate<User> userPredicate);
}
