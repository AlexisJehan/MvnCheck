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
package com.github.alexisjehan.mavencheck;

import com.github.alexisjehan.mavencheck.core.Service;
import com.github.alexisjehan.mavencheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mavencheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mavencheck.core.component.artifact.type.ArtifactType;
import com.github.alexisjehan.mavencheck.core.component.artifact.type.MavenArtifactType;
import com.github.alexisjehan.mavencheck.core.component.artifact.version.ArtifactUpdateVersion;
import com.github.alexisjehan.mavencheck.core.component.build.Build;
import com.github.alexisjehan.mavencheck.core.component.build.file.BuildFile;
import com.github.alexisjehan.mavencheck.core.component.build.file.BuildFileType;
import com.github.alexisjehan.mavencheck.core.component.build.resolver.BuildResolveException;
import com.github.alexisjehan.mavencheck.core.util.GithubUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@ExtendWith(MockitoExtension.class)
final class ApplicationTest {

	@Mock
	private Service mockedService;

	@Test
	void testConstructorInvalid() {
		try (var printStream = new PrintStream(OutputStream.nullOutputStream())) {
			assertThatNullPointerException()
					.isThrownBy(() -> new Application(null, printStream, false));
			assertThatNullPointerException()
					.isThrownBy(() -> new Application(printStream, null, false));
		}
	}

	@Test
	@Deprecated
	void testRunDeprecated() throws IOException {
		final var path1 = Path.of("path1");
		final var path2 = Path.of("path2");
		final var path3 = Path.of("path3");
		final var buildFile1 = new BuildFile(BuildFileType.MAVEN, Path.of("pom.xml"));
		final var buildFile2 = new BuildFile(BuildFileType.GRADLE_GROOVY, Path.of("build.gradle"));
		final var buildFile3 = new BuildFile(BuildFileType.GRADLE_KOTLIN, Path.of("build.gradle.kts"));
		final var artifact = new Artifact<>(
				MavenArtifactType.DEPENDENCY,
				new ArtifactIdentifier("foo-group-id", "foo-artifact-id"),
				"1.0.0"
		);
		final var build1 = new Build(
				buildFile1,
				List.of(),
				List.of(artifact)
		);
		final var build2 = new Build(
				buildFile2,
				List.of(),
				List.of(artifact)
		);
		Mockito.when(mockedService.findBuildFiles(Mockito.argThat(path1::equals), Mockito.anyInt()))
				.thenReturn(List.of());
		Mockito.when(mockedService.findBuildFiles(Mockito.argThat(path2::equals), Mockito.anyInt()))
				.thenReturn(List.of(buildFile1));
		Mockito.when(mockedService.findBuildFiles(Mockito.argThat(path3::equals), Mockito.anyInt()))
				.thenReturn(List.of(buildFile1, buildFile2, buildFile3));
		Mockito.when(mockedService.findBuild(Mockito.argThat(buildFile1::equals)))
				.thenReturn(build1);
		Mockito.when(mockedService.findBuild(Mockito.argThat(buildFile2::equals)))
				.thenReturn(build2);
		Mockito.when(mockedService.findBuild(Mockito.argThat(buildFile3::equals)))
				.thenThrow(BuildResolveException.class);
		Mockito.when(mockedService.findArtifactUpdateVersions(Mockito.argThat(build1::equals), Mockito.anyBoolean()))
				.thenReturn(List.of());
		Mockito.when(mockedService.findArtifactUpdateVersions(Mockito.argThat(build2::equals), Mockito.anyBoolean()))
				.thenReturn(List.of(new ArtifactUpdateVersion(artifact, "2.0.0")));
		try (var printStream = new PrintStream(OutputStream.nullOutputStream())) {
			try (var mockedGithubUtils = Mockito.mockStatic(GithubUtils.class)) {
				mockedGithubUtils.when(
								() -> GithubUtils.retrieveOptionalLatestReleaseName(
										Mockito.notNull(),
										Mockito.notNull()
								)
						)
						.thenReturn(
								Optional.of("1.0.0"),
								Optional.of("2.0.0"),
								Optional.empty()
						);
				try (var mockedApplication = Mockito.mockStatic(Application.class)) {
					mockedApplication.when(Application::createService)
							.thenReturn(mockedService);
					mockedApplication.when(Application::getCurrentVersion)
							.thenReturn(
									"1.0.0",
									"1.0.0",
									null
							);
					final var application = new Application(printStream);
					assertThatNoException()
							.isThrownBy(application::run);
					assertThatNoException()
							.isThrownBy(() -> application.run("-d", "0"));
					assertThatNoException()
							.isThrownBy(() -> application.run("-h"));
					assertThatNoException()
							.isThrownBy(() -> application.run("-i"));
					assertThatNoException()
							.isThrownBy(() -> application.run("-s"));
					assertThatNoException()
							.isThrownBy(() -> application.run("-v"));
					assertThatNoException()
							.isThrownBy(() -> application.run("directory_not-found"));
					assertThatNoException()
							.isThrownBy(() -> application.run(path1.toString()));
					assertThatNoException()
							.isThrownBy(() -> application.run(path2.toString()));
					assertThatNoException()
							.isThrownBy(() -> application.run(path3.toString()));
					assertThatNoException()
							.isThrownBy(() -> application.run(path1, true, true));
					assertThatNoException()
							.isThrownBy(() -> application.run(path2, true, true));
					assertThatNoException()
							.isThrownBy(() -> application.run(path3, true, true));
				}
			}
		}
	}

