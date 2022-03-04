package ru.anarcom.octopus.rest

import com.github.springtestdbunit.annotation.DatabaseSetup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.anarcom.octopus.TestWithDb
import ru.anarcom.octopus.util.TestClock
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class AttemptResultControllerTest : TestWithDb() {
    @Autowired
    private lateinit var clock: Clock

    @Autowired
    private lateinit var mockMvc: MockMvc

    private var token = ""

    @BeforeEach
    fun fixClock() {
        (clock as TestClock).setFixed(
            Instant.parse("2022-03-03T00:00:00.00Z"),
            ZoneOffset.UTC
        )
        token = getJwtTokenForDefaultUser()
    }

    @Test
    @DisplayName("add attempt")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_and_formulas.xml",
            "/db/team-participant/before/three-teams.xml",
//            "/db/rest/AttemptResultControllerTest/test_attempt_result.xml",
        ]
    )
//    @ExpectedDatabase(
//        value = "/db/rest/AttemptControllerTest/after/after_new_creating.xml",
//        assertionMode = DatabaseAssertionMode.NON_STRICT,
//    )
    fun addAttempt() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/team/1/attempt/3"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{" +
                            "    \"a\":\"11\"," +
                            "    \"b\":\"12\"" +
                            "}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
    // TODO update attempt


}
