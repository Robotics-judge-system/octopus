package ru.anarcom.octopus.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Clock
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "competitions")
class Competition(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "name")
    var name: String = "",

    @Column(name = "date_from")
    var dateFrom: Instant? = null,

    @Column(name = "date_to")
    var dateTo: Instant? = null,

    @ManyToOne
    var user: User? = null,

    @CreatedDate
    @Column(name = "created")
    var created: Instant = Clock.systemDefaultZone().instant(),

    @LastModifiedDate
    @Column(name = "updated")
    var updated: Instant = Clock.systemDefaultZone().instant(),

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Status = Status.NONE,
) {
    fun getUserOrThrow() = user!!
}
