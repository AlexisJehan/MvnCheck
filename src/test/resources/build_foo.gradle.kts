plugins {
	`java-library`
}

repositories {
	mavenCentral()
	maven {
		name = "foo-repository-name"
		url = uri("https://foo-repository-host")
	}
	ivy {
		name = "bar-repository-name"
		url = uri("https://bar-repository-host")
	}
}

dependencies {
	annotationProcessor("foo-annotation-processor-group-id:foo-annotation-processor-artifact-id:foo-annotation-processor-version")
	api("foo-api-group-id:foo-api-artifact-id:foo-api-version")
	compileOnly("foo-compile-only-group-id:foo-compile-only-artifact-id:foo-compile-only-version")
	compileOnlyApi("foo-compile-only-api-group-id:foo-compile-only-api-artifact-id:foo-compile-only-api-version")
	implementation("foo-implementation-group-id:foo-implementation-artifact-id:foo-implementation-version")
	runtimeOnly("foo-runtime-only-group-id:foo-runtime-only-artifact-id:foo-runtime-only-version")
	testAnnotationProcessor("foo-test-annotation-processor-group-id:foo-test-annotation-processor-artifact-id:foo-test-annotation-processor-version")
	testCompileOnly("foo-test-compile-only-group-id:foo-test-compile-only-artifact-id:foo-test-compile-only-version")
	testImplementation("foo-test-implementation-group-id:foo-test-implementation-artifact-id:foo-test-implementation-version")
	testRuntimeOnly("foo-test-runtime-only-group-id:foo-test-runtime-only-artifact-id:foo-test-runtime-only-version")
}