val rabbitmq_java_version: String by project
val fluentj_version: String by project
val junit_version: String by project

plugins {
    java
}

allprojects {
    apply(plugin = "java")
    repositories {
        mavenCentral()
    }

    version = "0.1-SNAPSHOT"

    dependencies {
        implementation("com.rabbitmq:amqp-client:$rabbitmq_java_version")

        testImplementation("org.junit.jupiter:junit-jupiter-api:${junit_version}")
        testImplementation("org.assertj:assertj-core:${fluentj_version}")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
            showStandardStreams = false
            events("passed", "skipped", "failed")
        }

    }
}

project(":Producer") {
    dependencies {
    }
}

project(":Consumer") {
    dependencies {

    }
}
