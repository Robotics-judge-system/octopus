package ru.anarcom.octopus.service

import ru.anarcom.octopus.entity.User

interface UserService {
    fun findByUsername(username: String): User?
    fun findById(id: Long): User?
    fun findByUsernameOrThrow(username:String): User


    /**
     * Hard deletes user from DB.
     *
     *
     * Note: Be sure that it is a time to delete User from DB, may be you have to use delete?
     *
     *
     * @param id Id of user to delete.
     */
    fun deleteHard(id: Long)

    /**
     * Deletes user by id (STATUS = DELETED).
     * @param id Id of user to delete.
     */
    fun delete(id: Long)

    /**
     * Registers user with some information.
     * @param username Username of new User.
     * @param email Email of new User.
     * @param name Name of new User.
     * @param password Not Encrypted password of new User.
     * @return New user (if was created).
     */
    fun registerUser(
        username: String,
        email: String,
        name: String,
        password: String
    ): User

    /**
     * Updates user if field is not null.
     */
    fun updateUser(
        name: String?,
        user: User
    ): User

    /**
     * Changes user password
     * @param user User to change password
     * @param oldPassword old User password
     * @param newPassword new User password
     * @return User with new Password
     */
    fun changePassword(
        user: User,
        oldPassword: String,
        newPassword: String
    ): User

    /**
     * Return true if username is already used.
     */
    fun existsByUsername(username: String): Boolean

    /**
     * Return true if email is already used.
     */
    fun existsByEmail(email: String): Boolean

    /**
     * Finds user for email.
     */
    fun findByEmailOrThrow(email: String):User

}
