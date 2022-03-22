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

class AttemptControllerTest : TestWithDb() {
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
    @DisplayName("create new attempt (normal work)")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/after/after_new_creating.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun createNewNormalAttemptTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\"name\":\"new attempt\"}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    ResourceReader.getResource("json/controllers/attempt/createNewAttempt.json")
                )
            )
    }

    @Test
    @DisplayName("create new attempt (with exception)")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
        ]
    )
    fun addNewAttemptWithExceptionTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\"name\":\"\"}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    "{\"exception_message\":\"name should not be blank or empty\"}\n"
                )
            )
    }

    @Test
    @DisplayName("get all attempts for categories")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts.xml",
        ]
    )
    fun getAllAttemptsForCategoryTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1/category/11/attempt"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    ResourceReader.getResource("json/controllers/attempt/getAllAttempts.json")
                )
            )
    }

    @Test
    @DisplayName("get attempt by id")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts.xml",
        ]
    )
    fun getAttemptByIdTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1/category/11/attempt/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    ResourceReader.getResource("json/controllers/attempt/getOneAttemptById.json")
                )
            )
    }

    @Test
    @DisplayName("get deleted attempt by id")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts.xml",
        ]
    )
    fun getDeletedAttemptByIdTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/v1/competition/1/category/11/attempt/3"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    ResourceReader.getResource(
                        "json/controllers/attempt/getOneDeletedAttemptById.json"
                    )
                )
            )
    }
    // TODO добавить проверку на получение exception при попытке получить несуществующий attempt

    @Test
    @DisplayName("delete already deleted attempt")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/before/some_attempts.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun deleteAlreadyDeletedTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete(
                "/api/v1/competition/1/category/11/attempt/3"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    ResourceReader.getResource(
                        "json/controllers/attempt/getOneDeletedAttemptById.json"
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
            "/db/rest/AttemptControllerTest/before/some_attempts.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/after/some_attempts_one_deleted.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun deleteAttemptTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete(
                "/api/v1/competition/1/category/11/attempt/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    ResourceReader.getResource(
                        "json/controllers/attempt/deleteAttempt.json"
                    )
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
            "/db/rest/AttemptControllerTest/before/some_attempts.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/after/after_updating.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun updateAttemptTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\"name\":\"attempt 1_new\"}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    ResourceReader.getResource(
                        "json/controllers/attempt/afterAttemptNameUpdate.json"
                    )
                )
            )
    }

    @Test
    @DisplayName("not valid update")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/before/some_attempts.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun notValidUpdateAttemptTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    "{\"name\":\"\"}"
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    "{\"exception_message\":\"name should not be empty or blank\"}\n"
                )
            )
    }

    @Test
    @DisplayName("add formula-protocol to attempt")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_and_formulas.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/after/after_adding_formula_protocol_to_attempt.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun addFormulaProtocolToAttemptTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/1/attach/formula-protocol/1"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    ResourceReader.getResource(
                        "json/controllers/attempt/addFormulaProtocol.json"
                    )
                )
            )
    }

    @Test
    @DisplayName("add formula-protocol to attempt with deleted status")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_and_formulas.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/before/some_attempts_and_formulas.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun addFormulaProtocolToAttemptWithDeletedStatusTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/1/attach/formula-protocol/2"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    "{\"exception_message\":\"Formula-protocol is already deleted\"}\n"
                )
            )
    }

    @DisplayName("add formula-protocol to active attempt")
    @Test
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/one_attempt_and_two_formulas.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/before/one_attempt_and_two_formulas.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun attachToActiveAttemptTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/1/attach/formula-protocol/2"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    "{\"exception_message\":\"Attempt is active\"}"
                )
            )
    }

    @Test
    @DisplayName("normal attach null to formula")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_for_null_attaching.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/after/after_normal_null_attach.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun attachNullToFormulaProtocolTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/3/attach/formula-protocol/null"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    ResourceReader.getResource(
                        "json/controllers/attempt/afterNullConnect.json"
                    )
                )
            )
    }

    @Test
    @DisplayName("attach null to active attempt")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_for_null_attaching.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/before/some_attempts_for_null_attaching.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun attachNullToActiveAttemptTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/1/attach/formula-protocol/null"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    "{\"exception_message\":\"Attempt is active\"}"
                )
            )
    }

    @Test
    @DisplayName("attach null to deleted attempt")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_for_null_attaching.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/before/some_attempts_for_null_attaching.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun attachNullToAlreadyDeletedTest(){
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/2/attach/formula-protocol/null"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    "{\"exception_message\":\"Attempt is already deleted\"}"
                )
            )
    }

    @Test
    @DisplayName("attach null to attempt with null")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_for_null_attaching.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/before/some_attempts_for_null_attaching.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun attachNullToAttemptWithNullTest(){
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/4/attach/formula-protocol/null"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    ResourceReader.getResource(
                        "json/controllers/attempt/attachNullToAttemptWithNull.json"
                    )
                )
            )
    }

    @Test
    @DisplayName("normal attempt activation")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_and_formulas.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/after/after_normal_activation.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun normalAttemptActivationTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/2/activate"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    ResourceReader.getResource(
                        "json/controllers/attempt/normalAttemptActivation.json"
                    )
                )
            )
    }

    @Test
    @DisplayName("attempt activation with null in formula-protocol")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_and_formulas.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/before/some_attempts_and_formulas.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun nullInFormulaProtocolActivationTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/1/activate"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    "{\"exception_message\":\"formula-protocol is null\"}"
                )
            )
    }

    @Test
    @DisplayName("deleted attempt activation")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempt_for_activation.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/before/some_attempt_for_activation.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun activationOfDeletedAttemptTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/4/activate"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    "{\"exception_message\":\"attempt is deleted\"}"
                )
            )
    }
    @Test
    @DisplayName("normal deactivation")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempt_for_activation.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/after/after_deactivation.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun normalDeactivationTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/3/deactivate"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    ResourceReader.getResource(
                        "json/controllers/attempt/afterNormalDeactivation.json"
                    )
                )
            )
    }

    @Test
    @DisplayName("normal deactivation with connected deleted results")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_with_additional_logic_for_activation.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/after/after_attempt_with_deleted_data_deactivation.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun normalDeactivationWithDeletedResultsTest(){
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/1/deactivate"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    ResourceReader.getResource(
                        "/json/controllers/attempt/deactivationOfAttemptWithDeletedAttemptResult.json"
                    )
                )
            )
    }
    @Test
    @DisplayName("not normal deactivation with connected active results")
    @DatabaseSetup(
        value = [
            "/db/auth/user.xml",
            "/db/rest/CompetitionControllerTest/default_competition.xml",
            "/db/rest/CategoryControllerTest/some_categories.xml",
            "/db/rest/AttemptControllerTest/before/some_attempts_with_additional_logic_for_activation.xml",
        ]
    )
    @ExpectedDatabase(
        value = "/db/rest/AttemptControllerTest/before/some_attempts_with_additional_logic_for_activation.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT,
    )
    fun notNormalDeactivationWithDeletedResultsTest(){
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/v1/competition/1/category/11/attempt/2/deactivate"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    "{\"exception_message\":\"Attempt has not deleted results\"}"
                )
            )
    }
}
