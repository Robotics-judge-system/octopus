package ru.anarcom.octopus.entity

import org.hibernate.Hibernate
import ru.anarcom.octopus.entity.basic.BaseEntity
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.Email

/*
CREATE TABLE users
(
    id             bigserial primary key,
    name           varchar(50)                         not null,
    email          varchar(20)                         not null,
    created_at     timestamp default CURRENT_TIMESTAMP not null,
    updated_at     timestamp default CURRENT_TIMESTAMP not null,
    status         varchar(10)                         not null,
    password_hash  varchar(255)                        not null,
    last_online_at timestamp default CURRENT_TIMESTAMP not null
);
*/
@Entity
@Table(name = "users")
data class User(
    @Column(name = "name")
    val name: String? = null,
    val email: @Email String? = null,
    val passwordHash: String? = null,
    val lastOnlineAt: Date? = null,
) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(
                other
            )
        ) return false
        other as User

        return id != null && id == other.id
    }

    override fun hashCode(): Int = 562048007

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name , " +
                "email = $email , passwordHash = $passwordHash , " +
                "lastOnlineAt = $lastOnlineAt , status = $status , " +
                "createdAt = $createdAt , updatedAt = $updatedAt )"
    }
}