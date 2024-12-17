plugins {
    id("com.gradle.plugin-publish")
}

group = properties["group"].toString()
version = properties["version"].toString()

repositories {
    mavenCentral()
}

gradlePlugin {
    vcsUrl.set("https://github.com/rururu387/gradle-jinjava-plugin")
    website.set("https://github.com/rururu387/gradle-jinjava-plugin/blob/main/README.md")

    plugins {
        create("gradle-jinjava-plugin") {
            setId("io.github.rururu387.gradle-jinjava-plugin")
            setImplementationClass("io.github.rururu387.JinjavaPlugin")
            setDisplayName("Jinjava plugin")
            setDescription("A Gradle plugin designed for handling files formatted with Jinja2 templates. This plugin " +
                    "uses Jinjava as its template engine.")
            tags.set(listOf("jinja", "jinja2", "jinjava", "template engine", "template"))
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    compileOnly(gradleApi())

    implementation("com.hubspot.jinjava:jinjava:${properties["jinjavaVersion"]}")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${properties["snakeYamlVersion"]}")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-toml:${properties["jacksonVersion"]}")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-properties:${properties["jacksonVersion"]}")
    implementation("jakarta.annotation:jakarta.annotation-api:${properties["jakartaAnnotationApiVersion"]}")

    testImplementation(platform("org.junit:junit-bom:${properties["junitVersion"]}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Suppress javadoc plugin warnings
tasks {
    javadoc {
        options {
            (this as CoreJavadocOptions).addBooleanOption("Xdoclint:none", true)
        }
    }
}

tasks.withType(Jar::class).asSequence().forEach { it.dependsOn(tasks.getByName("javadoc")) }

tasks.test {
    useJUnitPlatform()
}