plugins {
    idea
    jacoco
    `maven-publish`
    id("net.minecraftforge.gradle") version "[6.0.24,6.2)"
}

group = property("mod_group_id") as String
version = property("mod_version") as String
base { archivesName.set(property("artifact_name") as String) }
val gameTestFixtures = sourceSets.create("gameTestFixtures")

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withSourcesJar()
}

minecraft {
    mappings("official", property("minecraft_version") as String)
    copyIdeResources = true
    runs {
        configureEach {
            workingDirectory(project.file("run"))
            property("forge.logging.console.level", "info")
            property("forge.enabledGameTestNamespaces", property("mod_id") as String)
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", file("build/createSrgToMcp/output.srg").absolutePath)
            mods { create(property("mod_id") as String) { source(sourceSets.main.get()) } }
        }
        create("client")
        create("server") { arg("--nogui") }
        create("gameTestServer") {
            mods { named(project.property("mod_id") as String) { source(gameTestFixtures) } }
            workingDirectory(project.file("run-gametest"))
            property("forge.enableGameTest", "true")
            property("forge.gameTestServer", "true")
            arg("--nogui")
        }
    }
}

repositories {
    mavenCentral()
    maven("https://maven.minecraftforge.net")
    maven("https://www.cursemaven.com") { content { includeGroup("curse.maven") } }
}

dependencies {
    minecraft("net.minecraftforge:forge:${property("minecraft_version")}-${property("forge_version")}")
    implementation(fg.deobf("curse.maven:curios-api-309927:6418456"))
    compileOnly(fg.deobf("curse.maven:sodiumdynamiclights-551736:6044481"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.processResources {
    val props = mapOf(
        "minecraft_version" to project.property("minecraft_version"),
        "forge_version" to project.property("forge_version"),
        "mod_id" to project.property("mod_id"),
        "mod_name" to project.property("mod_name"),
        "mod_version" to project.property("mod_version"),
    )
    inputs.properties(props)
    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) { expand(props) }
}

tasks.named<Jar>("jar") { finalizedBy("reobfJar") }

val stageRuntimeJar by tasks.registering(Copy::class) {
    group = "build"
    dependsOn(tasks.named("reobfJar"))
    from(layout.buildDirectory.file("reobfJar/output.jar"))
    into(layout.buildDirectory.dir("libs"))
    rename { "${base.archivesName.get()}-$version.jar" }
}

tasks.named("assemble") { dependsOn(stageRuntimeJar) }
tasks.withType<JavaCompile>().configureEach { options.release.set(17) }
tasks.test { useJUnitPlatform() }

tasks.register("verifyFast") { dependsOn(tasks.named("test"), tasks.named("assemble")) }
tasks.register("verifyFull") { dependsOn(tasks.named("verifyFast"), tasks.named("runGameTestServer")) }

apply(from = "gradle/gametest-evidence.gradle")
