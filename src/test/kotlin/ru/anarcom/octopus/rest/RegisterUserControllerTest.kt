package ru.anarcom.octopus.rest

import com.github.springtestdbunit.annotation.DatabaseSetup
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.anarcom.octopus.TestWithDb
import ru.anarcom.octopus.entity.Status
import ru.anarcom.octopus.repository.UserRepository
import ru.anarcom.octopus.util.TestClock
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class RegisterUserControllerTest : TestWithDb() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var clock: Clock

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun fixClock() {
        (clock as TestClock).setFixed(
            Instant.parse("2021-09-01T00:00:00.00Z"),
            ZoneOffset.UTC
        )
    }

    @Test
    @DisplayName("Registration user test")
    @DatabaseSetup("/db/auth/user.xml")
    fun registrationUserTest() {
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/v1/register")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(
                        "{" +
                                "\"username\":\"username_1\"," +
                                "\"email\":\"email@email.email\"," +
                                "\"name\":\"Василий\"," +
                                "\"password\":\"password\"" +
                                "}"
                    )
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    "{\n" +
                            "    \"id\": 2,\n" +
                            "    \"username\": \"username_1\",\n" +
                            "    \"name\": \"Василий\",\n" +
                            "    \"email\": \"email@email.email\"\n" +
                            "}"
                )
            )
        assert(userRepository.count().toInt() == 2)

        val registeredUser = userRepository.getById(2)
        assertEquals( 2, registeredUser.id)
        assertEquals("email@email.email", registeredUser.email)
        assertEquals("Василий", registeredUser.name)
        assertEquals("username_1", registeredUser.username)
        assertEquals(registeredUser.created, registeredUser.updated)
        assertEquals(Instant.parse("2021-09-01T00:00:00.00Z"), registeredUser.updated)
        assertEquals(Status.ACTIVE,registeredUser.status)
    }
}
