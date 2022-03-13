package ru.anarcom.octopus.rest

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import ru.anarcom.octopus.TestWithDb
import ru.anarcom.octopus.repo.UserRepository
import ru.anarcom.octopus.util.TestClock
import ru.anarcom.octopus.utilus.ResourceReader
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class UserChangeDataTest:TestWithDb() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var clock: Clock

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    @BeforeEach
    fun fixClock() {
        (clock as TestClock).setFixed(
            Instant.parse("2021-09-01T00:00:00.00Z"),
            ZoneOffset.UTC
        )
    }

    @Test
    @DisplayName("change name")
    @DatabaseSetup("/db/auth/user.xml")
    @ExpectedDatabase(
        "/db/change_user_data/after_name_changing.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun changeUserData(){
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/v1/user/change/data")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer ${
                        getTokenAndValidateUsername("username", "test", "username")
                    }")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(
                        "{\n" +
                                "    \"name\": \"new name1\"\n" +
                                "}"
                    )
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                content().json( ResourceReader.getResource("json/controllers/team/changeUserData.json"))
            )
    }

    @Test
    @DisplayName("change password")
    @DatabaseSetup("/db/auth/user.xml")
    fun changePasswordTest() {
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/v1/user/change/password")
                    .header(
                        HttpHeaders.AUTHORIZATION, "Bearer ${
                            getTokenAndValidateUsername("username", "test", "username")
                        }"
                    )
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(
                        "{\n" +
                                "    \"old_password\":\"test\"," +
                                "    \"new_password\":\"paswd\" " +
                                "}"
                    )
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                content().json(ResourceReader.getResource("json/controllers/team/changePasswordTest.json")
                )
            )

        assertTrue(
            passwordEncoder.matches(
                "paswd",
                userRepository.getById(1L).password
            )
        )
    }

    @Test
    @DisplayName("Mistake in old password")
    @DatabaseSetup("/db/auth/user.xml")
    @ExpectedDatabase(
        value = "/db/auth/user.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    fun incorrectPasswordToChangeTest() {
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/v1/user/change/password")
                    .header(
                        HttpHeaders.AUTHORIZATION, "Bearer ${
                            getTokenAndValidateUsername("username", "test", "username")
                        }"
                    )
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(
                        "{\n" +
                                "    \"old_password\":\"tet\"," +
                                "    \"new_password\":\"passwd\" " +
                                "}"
                    )
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isForbidden)
            .andExpect(
                content().json(
                    ResourceReader.getResource("json/controllers/team/incorrectPasswordToChangeTest.json")
                )
            )
    }


}
