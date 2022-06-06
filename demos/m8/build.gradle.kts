val jackson_version: String by project
val rabbitmq_java_version: String by project
val logback_version: String by project
val javalin_version: String by project

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
        implementation("com.fasterxml.jackson.core:jackson-databind:$jackson_version")
        implementation("com.rabbitmq:amqp-client:$rabbitmq_java_version")
        implementation("ch.qos.logback:logback-classic:$logback_version")
    }
}

project(":QuoteService") {
    dependencies {
    }
}

project(":RegisterUserConsolePollConsumer") {
    dependencies {
        implementation(project(":QuoteService"))
    }
}

project(":RegisterUserConsolePullConsumer") {
    dependencies {
        implementation(project(":QuoteService"))
    }
}

project(":RegisterUserConsoleRoutingKeyConsumer") {
    dependencies {
        implementation(project(":QuoteService"))
    }
}

project(":RpcConsoleConsumer") {
    dependencies {
        implementation(project(":QuoteService"))
    }
}

project(":FanOutPubSubConsumer") {
    dependencies {
        implementation(project(":QuoteService"))
    }
}

project(":TopicConsoleConsumer") {
    dependencies {
        implementation(project(":QuoteService"))
    }
}


project(":ApiClient") {
    dependencies {
        implementation(project(":QuoteService"))
        implementation("io.javalin:javalin:4.4.0")
    }
}


