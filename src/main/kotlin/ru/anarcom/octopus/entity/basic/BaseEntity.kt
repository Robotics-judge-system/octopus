package ru.anarcom.octopus.entity.basic

import lombok.experimental.Accessors
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*

@MappedSuperclass
@Accessors(chain = true)
open class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @Enumerated(EnumType.STRING)
    var status: EntityStatus = EntityStatus.NOT_ACTIVE

    @CreatedDate
    val createdAt: Date? = null

    @LastModifiedDate
    val updatedAt: Date? = null
}