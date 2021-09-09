package ru.anarcom.octopus.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * Entity with User information.
 */
@Entity
@Table(name = "users")
class User : BaseEntity() {
    @Column(name = "username")
    var username: String = ""

    @Column(name = "name")
    var name: String = ""

    @Column(name = "email")
    var email: String = ""

    @Column(name = "password")
    var password: String = ""

    @Transient
    var roles: List<Role> = ArrayList()
}
