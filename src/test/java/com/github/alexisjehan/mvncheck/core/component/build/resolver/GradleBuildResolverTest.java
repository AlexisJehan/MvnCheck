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
import org.gradle.tooling.GradleConnectionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class GradleBuildResolverTest {

	private final GradleBuildResolver gradleBuildResolver = new GradleBuildResolver();

	@Test
	void testResolve(@TempDir final Path tmpDirectory) {
		assertThat(BuildFileType.GRADLE_GROOVY).satisfies(buildFileType -> {
			final var tmpBuildDirectory = tmpDirectory.resolve("groovy");
			Files.createDirectory(tmpBuildDirectory);
			final var tmpBuildFile = tmpBuildDirectory.resolve("build.gradle");
			Files.copy(
					Path.of("src", "test", "resources", "build_foo.gradle"),
					tmpBuildFile,
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
							"foo-repository-name",
							"https://foo-repository-host"
					)
			);
			assertThat(build.getArtifacts()).containsExactly(
					new Artifact<>(
							GradleArtifactType.ANNOTATION_PROCESSOR,
							new ArtifactIdentifier(
									"foo-annotation-processor-group-id",
									"foo-annotation-processor-artifact-id"
							),
							"foo-annotation-processor-version"
					),
					new Artifact<>(
							GradleArtifactType.API,
							new ArtifactIdentifier(
									"foo-api-group-id",
									"foo-api-artifact-id"
							),
							"foo-api-version"
					),
					new Artifact<>(
							GradleArtifactType.COMPILE_ONLY,
							new ArtifactIdentifier(
									"foo-compile-only-group-id",
									"foo-compile-only-artifact-id"
							),
							"foo-compile-only-version"
					),
					new Artifact<>(
							GradleArtifactType.COMPILE_ONLY_API,
							new ArtifactIdentifier(
									"foo-compile-only-api-group-id",
									"foo-compile-only-api-artifact-id"
							),
							"foo-compile-only-api-version"
					),
					new Artifact<>(
							GradleArtifactType.IMPLEMENTATION,
							new ArtifactIdentifier(
									"foo-implementation-group-id",
									"foo-implementation-artifact-id"
							),
							"foo-implementation-version"
					),
					new Artifact<>(
							GradleArtifactType.RUNTIME_ONLY,
							new ArtifactIdentifier(
									"foo-runtime-only-group-id",
									"foo-runtime-only-artifact-id"
							),
							"foo-runtime-only-version"
					),
					new Artifact<>(
							GradleArtifactType.TEST_ANNOTATION_PROCESSOR,
							new ArtifactIdentifier(
									"foo-test-annotation-processor-group-id",
									"foo-test-annotation-processor-artifact-id"
							),
							"foo-test-annotation-processor-version"
					),
					new Artifact<>(
							GradleArtifactType.TEST_COMPILE_ONLY,
							new ArtifactIdentifier(
									"foo-test-compile-only-group-id",
									"foo-test-compile-only-artifact-id"
							),
							"foo-test-compile-only-version"
					),
					new Artifact<>(
							GradleArtifactType.TEST_IMPLEMENTATION,
							new ArtifactIdentifier(
									"foo-test-implementation-group-id",
									"foo-test-implementation-artifact-id"
							),
							"foo-test-implementation-version"
					),
					new Artifact<>(
							GradleArtifactType.TEST_RUNTIME_ONLY,
							new ArtifactIdentifier(
									"foo-test-runtime-only-group-id",
									"foo-test-runtime-only-artifact-id"
							),
							"foo-test-runtime-only-version"
					)
			);
		});
		assertThat(BuildFileType.GRADLE_KOTLIN).satisfies(buildFileType -> {
			final var tmpBuildDirectory = tmpDirectory.resolve("kotlin");
			Files.createDirectory(tmpBuildDirectory);
			final var tmpBuildFile = tmpBuildDirectory.resolve("build.gradle.kts");
			Files.copy(
					Path.of("src", "test", "resources", "build_foo.gradle.kts"),
					tmpBuildFile,
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
							"foo-repository-name",
							"https://foo-repository-host"
					)
			);
			assertThat(build.getArtifacts()).containsExactly(
					new Artifact<>(
							GradleArtifactType.ANNOTATION_PROCESSOR,
							new ArtifactIdentifier(
									"foo-annotation-processor-group-id",
									"foo-annotation-processor-artifact-id"
							),
							"foo-annotation-processor-version"
					),
					new Artifact<>(
							GradleArtifactType.API,
							new ArtifactIdentifier(
									"foo-api-group-id",
									"foo-api-artifact-id"
							),
							"foo-api-version"
					),
					new Artifact<>(
							GradleArtifactType.COMPILE_ONLY,
							new ArtifactIdentifier(
									"foo-compile-only-group-id",
									"foo-compile-only-artifact-id"
							),
							"foo-compile-only-version"
					),
					new Artifact<>(
							GradleArtifactType.COMPILE_ONLY_API,
							new ArtifactIdentifier(
									"foo-compile-only-api-group-id",
									"foo-compile-only-api-artifact-id"
							),
							"foo-compile-only-api-version"
					),
					new Artifact<>(
							GradleArtifactType.IMPLEMENTATION,
							new ArtifactIdentifier(
									"foo-implementation-group-id",
									"foo-implementation-artifact-id"
							),
							"foo-implementation-version"
					),
					new Artifact<>(
							GradleArtifactType.RUNTIME_ONLY,
							new ArtifactIdentifier(
									"foo-runtime-only-group-id",
									"foo-runtime-only-artifact-id"
							),
							"foo-runtime-only-version"
					),
					new Artifact<>(
							GradleArtifactType.TEST_ANNOTATION_PROCESSOR,
							new ArtifactIdentifier(
									"foo-test-annotation-processor-group-id",
									"foo-test-annotation-processor-artifact-id"
							),
							"foo-test-annotation-processor-version"
					),
					new Artifact<>(
							GradleArtifactType.TEST_COMPILE_ONLY,
							new ArtifactIdentifier(
									"foo-test-compile-only-group-id",
									"foo-test-compile-only-artifact-id"
							),
							"foo-test-compile-only-version"
					),
					new Artifact<>(
							GradleArtifactType.TEST_IMPLEMENTATION,
							new ArtifactIdentifier(
									"foo-test-implementation-group-id",
									"foo-test-implementation-artifact-id"
							),
							"foo-test-implementation-version"
					),
					new Artifact<>(
							GradleArtifactType.TEST_RUNTIME_ONLY,
							new ArtifactIdentifier(
									"foo-test-runtime-only-group-id",
									"foo-test-runtime-only-artifact-id"
							),
							"foo-test-runtime-only-version"
					)
			);
		});
	}

	@Test
	void testResolveInvalid(@TempDir final Path tmpDirectory) {
		assertThatNullPointerException().isThrownBy(() -> gradleBuildResolver.resolve(null));
		try (var mockedStaticGradleUtils = Mockito.mockStatic(GradleUtils.class)) {
			mockedStaticGradleUtils.when(GradleUtils::retrieveOptionalHome)
					.thenReturn(Optional.of("directory_not-found"));
			assertThat(BuildFileType.GRADLE_GROOVY).satisfies(buildFileType -> {
				final var tmpBuildDirectory = tmpDirectory.resolve("groovy");
				Files.createDirectory(tmpBuildDirectory);
				assertThat(tmpBuildDirectory.resolve("build_not-found.gradle")).satisfies(tmpBuildFile -> {
					final var buildFile = new BuildFile(buildFileType, tmpBuildFile);
					assertThatExceptionOfType(BuildResolveException.class)
							.isThrownBy(() -> gradleBuildResolver.resolve(buildFile))
							.withCauseInstanceOf(GradleConnectionException.class);
				});
				assertThat(tmpBuildDirectory.resolve("build.gradle")).satisfies(tmpBuildFile -> {
					final var buildFile = new BuildFile(buildFileType, tmpBuildFile);
					Files.copy(
							Path.of("src", "test", "resources", "build_error.gradle"),
							tmpBuildFile,
							StandardCopyOption.REPLACE_EXISTING
					);
					assertThatExceptionOfType(BuildResolveException.class)
							.isThrownBy(() -> gradleBuildResolver.resolve(buildFile))
							.withCauseInstanceOf(GradleConnectionException.class);
					Files.copy(
							Path.of("src", "test", "resources", "build_foo.gradle"),
							tmpBuildFile,
							StandardCopyOption.REPLACE_EXISTING
					);
					assertThatExceptionOfType(BuildResolveException.class)
							.isThrownBy(() -> gradleBuildResolver.resolve(buildFile))
							.withCauseInstanceOf(GradleConnectionException.class);
				});
			});
			assertThat(BuildFileType.GRADLE_KOTLIN).satisfies(buildFileType -> {
				final var tmpBuildDirectory = tmpDirectory.resolve("kotlin");
				Files.createDirectory(tmpBuildDirectory);
				assertThat(tmpBuildDirectory.resolve("build_not-found.gradle.kts")).satisfies(tmpBuildFile -> {
					final var buildFile = new BuildFile(buildFileType, tmpBuildFile);
					assertThatExceptionOfType(BuildResolveException.class)
							.isThrownBy(() -> gradleBuildResolver.resolve(buildFile))
							.withCauseInstanceOf(GradleConnectionException.class);
				});
				assertThat(tmpBuildDirectory.resolve("build.gradle.kts")).satisfies(tmpBuildFile -> {
					final var buildFile = new BuildFile(buildFileType, tmpBuildFile);
					Files.copy(
							Path.of("src", "test", "resources", "build_error.gradle.kts"),
							tmpBuildFile,
							StandardCopyOption.REPLACE_EXISTING
					);
					assertThatExceptionOfType(BuildResolveException.class)
							.isThrownBy(() -> gradleBuildResolver.resolve(buildFile))
							.withCauseInstanceOf(GradleConnectionException.class);
					Files.copy(
							Path.of("src", "test", "resources", "build_foo.gradle.kts"),
							tmpBuildFile,
							StandardCopyOption.REPLACE_EXISTING
					);
					assertThatExceptionOfType(BuildResolveException.class)
							.isThrownBy(() -> gradleBuildResolver.resolve(buildFile))
							.withCauseInstanceOf(GradleConnectionException.class);
				});
			});
		}
	}

	@Test
	void testGetFileTypes() {
		assertThat(gradleBuildResolver.getFileTypes()).contains(
				BuildFileType.GRADLE_GROOVY,
				BuildFileType.GRADLE_KOTLIN
		);
	}

	@Test
	void testParseRepositories() throws IOException {
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"\n"
						)
				)
		) {
			assertThat(GradleBuildResolver.parseRepositories(bufferedReader)).isEmpty();
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :repositories\n"
										+ "NORMAL:foo-repository-name:https://foo-repository-host\n"
										+ "PLUGIN:foo-plugin-repository-name:https://foo-plugin-repository-host"
						)
				)
		) {
			assertThat(GradleBuildResolver.parseRepositories(bufferedReader)).containsExactly(
					new Repository(
							RepositoryType.NORMAL,
							"foo-repository-name",
							"https://foo-repository-host"
					),
					new Repository(
							RepositoryType.PLUGIN,
							"foo-plugin-repository-name",
							"https://foo-plugin-repository-host"
					)
			);
		}
	}

	@Test
	void testParseRepositoriesInvalid() throws IOException {
		assertThatNullPointerException()
				.isThrownBy(() -> GradleBuildResolver.parseRepositories(null));
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :repositories\n"
										+ "NORMAL:foo-repository-name"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseRepositories(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :repositories\n"
										+ "UNKNOWN:foo-repository-name:https://foo-repository-host"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseRepositories(bufferedReader));
		}
	}

	@Test
	void testParseArtifacts() throws IOException {
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"\n"
						)
				)
		) {
			assertThat(GradleBuildResolver.parseArtifacts(bufferedReader)).isEmpty();
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project\n" // Until Gradle 6.7
										+ "------------------------------------------------------------\n"
										+ "\n"
										+ "api - API dependencies for source set 'main'. (n)"
						)
				)
		) {
			assertThat(GradleBuildResolver.parseArtifacts(bufferedReader)).isEmpty();
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project 'foo-project'\n" // Since Gradle 6.8
										+ "------------------------------------------------------------\n"
										+ "\n"
										+ "api - API dependencies for source set 'main'. (n)"
						)
				)
		) {
			assertThat(GradleBuildResolver.parseArtifacts(bufferedReader)).isEmpty();
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project\n" // Until Gradle 6.7
										+ "------------------------------------------------------------\n"
										+ "\n"
										+ "api - API dependencies for source set 'main'. (n)\n"
										+ "No dependencies\n"
										+ "\n"
										+ "apiElements - API elements for main. (n)"
						)
				)
		) {
			assertThat(GradleBuildResolver.parseArtifacts(bufferedReader)).isEmpty();
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project 'foo-project'\n" // Since Gradle 6.8
										+ "------------------------------------------------------------\n"
										+ "\n"
										+ "api - API dependencies for source set 'main'. (n)\n"
										+ "No dependencies\n"
										+ "\n"
										+ "apiElements - API elements for main. (n)"
						)
				)
		) {
			assertThat(GradleBuildResolver.parseArtifacts(bufferedReader)).isEmpty();
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project\n" // Until Gradle 6.7
										+ "------------------------------------------------------------\n"
										+ "\n"
										+ "api - API dependencies for source set 'main'. (n)\n"
										+ "No dependencies\n"
										+ "\n"
										+ "apiElements - API elements for main. (n)\n"
										+ "No dependencies\n"
										+ "\n"
										+ "compileOnly - Compile only dependencies for source set 'main'.\n"
										+ "+--- foo-compile-only-group-id:foo-compile-only-artifact-id FAILED\n"
										+ "|    \\--- bar-compile-only-group-id:bar-compile-only-artifact-id FAILED\n"
										+ "+--- foo-compile-only-group-id:foo-compile-only-artifact-id:"
										+ "foo-compile-only-version (n)\n"
										+ "|    \\--- bar-compile-only-group-id:bar-compile-only-artifact-id:"
										+ "bar-compile-only-version (n)\n"
										+ "+--- foo-compile-only-group-id:foo-compile-only-artifact-id -> "
										+ "foo-compile-only-resolved-version\n"
										+ "|    \\--- bar-compile-only-group-id:bar-compile-only-artifact-id -> "
										+ "bar-compile-only-resolved-version\n"
										+ "\\--- foo-compile-only-group-id:foo-compile-only-artifact-id:"
										+ "foo-compile-only-version -> foo-compile-only-resolved-version (c)\n"
										+ "     \\--- bar-compile-only-group-id:bar-compile-only-artifact-id:"
										+ "bar-compile-only-version -> bar-compile-only-resolved-version (c)\n"
										+ "\n"
										+ "(c) - dependency constraint\n"
										+ "\n"
										+ "(n) - Not resolved (configuration is not meant to be resolved)\n"
										+ "\n"
										+ "A web-based, searchable dependency report is available by adding the --scan "
										+ "option."
						)
				)
		) {
			assertThat(GradleBuildResolver.parseArtifacts(bufferedReader)).containsExactly(
					new Artifact<>(
							GradleArtifactType.COMPILE_ONLY,
							new ArtifactIdentifier(
									"foo-compile-only-group-id",
									"foo-compile-only-artifact-id"
							)
					),
					new Artifact<>(
							GradleArtifactType.COMPILE_ONLY,
							new ArtifactIdentifier(
									"foo-compile-only-group-id",
									"foo-compile-only-artifact-id"
							),
							"foo-compile-only-version"
					),
					new Artifact<>(
							GradleArtifactType.COMPILE_ONLY,
							new ArtifactIdentifier(
									"foo-compile-only-group-id",
									"foo-compile-only-artifact-id"
							),
							"foo-compile-only-resolved-version"
					),
					new Artifact<>(
							GradleArtifactType.COMPILE_ONLY,
							new ArtifactIdentifier(
									"foo-compile-only-group-id",
									"foo-compile-only-artifact-id"
							),
							"foo-compile-only-version"
					)
			);
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project 'foo-project'\n" // Since Gradle 6.8
										+ "------------------------------------------------------------\n"
										+ "\n"
										+ "api - API dependencies for source set 'main'. (n)\n"
										+ "No dependencies\n"
										+ "\n"
										+ "apiElements - API elements for main. (n)\n"
										+ "No dependencies\n"
										+ "\n"
										+ "compileOnly - Compile only dependencies for source set 'main'.\n"
										+ "+--- foo-compile-only-group-id:foo-compile-only-artifact-id FAILED\n"
										+ "|    \\--- bar-compile-only-group-id:bar-compile-only-artifact-id FAILED\n"
										+ "+--- foo-compile-only-group-id:foo-compile-only-artifact-id:"
										+ "foo-compile-only-version (n)\n"
										+ "|    \\--- bar-compile-only-group-id:bar-compile-only-artifact-id:"
										+ "bar-compile-only-version (n)\n"
										+ "+--- foo-compile-only-group-id:foo-compile-only-artifact-id -> "
										+ "foo-compile-only-resolved-version\n"
										+ "|    \\--- bar-compile-only-group-id:bar-compile-only-artifact-id -> "
										+ "bar-compile-only-resolved-version\n"
										+ "\\--- foo-compile-only-group-id:foo-compile-only-artifact-id:"
										+ "foo-compile-only-version -> foo-compile-only-resolved-version (c)\n"
										+ "     \\--- bar-compile-only-group-id:bar-compile-only-artifact-id:"
										+ "bar-compile-only-version -> bar-compile-only-resolved-version (c)\n"
										+ "\n"
										+ "(c) - dependency constraint\n"
										+ "\n"
										+ "(n) - Not resolved (configuration is not meant to be resolved)\n"
										+ "\n"
										+ "A web-based, searchable dependency report is available by adding the --scan "
										+ "option."
						)
				)
		) {
			assertThat(GradleBuildResolver.parseArtifacts(bufferedReader)).containsExactly(
					new Artifact<>(
							GradleArtifactType.COMPILE_ONLY,
							new ArtifactIdentifier(
									"foo-compile-only-group-id",
									"foo-compile-only-artifact-id"
							)
					),
					new Artifact<>(
							GradleArtifactType.COMPILE_ONLY,
							new ArtifactIdentifier(
									"foo-compile-only-group-id",
									"foo-compile-only-artifact-id"
							),
							"foo-compile-only-version"
					),
					new Artifact<>(
							GradleArtifactType.COMPILE_ONLY,
							new ArtifactIdentifier(
									"foo-compile-only-group-id",
									"foo-compile-only-artifact-id"
							),
							"foo-compile-only-resolved-version"
					),
					new Artifact<>(
							GradleArtifactType.COMPILE_ONLY,
							new ArtifactIdentifier(
									"foo-compile-only-group-id",
									"foo-compile-only-artifact-id"
							),
							"foo-compile-only-version"
					)
			);
		}
	}

	@Test
	void testParseArtifactsInvalid() throws IOException {
		assertThatNullPointerException()
				.isThrownBy(() -> GradleBuildResolver.parseArtifacts(null));
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project" // Until Gradle 6.7
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project 'foo-project'" // Since Gradle 6.8
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project\n" // Until Gradle 6.7
										+ "------------------------------------------------------------"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project 'foo-project'\n" // Since Gradle 6.8
										+ "------------------------------------------------------------"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project\n" // Until Gradle 6.7
										+ "------------------------------------------------------------\n"
										+ "\n"
										+ "compileOnly - Compile only dependencies for source set 'main'.\n"
										+ "???? foo-compile-only-group-id:foo-compile-only-artifact-id:"
										+ "foo-compile-only-version (n)"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project 'foo-project'\n" // Since Gradle 6.8
										+ "------------------------------------------------------------\n"
										+ "\n"
										+ "compileOnly - Compile only dependencies for source set 'main'.\n"
										+ "???? foo-compile-only-group-id:foo-compile-only-artifact-id:"
										+ "foo-compile-only-version (n)"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project\n" // Until Gradle 6.7
										+ "------------------------------------------------------------\n"
										+ "\n"
										+ "compileOnly - Compile only dependencies for source set 'main'.\n"
										+ "+--- foo-compile-only-group-id (n)"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project 'foo-project'\n" // Since Gradle 6.8
										+ "------------------------------------------------------------\n"
										+ "\n"
										+ "compileOnly - Compile only dependencies for source set 'main'.\n"
										+ "+--- foo-compile-only-group-id (n)"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project\n" // Until Gradle 6.7
										+ "------------------------------------------------------------\n"
										+ "\n"
										+ "compileOnly - Compile only dependencies for source set 'main'.\n"
										+ "+--- foo-compile-only-group-id:foo-compile-only-artifact-id:"
										+ "foo-compile-only-version:foo-compile-only-resolved-version (n)"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project 'foo-project'\n" // Since Gradle 6.8
										+ "------------------------------------------------------------\n"
										+ "\n"
										+ "compileOnly - Compile only dependencies for source set 'main'.\n"
										+ "+--- foo-compile-only-group-id:foo-compile-only-artifact-id:"
										+ "foo-compile-only-version:foo-compile-only-resolved-version (n)"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project\n" // Until Gradle 6.7
										+ "------------------------------------------------------------\n"
										+ "\n"
										+ "compileOnly - Compile only dependencies for source set 'main'.\n"
										+ "+--- foo-compile-only-group-id:foo-compile-only-artifact-id -> "
										+ "foo-compile-only-version -> foo-compile-only-resolved-version (c)"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
		try (
				var bufferedReader = new BufferedReader(
						new StringReader(
								"> Task :dependencies\n"
										+ "\n"
										+ "------------------------------------------------------------\n"
										+ "Root project 'foo-project'\n" // Since Gradle 6.8
										+ "------------------------------------------------------------\n"
										+ "\n"
										+ "compileOnly - Compile only dependencies for source set 'main'.\n"
										+ "+--- foo-compile-only-group-id:foo-compile-only-artifact-id -> "
										+ "foo-compile-only-version -> foo-compile-only-resolved-version (c)"
						)
				)
		) {
			assertThatExceptionOfType(BuildResolveException.class)
					.isThrownBy(() -> GradleBuildResolver.parseArtifacts(bufferedReader));
		}
	}

	@Test
	void testFilterRepositories() {
		final var list = Stream.of(
				new Repository(
						RepositoryType.NORMAL,
						"MavenLocal",
						"file:/foo-repository-directory"
				),
				new Repository(
						RepositoryType.NORMAL,
						"foo-repository-name",
						"https://foo-repository-host"
				)
		).collect(Collectors.toList());
		assertThat(GradleBuildResolver.filterRepositories(list)).containsExactly(
				new Repository(
						RepositoryType.NORMAL,
						"foo-repository-name",
						"https://foo-repository-host"
				)
		);
	}

	@Test
	void testFilterRepositoriesInvalid() {
		assertThatNullPointerException()
				.isThrownBy(() -> GradleBuildResolver.filterRepositories(null));
		assertThatNullPointerException()
				.isThrownBy(() -> GradleBuildResolver.filterRepositories(Collections.singletonList(null)));
	}

	@Test
	@Deprecated
	void testFilter() {
		final var list = Stream.of(
				// Deprecated or classpath duplicates to be removed
				new Artifact<>(
						GradleArtifactType.COMPILE_CLASSPATH,
						new ArtifactIdentifier(
								"foo-implementation-group-id",
								"foo-implementation-artifact-id"
						),
						"foo-implementation-version"
				),
				new Artifact<>(
						GradleArtifactType.RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-runtime-only-group-id",
								"foo-runtime-only-artifact-id"
						),
						"foo-runtime-only-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_COMPILE_CLASSPATH,
						new ArtifactIdentifier(
								"foo-test-implementation-group-id",
								"foo-test-implementation-artifact-id"
						),
						"foo-test-implementation-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-test-runtime-only-group-id",
								"foo-test-runtime-only-artifact-id"
						),
						"foo-test-runtime-only-version"
				),

				// "COMPILE_CLASSPATH" duplicates to be removed
				new Artifact<>(
						GradleArtifactType.RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-compile-classpath-group-id",
								"foo-compile-classpath-artifact-id"
						),
						"foo-compile-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_COMPILE_CLASSPATH,
						new ArtifactIdentifier(
								"foo-compile-classpath-group-id",
								"foo-compile-classpath-artifact-id"
						),
						"foo-compile-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-compile-classpath-group-id",
								"foo-compile-classpath-artifact-id"
						),
						"foo-compile-classpath-version"
				),

				// "RUNTIME_CLASSPATH" duplicates to be removed
				new Artifact<>(
						GradleArtifactType.TEST_COMPILE_CLASSPATH,
						new ArtifactIdentifier(
								"foo-runtime-classpath-group-id",
								"foo-runtime-classpath-artifact-id"
						),
						"foo-runtime-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-runtime-classpath-group-id",
								"foo-runtime-classpath-artifact-id"
						),
						"foo-runtime-classpath-version"
				),

				// "TEST_COMPILE_CLASSPATH" duplicates to be removed
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-test-compile-classpath-group-id",
								"foo-test-compile-classpath-artifact-id"
						),
						"foo-test-compile-classpath-version"
				),

				// "COMPILE_CLASSPATH" to "IMPLEMENTATION"
				new Artifact<>(
						GradleArtifactType.COMPILE_CLASSPATH,
						new ArtifactIdentifier(
								"foo-compile-classpath-group-id",
								"foo-compile-classpath-artifact-id"
						),
						"foo-compile-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.IMPLEMENTATION,
						new ArtifactIdentifier(
								"foo-implementation-group-id",
								"foo-implementation-artifact-id"
						),
						"foo-implementation-version"
				),

				// "RUNTIME_CLASSPATH" to "RUNTIME_ONLY"
				new Artifact<>(
						GradleArtifactType.RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-runtime-classpath-group-id",
								"foo-runtime-classpath-artifact-id"
						),
						"foo-runtime-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.RUNTIME_ONLY,
						new ArtifactIdentifier(
								"foo-runtime-only-group-id",
								"foo-runtime-only-artifact-id"
						),
						"foo-runtime-only-version"
				),

				// "TEST_COMPILE_CLASSPATH" to "TEST_IMPLEMENTATION"
				new Artifact<>(
						GradleArtifactType.TEST_COMPILE_CLASSPATH,
						new ArtifactIdentifier(
								"foo-test-compile-classpath-group-id",
								"foo-test-compile-classpath-artifact-id"
						),
						"foo-test-compile-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_IMPLEMENTATION,
						new ArtifactIdentifier(
								"foo-test-implementation-group-id",
								"foo-test-implementation-artifact-id"
						),
						"foo-test-implementation-version"
				),

				// "TEST_RUNTIME_CLASSPATH" to "TEST_RUNTIME_ONLY"
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-test-runtime-classpath-group-id",
								"foo-test-runtime-classpath-artifact-id"
						),
						"foo-test-runtime-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_ONLY,
						new ArtifactIdentifier(
								"foo-test-runtime-only-group-id",
								"foo-test-runtime-only-artifact-id"
						),
						"foo-test-runtime-only-version"
				)
		).collect(Collectors.toList());
		assertThat(GradleBuildResolver.filter(list)).containsExactly(
				new Artifact<>(
						GradleArtifactType.IMPLEMENTATION,
						new ArtifactIdentifier(
								"foo-compile-classpath-group-id",
								"foo-compile-classpath-artifact-id"
						),
						"foo-compile-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.IMPLEMENTATION,
						new ArtifactIdentifier(
								"foo-implementation-group-id",
								"foo-implementation-artifact-id"
						),
						"foo-implementation-version"
				),
				new Artifact<>(
						GradleArtifactType.RUNTIME_ONLY,
						new ArtifactIdentifier(
								"foo-runtime-classpath-group-id",
								"foo-runtime-classpath-artifact-id"
						),
						"foo-runtime-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.RUNTIME_ONLY,
						new ArtifactIdentifier(
								"foo-runtime-only-group-id",
								"foo-runtime-only-artifact-id"
						),
						"foo-runtime-only-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_IMPLEMENTATION,
						new ArtifactIdentifier(
								"foo-test-compile-classpath-group-id",
								"foo-test-compile-classpath-artifact-id"
						),
						"foo-test-compile-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_IMPLEMENTATION,
						new ArtifactIdentifier(
								"foo-test-implementation-group-id",
								"foo-test-implementation-artifact-id"
						),
						"foo-test-implementation-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_ONLY,
						new ArtifactIdentifier(
								"foo-test-runtime-classpath-group-id",
								"foo-test-runtime-classpath-artifact-id"
						),
						"foo-test-runtime-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_ONLY,
						new ArtifactIdentifier(
								"foo-test-runtime-only-group-id",
								"foo-test-runtime-only-artifact-id"
						),
						"foo-test-runtime-only-version"
				)
		);
	}

	@Test
	@Deprecated
	void testFilterInvalid() {
		assertThatNullPointerException().isThrownBy(() -> GradleBuildResolver.filter(null));
		assertThatNullPointerException().isThrownBy(() -> GradleBuildResolver.filter(Collections.singletonList(null)));
	}

	@Test
	void testFilterArtifacts() {
		final var list = Stream.of(
				// Deprecated or classpath duplicates to be removed
				new Artifact<>(
						GradleArtifactType.COMPILE_CLASSPATH,
						new ArtifactIdentifier(
								"foo-implementation-group-id",
								"foo-implementation-artifact-id"
						),
						"foo-implementation-version"
				),
				new Artifact<>(
						GradleArtifactType.RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-runtime-only-group-id",
								"foo-runtime-only-artifact-id"
						),
						"foo-runtime-only-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_COMPILE_CLASSPATH,
						new ArtifactIdentifier(
								"foo-test-implementation-group-id",
								"foo-test-implementation-artifact-id"
						),
						"foo-test-implementation-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-test-runtime-only-group-id",
								"foo-test-runtime-only-artifact-id"
						),
						"foo-test-runtime-only-version"
				),

				// "COMPILE_CLASSPATH" duplicates to be removed
				new Artifact<>(
						GradleArtifactType.RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-compile-classpath-group-id",
								"foo-compile-classpath-artifact-id"
						),
						"foo-compile-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_COMPILE_CLASSPATH,
						new ArtifactIdentifier(
								"foo-compile-classpath-group-id",
								"foo-compile-classpath-artifact-id"
						),
						"foo-compile-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-compile-classpath-group-id",
								"foo-compile-classpath-artifact-id"
						),
						"foo-compile-classpath-version"
				),

				// "RUNTIME_CLASSPATH" duplicates to be removed
				new Artifact<>(
						GradleArtifactType.TEST_COMPILE_CLASSPATH,
						new ArtifactIdentifier(
								"foo-runtime-classpath-group-id",
								"foo-runtime-classpath-artifact-id"
						),
						"foo-runtime-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-runtime-classpath-group-id",
								"foo-runtime-classpath-artifact-id"
						),
						"foo-runtime-classpath-version"
				),

				// "TEST_COMPILE_CLASSPATH" duplicates to be removed
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-test-compile-classpath-group-id",
								"foo-test-compile-classpath-artifact-id"
						),
						"foo-test-compile-classpath-version"
				),

				// "COMPILE_CLASSPATH" to "IMPLEMENTATION"
				new Artifact<>(
						GradleArtifactType.COMPILE_CLASSPATH,
						new ArtifactIdentifier(
								"foo-compile-classpath-group-id",
								"foo-compile-classpath-artifact-id"
						),
						"foo-compile-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.IMPLEMENTATION,
						new ArtifactIdentifier(
								"foo-implementation-group-id",
								"foo-implementation-artifact-id"
						),
						"foo-implementation-version"
				),

				// "RUNTIME_CLASSPATH" to "RUNTIME_ONLY"
				new Artifact<>(
						GradleArtifactType.RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-runtime-classpath-group-id",
								"foo-runtime-classpath-artifact-id"
						),
						"foo-runtime-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.RUNTIME_ONLY,
						new ArtifactIdentifier(
								"foo-runtime-only-group-id",
								"foo-runtime-only-artifact-id"
						),
						"foo-runtime-only-version"
				),

				// "TEST_COMPILE_CLASSPATH" to "TEST_IMPLEMENTATION"
				new Artifact<>(
						GradleArtifactType.TEST_COMPILE_CLASSPATH,
						new ArtifactIdentifier(
								"foo-test-compile-classpath-group-id",
								"foo-test-compile-classpath-artifact-id"
						),
						"foo-test-compile-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_IMPLEMENTATION,
						new ArtifactIdentifier(
								"foo-test-implementation-group-id",
								"foo-test-implementation-artifact-id"
						),
						"foo-test-implementation-version"
				),

				// "TEST_RUNTIME_CLASSPATH" to "TEST_RUNTIME_ONLY"
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_CLASSPATH,
						new ArtifactIdentifier(
								"foo-test-runtime-classpath-group-id",
								"foo-test-runtime-classpath-artifact-id"
						),
						"foo-test-runtime-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_ONLY,
						new ArtifactIdentifier(
								"foo-test-runtime-only-group-id",
								"foo-test-runtime-only-artifact-id"
						),
						"foo-test-runtime-only-version"
				)
		).collect(Collectors.toList());
		assertThat(GradleBuildResolver.filterArtifacts(list)).containsExactly(
				new Artifact<>(
						GradleArtifactType.IMPLEMENTATION,
						new ArtifactIdentifier(
								"foo-compile-classpath-group-id",
								"foo-compile-classpath-artifact-id"
						),
						"foo-compile-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.IMPLEMENTATION,
						new ArtifactIdentifier(
								"foo-implementation-group-id",
								"foo-implementation-artifact-id"
						),
						"foo-implementation-version"
				),
				new Artifact<>(
						GradleArtifactType.RUNTIME_ONLY,
						new ArtifactIdentifier(
								"foo-runtime-classpath-group-id",
								"foo-runtime-classpath-artifact-id"
						),
						"foo-runtime-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.RUNTIME_ONLY,
						new ArtifactIdentifier(
								"foo-runtime-only-group-id",
								"foo-runtime-only-artifact-id"
						),
						"foo-runtime-only-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_IMPLEMENTATION,
						new ArtifactIdentifier(
								"foo-test-compile-classpath-group-id",
								"foo-test-compile-classpath-artifact-id"
						),
						"foo-test-compile-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_IMPLEMENTATION,
						new ArtifactIdentifier(
								"foo-test-implementation-group-id",
								"foo-test-implementation-artifact-id"
						),
						"foo-test-implementation-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_ONLY,
						new ArtifactIdentifier(
								"foo-test-runtime-classpath-group-id",
								"foo-test-runtime-classpath-artifact-id"
						),
						"foo-test-runtime-classpath-version"
				),
				new Artifact<>(
						GradleArtifactType.TEST_RUNTIME_ONLY,
						new ArtifactIdentifier(
								"foo-test-runtime-only-group-id",
								"foo-test-runtime-only-artifact-id"
						),
						"foo-test-runtime-only-version"
				)
		);
	}

	@Test
	void testFilterArtifactsInvalid() {
		assertThatNullPointerException()
				.isThrownBy(() -> GradleBuildResolver.filterArtifacts(null));
		assertThatNullPointerException()
				.isThrownBy(() -> GradleBuildResolver.filterArtifacts(Collections.singletonList(null)));
	}
}