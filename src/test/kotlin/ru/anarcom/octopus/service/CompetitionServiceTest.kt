package ru.anarcom.octopus.service

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import ru.anarcom.octopus.TestWithDb

class CompetitionServiceTest : TestWithDb() {

    @Autowired
    private lateinit var competitionService: CompetitionService

    @Test
    @DisplayName("Test for .delete method")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/service/competition-test/before/one_competition.xml"
        ]
    )
    @ExpectedDatabase(
        "/db/auth/user.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun deleteMethodTest() {
        competitionService.deleteById(1)
    }
}