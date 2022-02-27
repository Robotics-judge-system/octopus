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

class CompetitionControllerTest : TestWithDb() {

    private var token = ""

    @Autowired
    private lateinit var clock: Clock

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun fixClock() {
        (clock as TestClock).setFixed(
            Instant.parse("2021-09-01T00:00:00.00Z"),
            ZoneOffset.UTC
        )
        token = getJwtTokenForDefaultUser()
    }

    @Test
    @DatabaseSetup("/db/auth/user.xml")
    @DisplayName("testing of competitionCreate")
    fun createCompetitionTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/create"
            )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\n" +
                            "        \"name\":\"competition_name\"\n" +
                            "}"
                )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content()
                    .json( ResourceReader.getResource("json/controllers/team/createCompetitionTest.json"))
            )
    }

    @Test
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
        ]
    )
    @ExpectedDatabase(
        "/db/rest/CompetitionControllerTest/renamed_competiton.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @DisplayName("testing of competitionUpdate")
    fun updateCompetitionTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/update/name"
            )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\n" +
                            "        \"name\":\"new_name\"\n" +
                            "}"
                )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content()
                    .json( ResourceReader.getResource("json/controllers/team/updateCompetitionTest.json") )
            )
    }

    @Test
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/auth/another_user.xml",
            "/db/rest/CompetitionControllerTest/many_users_and_competitions.xml",
        ]
    )
    @DisplayName("testing of get all")
    fun allMethodTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content()
                    .json( ResourceReader.getResource("json/controllers/team/allMethodTest.json"))
            )
    }

    @Test
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/auth/another_user.xml",
            "/db/rest/CompetitionControllerTest/many_users_and_competitions.xml",
        ]
    )
    @DisplayName("testing of get one")
    fun getOneMethodTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content()
                    .json( ResourceReader.getResource("json/controllers/team/getOneMethodTest.json"))
            )
    }

    @Test
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/auth/another_user.xml",
            "/db/rest/CompetitionControllerTest/many_users_and_competitions.xml",
        ]
    )
    @ExpectedDatabase(
        "/db/rest/CompetitionControllerTest/one_deleted.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    @DisplayName("delete method test")
    fun deleteMethodTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete(
                "/api/v1/competition/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(ResourceReader.getResource("json/controllers/team/deleteMethodTest.json"))
            )
    }
}
