plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.13.3"
}

group = "dev.rollczi"
version = "2.8.7-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.eternalcode.pl/releases")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.1")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("com.intellij.java"))
}

dependencies {
    implementation("dev.rollczi:litecommands-core:3.0.0-BETA-pre10")
    implementation("dev.rollczi:litecommands-core-annotations:3.0.0-BETA-pre10")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild.set("223")
        untilBuild.set("231.*")
    }

    signPlugin {
        certificateChain.set(providers.environmentVariable("CERTIFICATE_CHAIN"))
        privateKey.set(providers.environmentVariable("PRIVATE_KEY"))
        password.set(providers.environmentVariable("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(providers.environmentVariable("PUBLISH_TOKEN"))
    }

    runIde {
        autoReloadPlugins.set(true)

    }

    buildSearchableOptions {
        enabled = false
    }
}
