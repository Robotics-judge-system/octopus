package ru.anarcom.octopus.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.anarcom.octopus.entity.FormulaProtocol

interface FormulaProtocolRepository : JpaRepository<FormulaProtocol, Long>