	@Test
	@Deprecated
	void testRunDeprecatedInvalid() {
		try (var printStream = new PrintStream(OutputStream.nullOutputStream())) {
			final var application = new Application(printStream);
			assertThatNullPointerException().isThrownBy(
					() -> application.run((String[]) null)
			);
			assertThatNullPointerException().isThrownBy(
					() -> application.run((String) null)
			);
			assertThatNullPointerException().isThrownBy(
					() -> application.run(null, false, false)
			);
		}
	}

	@Test
	void testRun() throws IOException {
		final var path1 = Path.of("path1");
		final var path2 = Path.of("path2");
		final var path3 = Path.of("path3");
		final var buildFile1 = new BuildFile(BuildFileType.MAVEN, Path.of("pom.xml"));
		final var buildFile2 = new BuildFile(BuildFileType.GRADLE_GROOVY, Path.of("build.gradle"));
		final var buildFile3 = new BuildFile(BuildFileType.GRADLE_KOTLIN, Path.of("build.gradle.kts"));
		final var artifact = new Artifact<>(
				MavenArtifactType.DEPENDENCY,
				new ArtifactIdentifier("foo-group-id", "foo-artifact-id"),
				"1.0.0"
		);
		final var build1 = new Build(
				buildFile1,
				List.of(),
				List.of(artifact)
		);
		final var build2 = new Build(
				buildFile2,
				List.of(),
				List.of(artifact)
		);
		Mockito.when(mockedService.findBuildFiles(Mockito.argThat(path1::equals), Mockito.anyInt()))
				.thenReturn(List.of());
		Mockito.when(mockedService.findBuildFiles(Mockito.argThat(path2::equals), Mockito.anyInt()))
				.thenReturn(List.of(buildFile1));
		Mockito.when(mockedService.findBuildFiles(Mockito.argThat(path3::equals), Mockito.anyInt()))
				.thenReturn(List.of(buildFile1, buildFile2, buildFile3));
		Mockito.when(mockedService.findBuild(Mockito.argThat(buildFile1::equals)))
				.thenReturn(build1);
		Mockito.when(mockedService.findBuild(Mockito.argThat(buildFile2::equals)))
				.thenReturn(build2);
		Mockito.when(mockedService.findBuild(Mockito.argThat(buildFile3::equals)))
				.thenThrow(BuildResolveException.class);
		Mockito.when(mockedService.findArtifactUpdateVersions(Mockito.argThat(build1::equals), Mockito.anyBoolean()))
				.thenReturn(List.of());
		Mockito.when(mockedService.findArtifactUpdateVersions(Mockito.argThat(build2::equals), Mockito.anyBoolean()))
				.thenReturn(List.of(new ArtifactUpdateVersion(artifact, "2.0.0")));
		try (var printStream = new PrintStream(OutputStream.nullOutputStream())) {
			try (var mockedGithubUtils = Mockito.mockStatic(GithubUtils.class)) {
				mockedGithubUtils.when(
								() -> GithubUtils.retrieveOptionalLatestReleaseName(
										Mockito.notNull(),
										Mockito.notNull()
								)
						)
						.thenReturn(
								Optional.of("1.0.0"),
								Optional.of("2.0.0"),
								Optional.empty()
						);
				try (var mockedApplication = Mockito.mockStatic(Application.class)) {
					mockedApplication.when(Application::createService)
							.thenReturn(mockedService);
					mockedApplication.when(Application::getCurrentVersion)
							.thenReturn(
									"1.0.0",
									"1.0.0",
									null
							);
					final var application = new Application(printStream);
					assertThatNoException()
							.isThrownBy(application::run);
					assertThatNoException()
							.isThrownBy(() -> application.run("-d", "0"));
					assertThatNoException()
							.isThrownBy(() -> application.run("-h"));
					assertThatNoException()
							.isThrownBy(() -> application.run("-i"));
					assertThatNoException()
							.isThrownBy(() -> application.run("-s"));
					assertThatNoException()
							.isThrownBy(() -> application.run("-v"));
					assertThatNoException()
							.isThrownBy(() -> application.run("directory_not-found"));
					assertThatNoException()
							.isThrownBy(() -> application.run(path1.toString()));
					assertThatNoException()
							.isThrownBy(() -> application.run(path2.toString()));
					assertThatNoException()
							.isThrownBy(() -> application.run(path3.toString()));
					assertThatNoException()
							.isThrownBy(() -> application.run(path1, 0, true, true));
					assertThatNoException()
							.isThrownBy(() -> application.run(path2, 0, true, true));
					assertThatNoException()
							.isThrownBy(() -> application.run(path3, 0, true, true));
				}
			}
		}
	}

