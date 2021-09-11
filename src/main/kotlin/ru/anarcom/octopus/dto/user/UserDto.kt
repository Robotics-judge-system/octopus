package ru.anarcom.octopus.dto.user

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import lombok.Data
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.entity.User

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
data class UserDto(
    val id: Long = 1,
    val username: String = "",
    val name: String = "",
    val email: String = "",
    val status: String = "",
) {

    fun toUser(): User
    {
        val user = User()
        user.id = id
        user.username = username
        user.name = name
        user.email = email
        user.status = Status.valueOf(status)
        return user
    }

    companion object {
        @JvmStatic
        fun fromUser(user: User): UserDto = UserDto(
            id = user.id,
            username = user.username,
            name = user.name,
            email = user.email,
            status = user.status.name
        )
    }
}