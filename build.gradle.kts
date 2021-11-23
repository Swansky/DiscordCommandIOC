plugins {
    java
    `maven-publish`
    id("net.linguica.maven-settings") version "0.5"
}

group = "fr.swansky"
version = "1.0.10"

repositories {
    mavenCentral()
    maven("https://maven.zoltowski.fr/releases")
    maven("https://m2.dv8tion.net/releases")
    mavenLocal()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("fr.swansky:SwansAPI:1.0.4")
    implementation("net.dv8tion:JDA:4.3.0_334")
}

publishing {
    repositories {
        maven {
            name = "maven-zoltowski"
            url = uri("https://maven.zoltowski.fr/releases")
        }

    }
    publications {
        create<MavenPublication>("maven-zoltowski") {
            groupId = "fr.swansky"
            artifactId = "DiscordCommandIOC"
            version = this.version

            from(components["java"])
        }
    }
}

apply(plugin = "net.linguica.maven-settings")
