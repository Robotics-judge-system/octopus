package ru.anarcom.octopus.service;


import ru.anarcom.octopus.entity.User;

public interface UserService {

    User findByUsername(String username);

    User findById(Long id);

    /**
     * Hard deletes user from DB.
     * <p>
     * Note: Be sure that it is a time to delete User from DB, may be you have to use delete?
     * </p>
     *
     * @param id Id of user to delete.
     */
    void deleteHard(Long id);

    /**
     * Deletes user by id (STATUS = DELETED).
     * @param id Id of user to delete.
     */
    void delete(Long id);

    /**
     * Registers user with some information.
     * @param username Username of new User.
     * @param email Email of new User.
     * @param name Name of new User.
     * @param password Not Encrypted password of new User.
     * @return New user (if was created).
     */
    User registerUser(
        String username,
        String email,
        String name,
        String password
    );

}
