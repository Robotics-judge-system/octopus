package ru.anarcom.octopus.entity.basic

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*

@MappedSuperclass
open class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @Enumerated(EnumType.STRING)
    var status: EntityStatus = EntityStatus.NOT_ACTIVE

    @CreatedDate
    val createdAt: Date = Date()

    @LastModifiedDate
    val updatedAt: Date = Date()
}