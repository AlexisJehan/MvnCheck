plugins {
	`java-library`
}

repositories {
	mavenCentral()
	maven {
		name = "google"
		url = uri("https://maven.google.com")
	}
	ivy {
		url = uri("https://ivy.example.com")
	}
}

dependencies {
	implementation("com.google.android.material:material:1.0.0")
	implementation("com.google.guava:guava:10.0")
	implementation("com.google.guava:guava:23.1-jre")
	implementation("com.google.guava:guava:23.1-android")
	implementation("org.springframework:spring-core:3.0.0.RELEASE")
}