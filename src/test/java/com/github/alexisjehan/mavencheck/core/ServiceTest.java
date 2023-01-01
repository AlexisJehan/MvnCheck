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
package com.github.alexisjehan.mavencheck.core;

import com.github.alexisjehan.javanilla.util.function.ThrowableConsumer;
import com.github.alexisjehan.mavencheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mavencheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mavencheck.core.component.artifact.type.MavenArtifactType;
import com.github.alexisjehan.mavencheck.core.component.artifact.version.ArtifactAvailableVersions;
import com.github.alexisjehan.mavencheck.core.component.artifact.version.ArtifactUpdateVersion;
import com.github.alexisjehan.mavencheck.core.component.artifact.version.resolver.ArtifactAvailableVersionsResolver;
import com.github.alexisjehan.mavencheck.core.component.build.Build;
import com.github.alexisjehan.mavencheck.core.component.build.file.BuildFile;
import com.github.alexisjehan.mavencheck.core.component.build.file.BuildFileType;
import com.github.alexisjehan.mavencheck.core.component.build.resolver.GradleBuildResolver;
import com.github.alexisjehan.mavencheck.core.component.build.resolver.MavenBuildResolver;
import com.github.alexisjehan.mavencheck.core.component.filter.artifact.ArtifactFilter;
import com.github.alexisjehan.mavencheck.core.component.filter.artifact.parser.ArtifactFilterParser;
import com.github.alexisjehan.mavencheck.core.component.session.MavenSession;
import com.github.alexisjehan.mavencheck.core.util.SystemUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@ExtendWith(MockitoExtension.class)
final class ServiceTest {

	@Mock
	private MavenSession mockedMavenSession;

	@Mock
	private MavenBuildResolver mockedMavenBuildResolver;

	@Mock
	private GradleBuildResolver mockedGradleBuildResolver;

