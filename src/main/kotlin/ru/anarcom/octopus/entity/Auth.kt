package ru.anarcom.octopus.entity

import ru.anarcom.octopus.model.BaseEntity
import ru.anarcom.octopus.model.User
import javax.persistence.*

@Entity
@Table(name = "auths")
data class Auth(

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,

    @Column(name = "refresh_token", unique = true)
    var refreshToken:String = "",
) : BaseEntity()
