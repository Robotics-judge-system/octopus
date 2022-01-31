package ru.anarcom.octopus.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Clock
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "teams")
class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    var category: Category?=null,

    @Column(name = "team_name")
    var teamName: String = "",

    @CreatedDate
    @Column(name = "created")
    var created: Instant = Clock.systemDefaultZone().instant(),

    @LastModifiedDate
    @Column(name = "updated")
    var updated: Instant = Clock.systemDefaultZone().instant(),

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Status = Status.NONE,

    //TODO: выяснить, почему поле оказывается пустым.
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    var participants: MutableList<Participant> = mutableListOf(),
){
    fun getAllActiveParticipants() = participants.filter {
        it.status != Status.DELETED && it.status != Status.NOT_ACTIVE
    }
}
