import org.gradle.kotlin.dsl.support.kotlinCompilerOptions
import org.jetbrains.kotlin.config.JvmTarget

plugins {
    idea
    id("net.neoforged.moddev") version "1.0.0"
    kotlin("jvm") version "2.0.0"
}

val modId: String by extra
val minecraftVersion: String by extra
val minecraftVersionRange: String by extra
val neoforgeVersion: String by extra
val neoVersionRange: String by extra
val loaderVersionRange: String by extra
val modName: String by extra
val modVersion: String by extra
val modLicense: String by extra
val modAuthors: String by extra
val modDescription: String by extra

version = "1.0.0"
group = "example.example"
base.archivesName = modId

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
kotlin.compilerOptions.jvmTarget to JvmTarget.JVM_21
kotlin.jvmToolchain(21)

neoForge {
    version = neoforgeVersion

    runs {
        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }

        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        create("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        create("data") {
            data()
            programArguments.addAll("--mod", modId, "--all", "--output", file("src/generated/resources/").absolutePath, "--existing", file("src/main/resources/").absolutePath)
        }
    }

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main.get().resources.setSrcDirs(listOf("src/generated/resources", "src/main/resources/"))

repositories {
    mavenCentral()

    maven(url = "https://thedarkcolour.github.io/KotlinForForge/")
}

dependencies {
    implementation("thedarkcolour:kotlinforforge-neoforge:5.4.0")
}

tasks.withType<ProcessResources> {
    val replaceProperties = mapOf("minecraftVersion" to minecraftVersion, "modId" to modId, 
        "modName" to modName, "modLicense" to modLicense, "modVersion" to modVersion, "modAuthors" to modAuthors, 
        "modDescription" to modDescription, "loaderVersionRange" to loaderVersionRange, 
        "minecraftVersionRange" to minecraftVersionRange, "neoVersionRange" to neoVersionRange)
    
    inputs.properties(replaceProperties)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadJavadoc = true
    }
}