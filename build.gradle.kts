plugins {
    id("java")

    id("de.eldoria.plugin-yml.bukkit") version "0.7.1"
    id("com.gradleup.shadow") version "8.3.1"
}

group = "dev.scarday"
version = "2.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
    maven("https://repo.panda-lang.org/releases/")
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    implementation("com.squareup.okhttp3:okhttp:5.1.0")

    implementation("eu.okaeri:okaeri-configs-serdes-bukkit:5.0.9")
    implementation("eu.okaeri:okaeri-configs-yaml-bukkit:5.0.9")

    implementation("dev.rollczi:litecommands-bukkit:3.10.4")
    implementation("dev.rollczi:litecommands-adventure-platform:3.10.4")

    implementation("net.kyori:adventure-platform-bukkit:4.4.1")
    implementation("net.kyori:adventure-text-minimessage:4.24.0")

    compileOnly("org.projectlombok:lombok:1.18.30")
    compileOnly("org.jetbrains:annotations:26.0.2")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.shadowJar {
    archiveBaseName.set(rootProject.name)
    archiveClassifier.set("")

    relocate("net.kyori", "$group.buyer.libs.kyori")
    relocate("com.squareup", "$group.buyer.libs.squareup")
    relocate("dev.rollczi", "$group.buyer.libs.rollczi")
    relocate("eu.okaeri", "$group.buyer.libs.okaeri")
}

bukkit {
    main = "$group.buyer.Main"
    apiVersion = "1.16"
}