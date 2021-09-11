package ru.anarcom.octopus.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant
import javax.persistence.*

/**
 * Base class for Entity with ID, created and updated fields.
 */
@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @CreatedDate
    @Column(name = "created")
    var created: Instant? = null

    @LastModifiedDate
    @Column(name = "updated")
    var updated: Instant? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Status? = null
}
