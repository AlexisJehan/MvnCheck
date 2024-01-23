/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Alexis Jehan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.alexisjehan.mvncheck.core.component.build.resolver;

import com.github.alexisjehan.mvncheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mvncheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mvncheck.core.component.artifact.type.GradleArtifactType;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFile;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFileType;
import com.github.alexisjehan.mvncheck.core.component.repository.Repository;
import com.github.alexisjehan.mvncheck.core.component.repository.RepositoryType;
import com.github.alexisjehan.mvncheck.core.util.GradleUtils;
import org.gradle.tooling.GradleConnector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisabledIfEnvironmentVariable(named = "CI", matches = "true")
final class GradleBuildResolverIT {

	private final GradleBuildResolver gradleBuildResolver = new GradleBuildResolver();

	@Test
	void testUpToDate() {
		assertThat(GradleUtils.VERSION).startsWith("8.5"); // Ensure tests are up-to-date after updating Gradle
	}

	@ParameterizedTest
	@EnabledForJreRange(max = JRE.JAVA_12)
	@ValueSource(strings = {"4.8", "4.9", "4.10", "5.0", "5.1", "5.2", "5.3", "5.4", "5.5", "5.6"})
	void testResolveUntilGradle67UntilJava12(final String gradleVersion, @TempDir final Path tmpDirectory) {
		testResolveUntilGradle67(gradleVersion, tmpDirectory);
	}

	@ParameterizedTest
	@EnabledForJreRange(max = JRE.JAVA_13)
	@ValueSource(strings = {"6.0", "6.1", "6.2"})
	void testResolveUntilGradle67UntilJava13(final String gradleVersion, @TempDir final Path tmpDirectory) {
		testResolveUntilGradle67(gradleVersion, tmpDirectory);
	}

	@ParameterizedTest
	@EnabledForJreRange(max = JRE.JAVA_15)
	@ValueSource(strings = {"6.3", "6.4", "6.5", "6.6", "6.7"})
	void testResolveUntilGradle67UntilJava15(final String gradleVersion, @TempDir final Path tmpDirectory) {
		testResolveUntilGradle67(gradleVersion, tmpDirectory);
	}

	@ParameterizedTest
	@EnabledForJreRange(max = JRE.JAVA_15)
	@ValueSource(strings = {"6.8", "6.9"})
	void testResolveSinceGradle68UntilJava15(final String gradleVersion, @TempDir final Path tmpDirectory) {
		testResolveSinceGradle68(gradleVersion, tmpDirectory);
	}

	@ParameterizedTest
	@EnabledForJreRange(max = JRE.JAVA_16)
	@ValueSource(strings = {"7.0", "7.1"})
	void testResolveSinceGradle68UntilJava16(final String gradleVersion, @TempDir final Path tmpDirectory) {
		testResolveSinceGradle68(gradleVersion, tmpDirectory);
	}

	@ParameterizedTest
	@EnabledForJreRange(max = JRE.JAVA_17)
	@ValueSource(strings = "7.2")
	void testResolveSinceGradle68UntilJava17(final String gradleVersion, @TempDir final Path tmpDirectory) {
		testResolveSinceGradle68(gradleVersion, tmpDirectory);
	}

	@ParameterizedTest
	@EnabledForJreRange(max = JRE.JAVA_18)
	@ValueSource(strings = {"7.3", "7.4", "7.5"})
	void testResolveSinceGradle68UntilJava18(final String gradleVersion, @TempDir final Path tmpDirectory) {
		testResolveSinceGradle68(gradleVersion, tmpDirectory);
	}

	@ParameterizedTest
	@EnabledForJreRange(max = JRE.JAVA_19)
	@ValueSource(strings = {"7.6", "8.0"})
	void testResolveSinceGradle68UntilJava19(final String gradleVersion, @TempDir final Path tmpDirectory) {
		testResolveSinceGradle68(gradleVersion, tmpDirectory);
	}

	@ParameterizedTest
	@EnabledForJreRange(max = JRE.JAVA_20)
	@ValueSource(strings = "8.1")
	void testResolveSinceGradle68UntilJava20(final String gradleVersion, @TempDir final Path tmpDirectory) {
		testResolveSinceGradle68(gradleVersion, tmpDirectory);
	}

	@ParameterizedTest
	@ValueSource(strings = {"8.2", "8.3", "8.4", "8.5"})
	void testResolveSinceGradle68UntilJavaLatest(final String gradleVersion, @TempDir final Path tmpDirectory) {
		testResolveSinceGradle68(gradleVersion, tmpDirectory);
	}

