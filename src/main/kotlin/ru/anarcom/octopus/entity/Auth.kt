package ru.anarcom.octopus.entity

import org.hibernate.Hibernate
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

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    @Column(name = "refresh_token", unique = true)
    var refreshToken:String = "",
) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(
                other
            )
        ) return false
        other as Auth

        return id == other.id
    }

    override fun hashCode(): Int = 1057601825

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , created = $created , updated = $updated , status = $status , user = $user , refreshToken = $refreshToken )"
    }
}
