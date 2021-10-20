package ru.anarcom.octopus.entity

import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "categories")
class Category(
    @Id
    @Column(name = "id")
    var id: Long = 0,

    @ManyToOne(optional = false)
    @JoinColumn(name = "competition_id")
    var competition: Competition,

    @Column(name = "name")
    var name: String,

    @Column(name = "date_from")
    var dateFrom: Instant,

    @Column(name = "date_to")
    var dateTo: Instant,

    @Column(name = "created")
    var created: Instant,

    @Column(name = "updated")
    var updated: Instant,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Status = Status.NONE,
)
