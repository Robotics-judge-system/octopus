package ru.anarcom.octopus.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Clock
import java.time.Instant
import javax.persistence.*

/**
 * Entity with information about all refresh_tokens.
 * <p>
 *     In future where would be information about OS, and fingerprints.
 * </p>
 */
@Entity
@Table(name = "auths")
class Auth(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    @Column(name = "refresh_token", unique = true)
    var refreshToken: String = "",

    @CreatedDate
    @Column(name = "created")
    var created: Instant = Clock.systemDefaultZone().instant(),

    @LastModifiedDate
    @Column(name = "updated")
    var updated: Instant = Clock.systemDefaultZone().instant(),

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Status = Status.NONE,
)
