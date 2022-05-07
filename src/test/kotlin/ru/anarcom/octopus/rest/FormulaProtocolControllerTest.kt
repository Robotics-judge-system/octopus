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

class FormulaProtocolControllerTest : TestWithDb() {
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
    @DisplayName("Get all formulaProtocols")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/FormulaProtocolController/before/some_formulas.xml"
        ]
    )
    fun getAllTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1/category/13/formula-protocol"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource(
                            "json/controllers/formulaProtocol/getAllFormulas.json"
                        )
                    )
            )
    }

    @Test
    @DisplayName("Get one formulaProtocol")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/FormulaProtocolController/before/some_formulas.xml"
        ]
    )
    fun getOneTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1/category/13/formula-protocol/2"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource(
                            "json/controllers/formulaProtocol/getOneFormula.json"
                        )
                    )
            )
    }

    @Test
    @DisplayName("Delete one formula")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/FormulaProtocolController/before/some_formulas.xml"
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/FormulaProtocolController/after/after_deleting.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun deleteTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete(
                "/api/v1/competition/1/category/13/formula-protocol/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource(
                            "json/controllers/formulaProtocol/deleteOne.json"
                        )
                    )
            )
    }

    @Test
    @DisplayName("Delete already deleted formula")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/FormulaProtocolController/before/some_formulas.xml"
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/FormulaProtocolController/before/some_formulas.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun deleteAlreadyDeletedTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete(
                "/api/v1/competition/1/category/13/formula-protocol/3"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource(
                            "json/controllers/formulaProtocol/deleteAlreadyDeleted.json"
                        )
                    )
            )
    }

    @Test
    @DisplayName("Add new with validation")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml"
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/FormulaProtocolController/after/add_formula_protocol.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun addNewFormulaProtocolTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/13/formula-protocol"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    ResourceReader.getResource(
                        "json/controllers/formulaProtocol/request/createNewFormulaProtocol.json"
                    )
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource(
                            "json/controllers/formulaProtocol/addNewFormulaProtocol.json"
                        )
                    )
            )
    }

    @Test
    @DisplayName("Add new with error in validation")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml"
        ]
    )
    fun validationInFormulaProtocolTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/13/formula-protocol"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    ResourceReader.getResource(
                        "json/controllers/formulaProtocol/request/updateFormulaProtocolWithValidationError.json"
                    )
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        "{\"exception_message\":\"field 'name' should not be null\"}\n"
                    )
            )
    }

    @Test
    @DisplayName("Update formula and protocol")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/FormulaProtocolController/before/some_formulas.xml"
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/FormulaProtocolController/after/after_update.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun updateFormulaProtocolTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/13/formula-protocol/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    ResourceReader.getResource(
                        "json/controllers/formulaProtocol/request/updateFormulaProtocol.json"
                    )
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content()
                    .json(
                        ResourceReader.getResource(
                            "json/controllers/formulaProtocol/afterUpdate.json"
                        )
                    )
            )
    }
}
