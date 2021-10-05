package ru.anarcom.octopus.entity


import org.hibernate.Hibernate
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "competitions")
class Competition(
    @Column(name = "name")
    var name: String = "",

    @Column(name = "date_from")
    var dateFrom: Instant? = null,

    @Column(name = "date_to")
    var dateTo: Instant? = null,

    @ManyToOne
    var user: User? = null
) : BaseEntity() {

    fun getUserOrThrow() = user!!

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Competition

        return id == other.id
    }

    override fun hashCode(): Int = 0

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id ," +
                " created = $created ," +
                " updated = $updated ," +
                " status = $status ," +
                " name = $name ," +
                " dateFrom = $dateFrom ," +
                " dateTo = $dateTo ," +
                " user = $user )"
    }
}
