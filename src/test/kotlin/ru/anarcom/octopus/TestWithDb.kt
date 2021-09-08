package ru.anarcom.octopus

import com.github.springtestdbunit.DbUnitTestExecutionListener
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.support.DirtiesContextTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import org.springframework.transaction.annotation.Transactional

@AutoConfigureEmbeddedDatabase(
	provider = DatabaseProvider.DEFAULT,
	refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD
)
@RunWith(
	SpringRunner::class
)
@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@Transactional
@TestExecutionListeners(
	DependencyInjectionTestExecutionListener::class,
	DirtiesContextTestExecutionListener::class,
	TransactionalTestExecutionListener::class,
	DbUnitTestExecutionListener::class,
)
@AutoConfigureMockMvc
class TestWithDb
