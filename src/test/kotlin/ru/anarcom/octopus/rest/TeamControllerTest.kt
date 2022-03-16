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

class TeamControllerTest : TestWithDb() {
    @Autowired
    private lateinit var clock: Clock

    @Autowired
    private lateinit var mockMvc: MockMvc

    private var token = ""

    @BeforeEach
    fun fixClock() {
        (clock as TestClock).setFixed(
            Instant.parse("2022-01-29T00:00:00.00Z"),
            ZoneOffset.UTC
        )
        token = getJwtTokenForDefaultUser()
    }

    @Test
    @DisplayName("Register team")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/team-participant/after/register.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun normalTeamRegister() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/team/register/"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\"team_name\": \"hello robot\"," +
                            "\"participants\":[" +
                            "{\"name\":\"Ivan Ivanov\",\"team_role\":\"COACH\"}," +
                            "{\"name\":\"Семен семенов\",\"team_role\":\"PARTICIPANT\"}," +
                            "{\"name\":\"Oleg g.\",\"team_role\":\"PARTICIPANT\"}" +
                            "]}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json( ResourceReader.getResource("json/controllers/team/normalTeamRegister.json"))
            )
    }

    @Test
    @DisplayName("Get all teams (normal)")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/team-participant/before/three-teams.xml"
        ]
    )
    @ExpectedDatabase(
        value = "/db/team-participant/before/three-teams.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun getAllTeamsNormalTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1/category/11/team/"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json( ResourceReader.getResource("json/controllers/team/getAllTeamsNormalTest.json"))
            )
    }

    @Test
    @DisplayName("show one normal team")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/team-participant/before/three-teams.xml"
        ]
    )
    @ExpectedDatabase(
        value = "/db/team-participant/before/three-teams.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun getOneNotDeletedTeamTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1/category/11/team/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json( ResourceReader.getResource("json/controllers/team/getOneNotDeletedTeamTest.json"))
            )
    }

    @Test
    @DisplayName("show one deleted team")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/team-participant/before/three-teams.xml"
        ]
    )
    @ExpectedDatabase(
        value = "/db/team-participant/before/three-teams.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun getOneDeletedTeamTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1/category/11/team/3"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(ResourceReader.getResource("json/controllers/team/getOneDeletedTeamTest.json"))
            )
    }

    @Test
    @DisplayName("Delete team")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/team-participant/before/three-teams.xml"
        ]
    )
    @ExpectedDatabase(
        value = "/db/team-participant/after/three-teams-one-deleted.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun deleteTeamTest() {
        (clock as TestClock).setFixed(
            Instant.parse("2022-01-29T09:00:00.00Z"),
            ZoneOffset.UTC
        )
        mockMvc.perform(
            MockMvcRequestBuilders.delete(
                "/api/v1/competition/1/category/11/team/2"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json( ResourceReader.getResource("json/controllers/team/deleteTeamTest.json")

                    )
            )
    }

    @Test
    @DisplayName("Delete (already deleted) team")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/team-participant/before/three-teams.xml"
        ]
    )
    @ExpectedDatabase(
        "/db/team-participant/before/three-teams.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun deleteAlreadyDeletedTeamTest() {
        (clock as TestClock).setFixed(
            Instant.parse("2022-01-29T09:00:00.00Z"),
            ZoneOffset.UTC
        )
        mockMvc.perform(
            MockMvcRequestBuilders.delete(
                "/api/v1/competition/1/category/11/team/3"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource("json/controllers/team/alreadyDeletedTest.json")
                    )
            )
    }

    @Test
    @DisplayName("Update team_name")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/team-participant/before/three-teams.xml"
        ]
    )
    @ExpectedDatabase(
        "/db/team-participant/after/teams-with-one-new-name.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun updateTest() {
        (clock as TestClock).setFixed(
            Instant.parse("2022-01-29T10:00:00.00Z"),
            ZoneOffset.UTC
        )
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/team/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\"team_name\": \"hello robot_2\"}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource("json/controllers/team/teamAfterNormalUpdateTest.json"
                        )
                    )
            )
    }

    @Test
    @DisplayName("Update team_name (to old team_name)")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/team-participant/before/three-teams.xml"
        ]
    )
    @ExpectedDatabase(
        "/db/team-participant/before/three-teams.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun updateToOldTeamNameTest() {
        (clock as TestClock).setFixed(
            Instant.parse("2022-01-29T10:00:00.00Z"),
            ZoneOffset.UTC
        )
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/team/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\"team_name\": \"hello robot\"}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource("json/controllers/team/notChangedTeamNameTest.json"
                        )
                    )
            )
    }

    @Test
    @DisplayName("Update team_name (with deleted status)")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/team-participant/before/three-teams.xml"
        ]
    )
    @ExpectedDatabase(
        "/db/team-participant/before/three-teams.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun updateNameOfDeletedTeamTest(){
        (clock as TestClock).setFixed(
            Instant.parse("2022-01-29T10:00:00.00Z"),
            ZoneOffset.UTC
        )
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/team/3"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\"team_name\": \"hello robot\"}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource("json/controllers/team/updateNameOfDeletedTeamTest.json"
                        )
                    )
            )
    }
}
