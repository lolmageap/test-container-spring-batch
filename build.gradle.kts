import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.allopen") version "1.9.20"
    kotlin("plugin.noarg") version "1.9.20"
}

group = "kotlin.batch"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core:10.15.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.flywaydb:flyway-database-postgresql:10.15.0")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.batch:spring-batch-core:5.1.2")
    implementation("org.springframework.batch:spring-batch-test:5.1.2")

    testRuntimeOnly("org.postgresql:postgresql")
    implementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:jdbc:1.20.2")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.jeasy:easy-random-core:5.0.0")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("io.kotest:kotest-runner-junit5:5.7.2")
    testImplementation("io.kotest:kotest-assertions-core:5.7.2")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:2.0.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

allOpen {
    annotation("cherhy.batch.settlement.annotation.Table")
}

noArg {
    annotation("cherhy.batch.settlement.annotation.Table")
}