	private void testResolveUntilGradle67(final String gradleVersion, final Path tmpDirectory) {
		try (var mockedStaticGradleUtils = Mockito.mockStatic(GradleUtils.class)) {
			mockedStaticGradleUtils.when(GradleUtils::retrieveOptionalHome)
					.thenReturn(Optional.empty());
			assertThat(BuildFileType.GRADLE_GROOVY).satisfies(buildFileType -> {
				final var connector = GradleConnector.newConnector()
						.useGradleVersion(gradleVersion);
				try (var mockedStaticGradleConnector = Mockito.mockStatic(GradleConnector.class)) {
					mockedStaticGradleConnector.when(GradleConnector::newConnector)
							.thenReturn(connector);
					final var tmpBuildDirectory = tmpDirectory.resolve("groovy");
					Files.createDirectory(tmpBuildDirectory);
					final var tmpBuildFile = tmpBuildDirectory.resolve("build.gradle");
					final var tmpSettingsFile = tmpBuildDirectory.resolve("settings.gradle");
					Files.copy(
							Path.of("src", "test", "resources", "build_it.gradle"),
							tmpBuildFile,
							StandardCopyOption.REPLACE_EXISTING
					);
					Files.copy(
							Path.of("src", "test", "resources", "settings_it_until-6.7.gradle"),
							tmpSettingsFile,
							StandardCopyOption.REPLACE_EXISTING
					);
					final var buildFile = new BuildFile(buildFileType, tmpBuildFile);
					final var build = gradleBuildResolver.resolve(buildFile);
					assertThat(build.getFile()).isSameAs(buildFile);
					assertThat(build.getRepositories()).containsExactly(
							new Repository(
									RepositoryType.NORMAL,
									"MavenRepo",
									"https://repo.maven.apache.org/maven2/"
							),
							new Repository(
									RepositoryType.NORMAL,
									"google",
									"https://maven.google.com"
							),
							new Repository(
									RepositoryType.PLUGIN,
									"maven",
									"https://plugin-management.example.com"
							)
					);
					assertThat(build.getArtifacts()).containsExactly(
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.android.material", "material"),
									"1.0.0"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.guava", "guava"),
									"10.0"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.guava", "guava"),
									"23.1-jre"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.guava", "guava"),
									"23.1-android"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("org.springframework", "spring-core"),
									"3.0.0.RELEASE"
							)
					);
				}
			});
			assertThat(BuildFileType.GRADLE_KOTLIN).satisfies(buildFileType -> {
				final var connector = GradleConnector.newConnector()
						.useGradleVersion(gradleVersion);
				try (var mockedStaticGradleConnector = Mockito.mockStatic(GradleConnector.class)) {
					mockedStaticGradleConnector.when(GradleConnector::newConnector)
							.thenReturn(connector);
					final var tmpBuildDirectory = tmpDirectory.resolve("kotlin");
					Files.createDirectory(tmpBuildDirectory);
					final var tmpBuildFile = tmpBuildDirectory.resolve("build.gradle.kts");
					final var tmpSettingsFile = tmpBuildDirectory.resolve("settings.gradle.kts");
					Files.copy(
							Path.of("src", "test", "resources", "build_it.gradle.kts"),
							tmpBuildFile,
							StandardCopyOption.REPLACE_EXISTING
					);
					Files.copy(
							Path.of("src", "test", "resources", "settings_it_until-6.7.gradle.kts"),
							tmpSettingsFile,
							StandardCopyOption.REPLACE_EXISTING
					);
					final var buildFile = new BuildFile(buildFileType, tmpBuildFile);
					final var build = gradleBuildResolver.resolve(buildFile);
					assertThat(build.getFile()).isSameAs(buildFile);
					assertThat(build.getRepositories()).containsExactly(
							new Repository(
									RepositoryType.NORMAL,
									"MavenRepo",
									"https://repo.maven.apache.org/maven2/"
							),
							new Repository(
									RepositoryType.NORMAL,
									"google",
									"https://maven.google.com"
							),
							new Repository(
									RepositoryType.PLUGIN,
									"maven",
									"https://plugin-management.example.com"
							)
					);
					assertThat(build.getArtifacts()).containsExactly(
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.android.material", "material"),
									"1.0.0"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.guava", "guava"),
									"10.0"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.guava", "guava"),
									"23.1-jre"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.guava", "guava"),
									"23.1-android"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("org.springframework", "spring-core"),
									"3.0.0.RELEASE"
							)
					);
				}
			});
		}
	}

	private void testResolveSinceGradle68(final String gradleVersion, final Path tmpDirectory) {
		try (var mockedStaticGradleUtils = Mockito.mockStatic(GradleUtils.class)) {
			mockedStaticGradleUtils.when(GradleUtils::retrieveOptionalHome)
					.thenReturn(Optional.empty());
			assertThat(BuildFileType.GRADLE_GROOVY).satisfies(buildFileType -> {
				final var connector = GradleConnector.newConnector()
						.useGradleVersion(gradleVersion);
				try (var mockedStaticGradleConnector = Mockito.mockStatic(GradleConnector.class)) {
					mockedStaticGradleConnector.when(GradleConnector::newConnector)
							.thenReturn(connector);
					final var tmpBuildDirectory = tmpDirectory.resolve("groovy");
					Files.createDirectory(tmpBuildDirectory);
					final var tmpBuildFile = tmpBuildDirectory.resolve("build.gradle");
					final var tmpSettingsFile = tmpBuildDirectory.resolve("settings.gradle");
					Files.copy(
							Path.of("src", "test", "resources", "build_it.gradle"),
							tmpBuildFile,
							StandardCopyOption.REPLACE_EXISTING
					);
					Files.copy(
							Path.of("src", "test", "resources", "settings_it_since-6.8.gradle"),
							tmpSettingsFile,
							StandardCopyOption.REPLACE_EXISTING
					);
					final var buildFile = new BuildFile(buildFileType, tmpBuildFile);
					final var build = gradleBuildResolver.resolve(buildFile);
					assertThat(build.getFile()).isSameAs(buildFile);
					assertThat(build.getRepositories()).containsExactly(
							new Repository(
									RepositoryType.NORMAL,
									"maven",
									"https://dependency-resolution-management.example.com"
							),
							new Repository(
									RepositoryType.NORMAL,
									"MavenRepo",
									"https://repo.maven.apache.org/maven2/"
							),
							new Repository(
									RepositoryType.NORMAL,
									"google",
									"https://maven.google.com"
							),
							new Repository(
									RepositoryType.PLUGIN,
									"maven",
									"https://plugin-management.example.com"
							)
					);
					assertThat(build.getArtifacts()).containsExactly(
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.android.material", "material"),
									"1.0.0"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.guava", "guava"),
									"10.0"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.guava", "guava"),
									"23.1-jre"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.guava", "guava"),
									"23.1-android"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("org.springframework", "spring-core"),
									"3.0.0.RELEASE"
							)
					);
				}
			});
			assertThat(BuildFileType.GRADLE_KOTLIN).satisfies(buildFileType -> {
				final var connector = GradleConnector.newConnector()
						.useGradleVersion(gradleVersion);
				try (var mockedStaticGradleConnector = Mockito.mockStatic(GradleConnector.class)) {
					mockedStaticGradleConnector.when(GradleConnector::newConnector)
							.thenReturn(connector);
					final var tmpBuildDirectory = tmpDirectory.resolve("kotlin");
					Files.createDirectory(tmpBuildDirectory);
					final var tmpBuildFile = tmpBuildDirectory.resolve("build.gradle.kts");
					final var tmpSettingsFile = tmpBuildDirectory.resolve("settings.gradle.kts");
					Files.copy(
							Path.of("src", "test", "resources", "build_it.gradle.kts"),
							tmpBuildFile,
							StandardCopyOption.REPLACE_EXISTING
					);
					Files.copy(
							Path.of("src", "test", "resources", "settings_it_since-6.8.gradle.kts"),
							tmpSettingsFile,
							StandardCopyOption.REPLACE_EXISTING
					);
					final var buildFile = new BuildFile(buildFileType, tmpBuildFile);
					final var build = gradleBuildResolver.resolve(buildFile);
					assertThat(build.getFile()).isSameAs(buildFile);
					assertThat(build.getRepositories()).containsExactly(
							new Repository(
									RepositoryType.NORMAL,
									"maven",
									"https://dependency-resolution-management.example.com"
							),
							new Repository(
									RepositoryType.NORMAL,
									"MavenRepo",
									"https://repo.maven.apache.org/maven2/"
							),
							new Repository(
									RepositoryType.NORMAL,
									"google",
									"https://maven.google.com"
							),
							new Repository(
									RepositoryType.PLUGIN,
									"maven",
									"https://plugin-management.example.com"
							)
					);
					assertThat(build.getArtifacts()).containsExactly(
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.android.material", "material"),
									"1.0.0"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.guava", "guava"),
									"10.0"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.guava", "guava"),
									"23.1-jre"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("com.google.guava", "guava"),
									"23.1-android"
							),
							new Artifact<>(
									GradleArtifactType.COMPILE_ONLY,
									new ArtifactIdentifier("org.springframework", "spring-core"),
									"3.0.0.RELEASE"
							)
					);
				}
			});
		}
	}
}