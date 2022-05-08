package ru.anarcom.octopus.entity

import com.vladmihalcea.hibernate.type.array.IntArrayType
import com.vladmihalcea.hibernate.type.array.StringArrayType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Clock
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "attempts_results")
@TypeDefs(
    TypeDef(name = "string-array", typeClass = StringArrayType::class),
    TypeDef(name = "int-array", typeClass = IntArrayType::class),
    TypeDef(name = "json", typeClass = JsonStringType::class),
    TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
)
class AttemptResult(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @CreatedDate
    @Column(name = "created")
    var created: Instant = Clock.systemDefaultZone().instant(),

    @LastModifiedDate
    @Column(name = "updated")
    var updated: Instant = Clock.systemDefaultZone().instant(),

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Status = Status.NONE,

    @ManyToOne
    @JoinColumn(name = "team_id")
    var team: Team,

    @ManyToOne
    @JoinColumn(name = "formula_protocol_id")
    var formulaProtocol: FormulaProtocol,

    @ManyToOne
    @JoinColumn(name = "last_user_id")
    var user: User,

    @ManyToOne
    @JoinColumn(name = "attempt_id")
    var attempt: Attempt,

    @Enumerated(EnumType.STRING)
    @Column(name = "calculation_status")
    var calculationStatus: AttemptResultCalculationStatus =
        AttemptResultCalculationStatus.NOT_CALCULATED,

    @Column(name = "attempt_time")
    var attemptTime: Long?=null,

    @Column(name = "attempt_score")
    var attemptScore: Long?=null,

    @Type(type = "jsonb")
    @Column(name = "attempt_data")
    var attemptData: Map<String, Int>,
)