	@Test
	void testRunInvalid() {
		try (var printStream = new PrintStream(OutputStream.nullOutputStream())) {
			final var application = new Application(printStream);
			assertThatNullPointerException().isThrownBy(
					() -> application.run((String[]) null)
			);
			assertThatNullPointerException().isThrownBy(
					() -> application.run((String) null)
			);
			assertThatNullPointerException().isThrownBy(
					() -> application.run(null, 0, false, false)
			);
			assertThatIllegalArgumentException().isThrownBy(
					() -> application.run(Path.of("path"), -1, false, false)
			);
		}
	}

	@Test
	void testCreateService() {
		assertThatNoException().isThrownBy(Application::createService);
	}

	@Test
	void testToStringException() {
		assertThat(Application.toString(new Exception("foo ")))
				.isEqualTo("java.lang.Exception: foo");
		assertThat(Application.toString(new Exception("foo ", new Exception("bar "))))
				.isEqualTo("java.lang.Exception: bar");
	}

	@Test
	void testToStringExceptionInvalid() {
		assertThatNullPointerException().isThrownBy(() -> Application.toString((Exception) null));
	}

	@Test
	void testToStringPath() {
		assertThat(Application.toString(Path.of("." + File.separator + "pom.xml")))
				.isEqualTo("pom.xml");
		assertThat(Application.toString(Path.of("." + File.separator + "foo" + File.separator + "pom.xml")))
				.isEqualTo("foo" + File.separator + "pom.xml");
	}

	@Test
	void testToStringPathInvalid() {
		assertThatNullPointerException().isThrownBy(() -> Application.toString((Path) null));
	}

	@Test
	void testToStringArtifactType() {
		assertThat(Application.toString(MavenArtifactType.DEPENDENCY)).isEqualTo("DEPENDENCY");
		assertThat(Application.toString(MavenArtifactType.BUILD_PLUGIN)).isEqualTo("BUILD PLUGIN");
	}

	@Test
	void testToStringArtifactTypeInvalid() {
		assertThatNullPointerException().isThrownBy(() -> Application.toString((ArtifactType) null));
	}

	@Test
	void testToStringArtifactIdentifier() {
		assertThat(Application.toString(new ArtifactIdentifier("foo-group-id", "foo-artifact-id")))
				.isEqualTo("foo-group-id:foo-artifact-id");
	}

	@Test
	void testToStringArtifactIdentifierInvalid() {
		assertThatNullPointerException().isThrownBy(() -> Application.toString((ArtifactIdentifier) null));
	}

	@Test
	void testGetCurrentVersion() {
		assertThatNoException().isThrownBy(Application::getCurrentVersion);
	}
}