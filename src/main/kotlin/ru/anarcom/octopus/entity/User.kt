package ru.anarcom.octopus.entity

import org.hibernate.Hibernate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * Entity with User information.
 */
@Entity
@Table(name = "users")
data class User(
    @Column(name = "username")
    var username: String = "",

    @Column(name = "name")
    var name: String = "",

    @Column(name = "email")
    var email: String = "",

    @Column(name = "password")
    var password: String = "",

    @Transient
    var roles: List<Role> = ArrayList()
) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(
                other
            )
        ) return false
        other as User

        return id == other.id
    }

    override fun hashCode(): Int = 562048007

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id ," +
                " created = $created ," +
                " updated = $updated ," +
                " status = $status ," +
                " username = $username ," +
                " name = $name ," +
                " email = $email ," +
                " password = $password ," +
                " roles = $roles )"
    }
}
