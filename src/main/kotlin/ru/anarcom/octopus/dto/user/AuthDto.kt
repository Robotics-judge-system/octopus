package ru.anarcom.octopus.dto.user

import ru.anarcom.octopus.entity.Auth
import java.time.Clock
import java.time.Instant

data class AuthDto(
    var id: Long = 0,
    var created: Instant = Clock.systemDefaultZone().instant(),
    var updated: Instant = Clock.systemDefaultZone().instant(),
) {
    companion object {
        fun fromInstant(auth: Auth): AuthDto =
            AuthDto(
                id = auth.id,
                created = auth.created,
                updated = auth.updated
            )

        fun fromInstant(auths:List<Auth>):List<AuthDto> =
            auths.map {
                fromInstant(it)
            }
    }
}
