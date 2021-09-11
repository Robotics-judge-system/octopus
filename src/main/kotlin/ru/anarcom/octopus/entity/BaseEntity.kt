package ru.anarcom.octopus.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Clock
import java.time.Instant
import javax.persistence.*

/**
 * Base class for Entity with ID, created and updated fields.
 */
@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @CreatedDate
    @Column(name = "created")
    var created: Instant = Clock.systemDefaultZone().instant()

    @LastModifiedDate
    @Column(name = "updated")
    var updated: Instant = Clock.systemDefaultZone().instant()

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Status = Status.NONE
}
