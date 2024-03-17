import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"

    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

group = "com.academy.fourtk"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {

    // Detekt
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.5")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.compileKotlin { }

// sourceSets{ getByName()}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Detekt> {
    description = "Runs detekt static analisys."
    setSource(files("src/main/kotlin"))
    config.setFrom(files("config/detekt/detekt.yml"))
    debug = true
    autoCorrect = true
    finalizedBy("ktlintCheck")
    reports {
        html.required.set(true)
        html.outputLocation.set(file("build/reports/detekt.html"))
    }
}
// tasks.withType<Test> {
// 	useJUnitPlatform()
// 	dependsOn("ktlintCheck, detekt")
// }
