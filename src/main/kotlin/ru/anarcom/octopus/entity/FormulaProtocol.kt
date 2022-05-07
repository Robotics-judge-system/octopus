package ru.anarcom.octopus.entity

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import ru.anarcom.octopus.entity.protocol.ProtocolFieldDescription
import java.time.Clock
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "formulas_protocols")
@TypeDefs(
    TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
)
class FormulaProtocol(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

/*
TODO в таблице уже есть поля formula_description и protocol_description
    */

    @Column(name = "protocol_description")
    @Type(type = "jsonb")
    var protocolDescription:List<ProtocolFieldDescription>,

    @Column(name = "name")
    var name: String = "",

    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: Category,

    @CreatedDate
    @Column(name = "created")
    var created: Instant = Clock.systemDefaultZone().instant(),

    @LastModifiedDate
    @Column(name = "updated")
    var updated: Instant = Clock.systemDefaultZone().instant(),

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Status = Status.NONE,
)
