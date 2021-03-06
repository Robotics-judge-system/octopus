package ru.anarcom.octopus.service.impl


import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.User
import ru.anarcom.octopus.exceptions.IncorrectPasswordException
import ru.anarcom.octopus.exceptions.InvalidLoginOrPasswordException
import ru.anarcom.octopus.exceptions.ValidationException
import ru.anarcom.octopus.repo.UserRepository
import ru.anarcom.octopus.service.UserService
import ru.anarcom.octopus.util.logger
import java.time.Clock

@Service
@Slf4j
@RequiredArgsConstructor //TODO change logger messages
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val clock: Clock,
) : UserService {

    private val log = logger()

    override fun findActiveByUsername(username: String): User? =
        userRepository.findByUsernameAndStatus(username, Status.ACTIVE)

    override fun findById(id: Long): User? =
         userRepository.findById(id).orElse(null)

    override fun findByUsernameOrThrow(username: String): User =
        userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User with current username was not found")


    override fun deleteHard(id: Long) {
        userRepository.deleteById(id)
        log.info("IN delete - user with id: {} successfully (hard) deleted", id)
    }

    override fun delete(id: Long) {
        val user = findById(id)?:throw UsernameNotFoundException("User not found")
        user.status = Status.DELETED
        userRepository.save(user)
    }

    override fun registerUser(username: String, email: String, name: String, password: String): User {
        if (
            userRepository.existsByEmail(email) ||
            userRepository.existsByUsername(name)
        ) {
            throw ValidationException("username or email is already in use")
        }
        val time = clock.instant()
        val user = User(
            email = email,
            name = name,
            username = username,
            password = passwordEncoder.encode(password),
            created = time,
            updated = time,
            status = Status.ACTIVE
        )

        return userRepository.save(user)
    }

    override fun updateUser(name: String?, user: User): User {
        if (name != null) {
            user.name = name
        }
        user.updated = clock.instant()
        return userRepository.save(user)
    }

    override fun changePassword(user: User, oldPassword: String, newPassword: String): User {
        if (!passwordEncoder.matches(
                oldPassword,
                user.password
            )
        ) {
            throw IncorrectPasswordException("Password incorrect")
        }
        user.password = passwordEncoder.encode(newPassword)
        user.updated = clock.instant()
        return userRepository.save(user)
    }

    override fun existsByUsername(username: String): Boolean =
        userRepository.existsByUsername(username)

    override fun existsByEmail(email: String): Boolean =
        userRepository.existsByEmail(email)

    override fun findByEmailOrThrow(email: String): User =
        userRepository.findByEmail(email)
            ?: throw InvalidLoginOrPasswordException()
}
