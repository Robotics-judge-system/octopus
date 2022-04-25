package ru.anarcom.octopus.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import org.hamcrest.Matchers.`is`
import org.junit.Ignore
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import ru.anarcom.octopus.TestWithDb
import ru.anarcom.octopus.util.TestClock
import ru.anarcom.octopus.utilus.ResourceReader
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset


class AuthControllerTest : TestWithDb() {

    @Autowired
    private lateinit var clock: Clock

    @BeforeEach
    fun setTime() {
        (clock as TestClock).setFixed(
            Instant.parse("2021-09-01T00:00:00.00Z"),
            ZoneOffset.UTC
        )
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("Login with correct login/password")
    @DatabaseSetup("/db/auth/user.xml")
    @ExpectedDatabase(
        value = "/db/auth/user.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun correctAuthAndSelfDataTest() {
        val token = getTokenAndValidateUsername(
            "username",
            "test",
            "username"
        )
        mockMvc
            .perform(
                get("/api/v1/self")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                content().json(
                    ResourceReader.getResource("json/controllers/team/correctAuthAndSelfDataTest.json")
                )
            )
    }

    @Test
    @DisplayName("Incorrect login and password")
    @DatabaseSetup("/db/auth/user.xml")
    fun loginWithIncorrectLoginTest() {
        mockMvc
            .perform(
                post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(
                        "{\n" +
                                "    \"username\":\"wrong_login\",\n" +
                                "    \"password\":\"test\"\n" +
                                "}"
                    )
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isForbidden)
            .andExpect(
                content().json(
                    ResourceReader.getResource("json/controllers/team/loginWithIncorrectLoginTest.json")
                )
            )
    }

    @Test
    @DisplayName("Correct refresh token")
    @DatabaseSetup("/db/auth/user.xml")
    @ExpectedDatabase(
        value = "/db/auth/user.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun refreshTokenTest() {
        val result: MvcResult = mockMvc
            .perform(
                post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(
                        "{\n" +
                                "    \"login\":\"username\",\n" +
                                "    \"password\":\"test\"\n" +
                                "}"
                    )
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username", `is`("username")))
            .andReturn()
        val respData: MutableMap<*, *>? = ObjectMapper()
            .readValue(
                result.response.contentAsString,
                MutableMap::class.java
            )
        val token = respData?.get("token")
        val refreshToken = respData?.get("refresh_token")
        mockMvc
            .perform(
                get("/api/v1/self")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                content().json(
                    ResourceReader.getResource("json/controllers/team/refreshTokenTest.json")
                )
            )
        mockMvc
            .perform(
                post("/api/v1/auth/refresh")
                    .content(
                        "{\n" +
                                "    \"refresh\":\"$refreshToken\"\n" +
                                "}"
                    )
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.username", `is`("username")))
            .andExpect(jsonPath("$.refresh_token", `is`(refreshToken)))
    }

    @Test
    @DisplayName("incorrect refresh token")
    @DatabaseSetup("/db/auth/user.xml")
    fun incorrectRefreshTokenTest() {
        mockMvc
            .perform(
                post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(
                        "{\n" +
                                "    \"login\":\"username\",\n" +
                                "    \"password\":\"test\"\n" +
                                "}"
                    )
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username", `is`("username")))

        val refreshToken = "exactly bad token"
        mockMvc
            .perform(
                post("/api/v1/auth/refresh")
                    .content(
                        "{\n" +
                                "    \"refresh\":\"$refreshToken\"\n" +
                                "}"
                    )
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNotFound)
            .andExpect(
                content().json(
                    "{\"exception_message\":\"User with that refresh token not found\"}"
                )
            )
    }

    @Test
    @DisplayName("Auth as deleted user")
    @DatabaseSetup("/db/auth/deleted_user.xml")
    fun authDeletedUserTest(){
        mockMvc
            .perform(
                post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(
                        "{\n" +
                                "    \"login\":\"username\",\n" +
                                "    \"password\":\"test\"\n" +
                                "}"
                    )
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isForbidden)
            .andExpect(content().json("{\"exception_message\":\"invalid username or password\"}"))
    }

    @Test
    @DisplayName("Get all auths")
    @DatabaseSetup(
        value = ["/db/auth/user.xml", "/db/auth/auths.xml"]
    )
    @Ignore
    /*
    TODO: проблема в том, что в бд уже лежат данные об авторизации - надо будет это как-то поправить
     */
    fun getAllAuthsTest(){
//        val token = getJwtTokenForDefaultUser()
//        mockMvc
//            .perform(
//                get("/api/v1/auth/auth")
//                    .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
//            )
//            .andDo(MockMvcResultHandlers.print())
//            .andExpect(status().isOk)
//            .andExpect(
//                content().json(
//                    ResourceReader.getResource("json/auth/get_all_auths.json")
//                )
//            )
    }
}