	@Mock
	private ArtifactAvailableVersionsResolver mockedArtifactAvailableVersionsResolver;

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException().isThrownBy(
				() -> new Service(
						null
				)
		);
		assertThatNullPointerException().isThrownBy(
				() -> new Service(
						null,
						mockedArtifactAvailableVersionsResolver
				)
		);
		assertThatNullPointerException().isThrownBy(
				() -> new Service(
						Collections.singleton(null),
						mockedArtifactAvailableVersionsResolver
				)
		);
		assertThatNullPointerException().isThrownBy(
				() -> new Service(
						Set.of(mockedMavenBuildResolver, mockedGradleBuildResolver),
						null
				)
		);
	}

	@Test
	void testFindBuildFiles(@TempDir final Path tmpDirectory) throws IOException {
		final var service = new Service(mockedMavenSession);
		final var rootMavenFile = tmpDirectory.resolve(Path.of("pom.xml"));
		final var fooMavenFile = tmpDirectory.resolve(Path.of("foo", "pom.xml"));
		final var fooGradleGroovyFile = tmpDirectory.resolve(Path.of("foo", "build.gradle"));
		final var fooGradleKotlinFile = tmpDirectory.resolve(Path.of("foo", "build.gradle.kts"));
		final var foo10MavenFile = tmpDirectory.resolve(Path.of("foo", "10", "pom.xml"));
		final var foo1GradleGroovyFile = tmpDirectory.resolve(Path.of("foo", "1", "build.gradle"));
		final var foo2GradleKotlinFile = tmpDirectory.resolve(Path.of("foo", "2", "build.gradle.kts"));
		final var barMavenFile = tmpDirectory.resolve(Path.of("bar", "pom.xml"));
		final var barGradleGroovyFile = tmpDirectory.resolve(Path.of("bar", "build.gradle"));
		final var barGradleKotlinFile = tmpDirectory.resolve(Path.of("bar", "build.gradle.kts"));
		final var bar10MavenFile = tmpDirectory.resolve(Path.of("bar", "10", "pom.xml"));
		final var bar1GradleGroovyFile = tmpDirectory.resolve(Path.of("bar", "1", "build.gradle"));
		final var bar2GradleKotlinFile = tmpDirectory.resolve(Path.of("bar", "2", "build.gradle.kts"));
		Stream.of(
				rootMavenFile,
				fooMavenFile,
				fooGradleGroovyFile,
				fooGradleKotlinFile,
				foo10MavenFile,
				foo1GradleGroovyFile,
				foo2GradleKotlinFile,
				barMavenFile,
				barGradleGroovyFile,
				barGradleKotlinFile,
				bar10MavenFile,
				bar1GradleGroovyFile,
				bar2GradleKotlinFile
		).forEach(
				ThrowableConsumer.sneaky(file -> {
					Files.createDirectories(file.getParent());
					Files.createFile(file);
				})
		);
		assertThat(service.findBuildFiles(tmpDirectory)).containsExactly(
				new BuildFile(BuildFileType.MAVEN, rootMavenFile),
				new BuildFile(BuildFileType.GRADLE_GROOVY, barGradleGroovyFile),
				new BuildFile(BuildFileType.GRADLE_KOTLIN, barGradleKotlinFile),
				new BuildFile(BuildFileType.MAVEN, barMavenFile),
				new BuildFile(BuildFileType.GRADLE_GROOVY, bar1GradleGroovyFile),
				new BuildFile(BuildFileType.GRADLE_KOTLIN, bar2GradleKotlinFile),
				new BuildFile(BuildFileType.MAVEN, bar10MavenFile),
				new BuildFile(BuildFileType.GRADLE_GROOVY, fooGradleGroovyFile),
				new BuildFile(BuildFileType.GRADLE_KOTLIN, fooGradleKotlinFile),
				new BuildFile(BuildFileType.MAVEN, fooMavenFile),
				new BuildFile(BuildFileType.GRADLE_GROOVY, foo1GradleGroovyFile),
				new BuildFile(BuildFileType.GRADLE_KOTLIN, foo2GradleKotlinFile),
				new BuildFile(BuildFileType.MAVEN, foo10MavenFile)
		);
	}

	@Test
	void testFindBuildFilesInvalid() throws IOException {
		final var service = new Service(mockedMavenSession);
		assertThatNullPointerException()
				.isThrownBy(() -> service.findBuildFiles(null));
		assertThatIllegalArgumentException()
				.isThrownBy(() -> service.findBuildFiles(Path.of("directory_not-found")));
	}

	@Test
	void testFindBuild() throws IOException {
		Mockito.when(mockedMavenBuildResolver.resolve(Mockito.notNull()))
				.then(invocation -> new Build(invocation.getArgument(0), List.of(), List.of()));
		Mockito.when(mockedMavenBuildResolver.getFileTypes())
				.thenCallRealMethod();
		Mockito.when(mockedGradleBuildResolver.resolve(Mockito.notNull()))
				.then(invocation -> new Build(invocation.getArgument(0), List.of(), List.of()));
		Mockito.when(mockedGradleBuildResolver.getFileTypes())
				.thenCallRealMethod();
		assertThat(
				new Service(
						Set.of(),
						mockedArtifactAvailableVersionsResolver
				)
		).satisfies(service -> {
			assertThat(new BuildFile(BuildFileType.MAVEN, Path.of("pom.xml"))).satisfies(
					buildFile -> assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
							() -> service.findBuild(buildFile)
					)
			);
			assertThat(new BuildFile(BuildFileType.GRADLE_GROOVY, Path.of("build.gradle"))).satisfies(
					buildFile -> assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
							() -> service.findBuild(buildFile)
					)
			);
			assertThat(new BuildFile(BuildFileType.GRADLE_KOTLIN, Path.of("build.gradle.kts"))).satisfies(
					buildFile -> assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
							() -> service.findBuild(buildFile)
					)
			);
		});
		assertThat(
				new Service(
						Set.of(mockedMavenBuildResolver),
						mockedArtifactAvailableVersionsResolver
				)
		).satisfies(service -> {
			assertThat(new BuildFile(BuildFileType.MAVEN, Path.of("pom.xml"))).satisfies(
					buildFile -> assertThat(service.findBuild(buildFile).getFile()).isSameAs(buildFile)
			);
			assertThat(new BuildFile(BuildFileType.GRADLE_GROOVY, Path.of("build.gradle"))).satisfies(
					buildFile -> assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
							() -> service.findBuild(buildFile)
					)
			);
			assertThat(new BuildFile(BuildFileType.GRADLE_KOTLIN, Path.of("build.gradle.kts"))).satisfies(
					buildFile -> assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
							() -> service.findBuild(buildFile)
					)
			);
		});
		assertThat(
				new Service(
						Set.of(mockedGradleBuildResolver),
						mockedArtifactAvailableVersionsResolver
				)
		).satisfies(service -> {
			assertThat(new BuildFile(BuildFileType.MAVEN, Path.of("pom.xml"))).satisfies(
					buildFile -> assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
							() -> service.findBuild(buildFile)
					)
			);
			assertThat(new BuildFile(BuildFileType.GRADLE_GROOVY, Path.of("build.gradle"))).satisfies(
					buildFile -> assertThat(service.findBuild(buildFile).getFile()).isSameAs(buildFile)
			);
			assertThat(new BuildFile(BuildFileType.GRADLE_KOTLIN, Path.of("build.gradle.kts"))).satisfies(
					buildFile -> assertThat(service.findBuild(buildFile).getFile()).isSameAs(buildFile)
			);
		});
		assertThat(
				new Service(
						Set.of(mockedMavenBuildResolver, mockedGradleBuildResolver),
						mockedArtifactAvailableVersionsResolver
				)
		).satisfies(service -> {
			assertThat(new BuildFile(BuildFileType.MAVEN, Path.of("pom.xml"))).satisfies(
					buildFile -> assertThat(service.findBuild(buildFile).getFile()).isSameAs(buildFile)
			);
			assertThat(new BuildFile(BuildFileType.GRADLE_GROOVY, Path.of("build.gradle"))).satisfies(
					buildFile -> assertThat(service.findBuild(buildFile).getFile()).isSameAs(buildFile)
			);
			assertThat(new BuildFile(BuildFileType.GRADLE_KOTLIN, Path.of("build.gradle.kts"))).satisfies(
					buildFile -> assertThat(service.findBuild(buildFile).getFile()).isSameAs(buildFile)
			);
		});
	}

	@Test
	void testFindBuildInvalid() throws IOException {
		final var service = new Service(mockedMavenSession);
		assertThatNullPointerException().isThrownBy(() -> service.findBuild(null));
	}

	@Test
	void testFindArtifactUpdateVersions() throws IOException {
		final var service = new Service(
				Set.of(mockedMavenBuildResolver, mockedGradleBuildResolver),
				mockedArtifactAvailableVersionsResolver
		);
		final var fooIdentifier = new ArtifactIdentifier("foo-group-id", "foo-artifact-id");
		final var barIdentifier = new ArtifactIdentifier("bar-group-id", "bar-artifact-id");
		Mockito
				.when(
						mockedArtifactAvailableVersionsResolver.resolve(
								Mockito.argThat(
										artifact -> null != artifact && fooIdentifier.equals(artifact.getIdentifier())
								),
								Mockito.notNull()
						)
				)
				.then(
						invocation -> new ArtifactAvailableVersions(
								invocation.getArgument(0),
								List.of("1.0.0", "2.0.0")
						)
				);
		Mockito
				.when(
						mockedArtifactAvailableVersionsResolver.resolve(
								Mockito.argThat(
										artifact -> null != artifact && barIdentifier.equals(artifact.getIdentifier())
								),
								Mockito.notNull()
						)
				)
				.then(
						invocation -> new ArtifactAvailableVersions(
								invocation.getArgument(0),
								List.of()
						)
				);
		final var fooArtifact1 = new Artifact<>(MavenArtifactType.DEPENDENCY, fooIdentifier, "1.0.0");
		final var fooArtifact2 = new Artifact<>(MavenArtifactType.DEPENDENCY, fooIdentifier, "2.0.0-SNAPSHOT");
		final var fooArtifact3 = new Artifact<>(MavenArtifactType.DEPENDENCY, fooIdentifier, "2.0.0");
		final var barArtifact = new Artifact<>(MavenArtifactType.DEPENDENCY, barIdentifier, "1.1.0");
		final var build = new Build(
				new BuildFile(BuildFileType.MAVEN, Path.of("src", "test", "resources", "pom.xml")),
				List.of(),
				List.of(fooArtifact1, fooArtifact2, fooArtifact3, barArtifact)
		);
		assertThat(service.findArtifactUpdateVersions(build, false)).containsExactly(
				new ArtifactUpdateVersion(fooArtifact1, "2.0.0"),
				new ArtifactUpdateVersion(fooArtifact2, "2.0.0")
		);
		assertThat(service.findArtifactUpdateVersions(build, true)).containsExactly(
				new ArtifactUpdateVersion(fooArtifact1, "2.0.0")
		);
	}

	@Test
	void testFindArtifactUpdateVersionsInvalid() throws IOException {
		final var service = new Service(mockedMavenSession);
		assertThatNullPointerException()
				.isThrownBy(() -> service.findArtifactUpdateVersions(null, false));
	}

	@Test
	void testCreateUserArtifactFilter(@TempDir final Path tmpDirectory) throws IOException {
		final var ignoreFile = tmpDirectory.resolve(Path.of(".mvnchk-ignore"));
		try (final var mockedArtifactFilterParser = Mockito.mockStatic(ArtifactFilterParser.class)) {
			mockedArtifactFilterParser.when(() -> ArtifactFilterParser.parse(Mockito.notNull()))
					.thenReturn(ArtifactFilter.ALL);
			try (final var mockedSystemUtils = Mockito.mockStatic(SystemUtils.class)) {
				mockedSystemUtils.when(SystemUtils::getUserHomeDirectory)
						.thenReturn(tmpDirectory);
				assertThat(Service.createUserArtifactFilter()).isSameAs(ArtifactFilter.NONE);
				Files.createFile(ignoreFile);
				assertThat(Service.createUserArtifactFilter()).isSameAs(ArtifactFilter.ALL);
			}
		}
	}

	@Test
	void testCreateBuildArtifactFilter(@TempDir final Path tmpDirectory) throws IOException {
		final var ignoreFile = tmpDirectory.resolve(Path.of(".mvnchk-ignore"));
		try (final var mockedArtifactFilterParser = Mockito.mockStatic(ArtifactFilterParser.class)) {
			mockedArtifactFilterParser.when(() -> ArtifactFilterParser.parse(Mockito.notNull()))
					.thenReturn(ArtifactFilter.ALL);
			final var buildFile = new BuildFile(BuildFileType.MAVEN, tmpDirectory.resolve("pom.xml"));
			assertThat(Service.createBuildArtifactFilter(buildFile)).isSameAs(ArtifactFilter.NONE);
			Files.createFile(ignoreFile);
			assertThat(Service.createBuildArtifactFilter(buildFile)).isSameAs(ArtifactFilter.ALL);
		}
	}

	@Test
	void testCreateBuildArtifactFilterInvalid() {
		assertThatNullPointerException().isThrownBy(() -> Service.createBuildArtifactFilter(null));
	}
}