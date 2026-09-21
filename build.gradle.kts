plugins {
    java
}

group = "com.community"
version = "1.0.0"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.runelite.net") }
}

dependencies {
    // Explicitly import the latest stable RuneLite api and client packages
    compileOnly("net.runelite:runelite-api:1.10.33")
    compileOnly("net.runelite:client:1.10.33")
    
    // Lombok annotation processor setup for clean logging compilation
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// Keep our jar-wrapper generator task active at the bottom
tasks.wrapper {
    gradleVersion = "8.5"
    distributionType = Wrapper.DistributionType.BIN
}
