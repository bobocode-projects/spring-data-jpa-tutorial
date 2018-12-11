package com.bobocode;

import com.bobocode.config.RootConfig;
import com.bobocode.model.RoleType;
import com.bobocode.model.User;
import com.bobocode.persistence.UserRepository;
import com.bobocode.util.TestDataGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


public class RepositoryBasicsApp {
    private static int DEFAULT_USERS_COUNT = 10;
    private static UserRepository userRepository;
    private static TestDataGenerator dataGenerator;
    private static List<User> generatedUsers;


    public static void main(String[] args) {
        init();

        printOptionalUserEmailById();
        printUserProxyClassById();
        printAllSortedUserFirstNames();
        deleteUserById();
        printUserLastNameByEmail();
        printFullUserList();
        deleteAllAdmins();
        printFullUserList();
    }

    private static void init() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(RootConfig.class);
        userRepository = applicationContext.getBean(UserRepository.class);
        dataGenerator = applicationContext.getBean(TestDataGenerator.class);
        generateUsers(DEFAULT_USERS_COUNT);
    }

    private static void generateUsers(int usersCount) {
        generatedUsers = Stream.generate(dataGenerator::generateUser)
                .limit(usersCount)
                .collect(toList());
        userRepository.saveAll(generatedUsers);
    }

    private static void printOptionalUserEmailById() {
        Long userId = generatedUsers.get(0).getId();
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(user -> System.out.printf("> Found optional user with email %s by id = %d%n\n", user.getEmail(), userId));
    }

    private static void printUserProxyClassById() {
        Long userId = generatedUsers.get(1).getId();
        User user = userRepository.getOne(userId);
        System.out.printf("> Got user proxy of class %s by id = %d%n\n", user.getClass().getName(), userId);
    }

    private static void printAllSortedUserFirstNames() {
        List<User> usersSortedByFirstName = userRepository.findAll(Sort.by("firstName"));
        System.out.println("> Users first names (sorted):");
        usersSortedByFirstName.stream().map(User::getFirstName).forEach(System.out::println);
        System.out.println();
    }


    private static void deleteUserById() {
        Long userId = generatedUsers.get(0).getId();
        userRepository.deleteById(userId);
        System.out.printf("> Remove user by id = %d%n\n", userId);
    }

    private static void printUserLastNameByEmail() {
        String email = generatedUsers.get(2).getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        optionalUser.ifPresent(user -> System.out.printf("> User last name %s, found by email = %s%n\n", user.getLastName(), email));
    }

    private static void printFullUserList() {
        List<User> completeUserList = userRepository.findAllFetchAddressAndRoles();
        System.out.println("> Complete user list:");
        completeUserList.forEach(System.out::println);
        System.out.println();
    }

    private static void deleteAllAdmins() {
        System.out.println("> Remove all admins\n");
        userRepository.removeAll(user -> user.getRoles().stream()
                .noneMatch(role -> role.getRoleType().equals(RoleType.ADMIN)));
    }

}
