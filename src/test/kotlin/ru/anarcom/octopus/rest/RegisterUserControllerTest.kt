package ru.anarcom.octopus.rest

import com.github.springtestdbunit.annotation.DatabaseSetup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.anarcom.octopus.OctopusApplicationTests
import java.time.Clock

class RegisterUserControllerTest : OctopusApplicationTests() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var clock: Clock

    @BeforeEach
    fun fixClock() {
    }

    @Test
    @DisplayName("Registration user test")
    @DatabaseSetup("/db/auth/user.xml")
//    @ExpectedDatabase(
//        value = "/db/registration/after_registration.xml",
//        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
//    )
    // TODO: CLOCK FIX
    fun registrationUserTest() {
        val result: MvcResult = mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/v1/register")
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
            .andReturn()
    }
}