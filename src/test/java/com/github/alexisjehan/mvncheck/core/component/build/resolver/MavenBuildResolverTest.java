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
import com.github.alexisjehan.mvncheck.core.component.artifact.type.MavenArtifactType;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFile;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFileType;
import com.github.alexisjehan.mvncheck.core.component.repository.Repository;
import com.github.alexisjehan.mvncheck.core.component.repository.RepositoryType;
import com.github.alexisjehan.mvncheck.core.component.session.MavenSession;
import org.apache.maven.model.building.ModelBuildingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@ExtendWith(MockitoExtension.class)
final class MavenBuildResolverTest {

	@Mock
	private MavenSession mockedMavenSession;

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException().isThrownBy(() -> new MavenBuildResolver(null));
	}

	@Test
	void testResolve() {
		final var mavenBuildResolver = new MavenBuildResolver(mockedMavenSession);
		assertThat(
				new BuildFile(
						BuildFileType.MAVEN,
						Path.of("src", "test", "resources", "pom_empty.xml")
				)
		).satisfies(buildFile -> {
			final var build = mavenBuildResolver.resolve(buildFile);
			assertThat(build.getFile()).isSameAs(buildFile);
			assertThat(build.getRepositories()).containsExactly(
					new Repository(
							RepositoryType.NORMAL,
							"central",
							"https://repo.maven.apache.org/maven2"
					),
					new Repository(
							RepositoryType.PLUGIN,
							"central",
							"https://repo.maven.apache.org/maven2"
					)
			);
			assertThat(build.getArtifacts()).isEmpty();
		});
		assertThat(
				new BuildFile(
						BuildFileType.MAVEN,
						Path.of("src", "test", "resources", "pom_foo_parent.xml")
				)
		).satisfies(buildFile -> {
			final var build = mavenBuildResolver.resolve(buildFile);
			assertThat(build.getFile()).isSameAs(buildFile);
			assertThat(build.getRepositories()).containsExactly(
					new Repository(
							RepositoryType.NORMAL,
							"central",
							"https://repo.maven.apache.org/maven2"
					),
					new Repository(
							RepositoryType.PLUGIN,
							"central",
							"https://repo.maven.apache.org/maven2"
					)
			);
			assertThat(build.getArtifacts()).isEmpty();
		});
		assertThat(
				new BuildFile(
						BuildFileType.MAVEN,
						Path.of("src", "test", "resources", "pom_foo.xml")
				)
		).satisfies(buildFile -> {
			final var build = mavenBuildResolver.resolve(buildFile);
			assertThat(build.getFile()).isSameAs(buildFile);
			assertThat(build.getRepositories()).containsExactly(
					new Repository(
							RepositoryType.NORMAL,
							"foo-repository-id",
							"https://foo-repository-host"
					),
					new Repository(
							RepositoryType.NORMAL,
							"central",
							"https://repo.maven.apache.org/maven2"
					),
					new Repository(
							RepositoryType.PLUGIN,
							"foo-plugin-repository-id",
							"https://foo-plugin-repository-host"
					),
					new Repository(
							RepositoryType.PLUGIN,
							"central",
							"https://repo.maven.apache.org/maven2"
					)
			);
			assertThat(build.getArtifacts()).containsExactly(
					new Artifact<>(
							MavenArtifactType.PARENT,
							new ArtifactIdentifier(
									"foo-parent-group-id",
									"foo-parent-artifact-id"
							),
							"foo-parent-version"
					),
					new Artifact<>(
							MavenArtifactType.DEPENDENCY_MANAGEMENT_DEPENDENCY,
							new ArtifactIdentifier(
									"foo-dependency-management-dependency-group-id",
									"foo-dependency-management-dependency-artifact-id"
							),
							"foo-dependency-management-dependency-version"
					),
					new Artifact<>(
							MavenArtifactType.DEPENDENCY,
							new ArtifactIdentifier(
									"foo-dependency-group-id",
									"foo-dependency-artifact-id"
							),
							"foo-dependency-version"
					),
					new Artifact<>(
							MavenArtifactType.BUILD_EXTENSION,
							new ArtifactIdentifier(
									"foo-build-extension-group-id",
									"foo-build-extension-artifact-id"
							),
							"foo-build-extension-version"
					),
					new Artifact<>(
							MavenArtifactType.BUILD_PLUGIN_MANAGEMENT_PLUGIN,
							new ArtifactIdentifier(
									"foo-build-plugin-management-plugin-group-id",
									"foo-build-plugin-management-plugin-artifact-id"
							),
							"foo-build-plugin-management-plugin-version"
					),
					new Artifact<>(
							MavenArtifactType.BUILD_PLUGIN_MANAGEMENT_PLUGIN_DEPENDENCY,
							new ArtifactIdentifier(
									"foo-build-plugin-management-plugin-dependency-group-id",
									"foo-build-plugin-management-plugin-dependency-artifact-id"
							),
							"foo-build-plugin-management-plugin-dependency-version"
					),
					new Artifact<>(
							MavenArtifactType.BUILD_PLUGIN,
							new ArtifactIdentifier(
									"foo-build-plugin-group-id",
									"foo-build-plugin-artifact-id"
							),
							"foo-build-plugin-version"
					),
					new Artifact<>(
							MavenArtifactType.BUILD_PLUGIN_DEPENDENCY,
							new ArtifactIdentifier(
									"foo-build-plugin-dependency-group-id",
									"foo-build-plugin-dependency-artifact-id"
							),
							"foo-build-plugin-dependency-version"
					),
					new Artifact<>(
							MavenArtifactType.REPORTING_PLUGIN,
							new ArtifactIdentifier(
									"foo-reporting-plugin-group-id",
									"foo-reporting-plugin-artifact-id"
							),
							"foo-reporting-plugin-version"
					),
					new Artifact<>(
							MavenArtifactType.PROFILE_DEPENDENCY_MANAGEMENT_DEPENDENCY,
							new ArtifactIdentifier(
									"foo-profile-dependency-management-dependency-group-id",
									"foo-profile-dependency-management-dependency-artifact-id"
							),
							"foo-profile-dependency-management-dependency-version"
					),
					new Artifact<>(
							MavenArtifactType.PROFILE_DEPENDENCY,
							new ArtifactIdentifier(
									"foo-profile-dependency-group-id",
									"foo-profile-dependency-artifact-id"
							),
							"foo-profile-dependency-version"
					),
					new Artifact<>(
							MavenArtifactType.PROFILE_BUILD_PLUGIN_MANAGEMENT_PLUGIN,
							new ArtifactIdentifier(
									"foo-profile-build-plugin-management-plugin-group-id",
									"foo-profile-build-plugin-management-plugin-artifact-id"
							),
							"foo-profile-build-plugin-management-plugin-version"
					),
					new Artifact<>(
							MavenArtifactType.PROFILE_BUILD_PLUGIN_MANAGEMENT_PLUGIN_DEPENDENCY,
							new ArtifactIdentifier(
									"foo-profile-build-plugin-management-plugin-dependency-group-id",
									"foo-profile-build-plugin-management-plugin-dependency-artifact-id"
							),
							"foo-profile-build-plugin-management-plugin-dependency-version"
					),
					new Artifact<>(
							MavenArtifactType.PROFILE_BUILD_PLUGIN,
							new ArtifactIdentifier(
									"foo-profile-build-plugin-group-id",
									"foo-profile-build-plugin-artifact-id"
							),
							"foo-profile-build-plugin-version"
					),
					new Artifact<>(
							MavenArtifactType.PROFILE_BUILD_PLUGIN_DEPENDENCY,
							new ArtifactIdentifier(
									"foo-profile-build-plugin-dependency-group-id",
									"foo-profile-build-plugin-dependency-artifact-id"
							),
							"foo-profile-build-plugin-dependency-version"
					),
					new Artifact<>(
							MavenArtifactType.PROFILE_REPORTING_PLUGIN,
							new ArtifactIdentifier(
									"foo-profile-reporting-plugin-group-id",
									"foo-profile-reporting-plugin-artifact-id"
							),
							"foo-profile-reporting-plugin-version"
					)
			);
		});
	}

	@Test
	void testResolveInvalid() {
		final var mavenBuildResolver = new MavenBuildResolver(mockedMavenSession);
		assertThatNullPointerException().isThrownBy(() -> mavenBuildResolver.resolve(null));
		assertThat(
				new BuildFile(
						BuildFileType.MAVEN,
						Path.of("src", "test", "resources", "pom_not-found.xml")
				)
		).satisfies(
				buildFile -> assertThatExceptionOfType(BuildResolveException.class)
						.isThrownBy(() -> mavenBuildResolver.resolve(buildFile))
						.withCauseInstanceOf(ModelBuildingException.class)
		);
		assertThat(
				new BuildFile(
						BuildFileType.MAVEN,
						Path.of("src", "test", "resources", "pom_error.xml")
				)
		).satisfies(
				buildFile -> assertThatExceptionOfType(BuildResolveException.class)
						.isThrownBy(() -> mavenBuildResolver.resolve(buildFile))
						.withCauseInstanceOf(ModelBuildingException.class)
		);
	}

	@Test
	void testGetFileTypes() {
		final var mavenBuildResolver = new MavenBuildResolver(mockedMavenSession);
		assertThat(mavenBuildResolver.getFileTypes()).contains(BuildFileType.MAVEN);
	}
}