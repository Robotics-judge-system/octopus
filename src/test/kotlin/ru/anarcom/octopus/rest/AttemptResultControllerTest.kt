package ru.anarcom.octopus.rest

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
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
import ru.anarcom.octopus.utilus.ResourceReader
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
            Instant.parse("2022-03-05T00:00:00.00Z"),
            ZoneOffset.UTC
        )
        token = getJwtTokenForDefaultUser()
    }

    @Test
    @DisplayName("add attempt result")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
//            "/db/rest/AttemptControllerTest/before/some_attempts_and_formulas.xml",
            "/db/rest/AttemptResultControllerTest/before/some_attempts_and_formulas_with_valid_descriptions.xml",
            "/db/team-participant/before/three-teams.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptResultControllerTest/after/add_new_attempt_result.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
    )
    fun addAttempt() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/team/1/attempt/3"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{" +
                            "\"Time of attempt\":1," +
                            "\"school\":12" +
                            "}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource(
                            "json/controllers/attemptResult/addAttempt.json"
                        )
                    )
            )
    }

    @Test
    @DisplayName("add attempt result to not active attempt")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_and_formulas.xml",
            "/db/team-participant/before/three-teams.xml",
        ]
    )
    fun addResultToNotActiveAttempt() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/team/1/attempt/2"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{" +
                            "\"a\":\"11\"," +
                            "\"b\":\"12\"" +
                            "}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().`is`(409))
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        "{\"exception_message\":\"attempt is not active\"}"
                    )
            )
    }

    @Test
    @DisplayName("update attempt")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_and_formulas.xml",
            "/db/team-participant/before/three-teams.xml",
            "/db/rest/AttemptResultControllerTest/before/one_attempt.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptResultControllerTest/after/one_attempt_after_updating.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
    )
    fun updateAttempt() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/team/1/attempt/3"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{" +
                            "\"Time of attempt\":1," +
                            "\"school\":12" +
                            "}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource(
                            "json/controllers/attemptResult/afterNormalUpdate.json"
                        )
                    )
            )
    }


    @Test
    @DisplayName("update not in attempt")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_and_formulas.xml",
            "/db/team-participant/before/three-teams.xml",
            "/db/rest/AttemptResultControllerTest/before/attempt_not_in.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptResultControllerTest/after/attempt_not_in_after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
    )
    fun updateNotInAttempt() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/team/1/attempt/3"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{" +
                            "\"Time of attempt\":1," +
                            "\"school\":12" +
                            "}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource(
                            "json/controllers/attemptResult/afterUpdating.json"
                        )
                    )
            )
    }

    @Test
    @DisplayName("delete attempt")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_and_formulas.xml",
            "/db/team-participant/before/three-teams.xml",
            "/db/rest/AttemptResultControllerTest/before/one_attempt.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptResultControllerTest/after/one_attempt_after_deleting.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
    )
    fun deleteAttempt() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete(
                "/api/v1/competition/1/category/11/team/1/attempt/3"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource(
                            "json/controllers/attemptResult/deleteAttempt.json"
                        )
                    )
            )
    }


    @Test
    @DisplayName("get not deleted attempts of team")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_and_formulas.xml",
            "/db/team-participant/before/three-teams.xml",
            "/db/rest/AttemptResultControllerTest/before/two_attempts.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptResultControllerTest/before/two_attempts.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
    )
    fun getAllNotDeletedAttemptsByTeam() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1/category/11/team/1/attempt"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource(
                            "json/controllers/attemptResult/getAllAttempts.json"
                        )
                    )
            )
    }


    @Test
    @DisplayName("get one attempts of team")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_and_formulas.xml",
            "/db/team-participant/before/three-teams.xml",
            "/db/rest/AttemptResultControllerTest/before/two_attempts.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptResultControllerTest/before/two_attempts.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED,
    )
    fun getAttemptByTeam() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1/category/11/team/1/attempt/3"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource(
                            "json/controllers/attemptResult/getAttemptByTeam.json"
                        )
                    )
            )
    }


//    TODO добавить тест для проверки, что не возвращается попытка со статусом DELETED
}
