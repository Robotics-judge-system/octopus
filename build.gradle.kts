import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.21"
	kotlin("plugin.spring") version "1.5.21"
	kotlin("plugin.jpa") version "1.3.72"
}

allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.MappedSuperclass")
	annotation("javax.persistence.Embeddable")
}

group = "ru.anarcom"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security:2.6.4")
	implementation("org.springframework.boot:spring-boot-starter-web:2.6.4")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.6.4")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("javax.validation:validation-api:2.0.1.Final")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.4")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("org.apache.commons:commons-lang3:3.12.0")
	implementation("org.liquibase:liquibase-core:4.8.0")
	implementation("com.vladmihalcea:hibernate-types-52:2.14.0")

	compileOnly("org.projectlombok:lombok:1.18.22")
	runtimeOnly("org.postgresql:postgresql:42.3.3")
	annotationProcessor("org.projectlombok:lombok:1.18.22")
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.4")
	testImplementation("org.springframework.security:spring-security-test:5.6.2")

	testImplementation("io.zonky.test:embedded-database-spring-test:2.1.1")
	testImplementation("io.zonky.test:embedded-postgres:1.3.1")

	testImplementation("org.mockito:mockito-all:1.10.19")
	testImplementation("com.github.springtestdbunit:spring-test-dbunit:1.3.0")
	testImplementation("org.dbunit:dbunit:2.7.3")
	testImplementation("org.springframework.boot:spring-boot-starter-web:2.6.4")
	implementation("com.google.guava:guava:31.1-jre")

	testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
	testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
	testImplementation("org.junit.platform:junit-platform-commons:1.8.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}