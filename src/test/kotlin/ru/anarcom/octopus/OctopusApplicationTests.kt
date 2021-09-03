package ru.anarcom.octopus

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner
import ru.anarcom.octopus.repository.UserRepository

@AutoConfigureEmbeddedDatabase(
	provider = DatabaseProvider.ZONKY,
	refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD
)
@RunWith(
	SpringRunner::class
)
@DataJpaTest
class OctopusApplicationTests {

	@Autowired
	private lateinit var userRepository: UserRepository

	@Test
	fun contextLoads() {
		println(userRepository.getById(1).toString())
	}

}
