/*
 * MIT License
 *
 * Copyright (c) 2022-2024 Alexis Jehan
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
package com.github.alexisjehan.mvncheck.core.component.artifact.version.resolver;

import com.github.alexisjehan.mvncheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mvncheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mvncheck.core.component.artifact.type.MavenArtifactType;
import com.github.alexisjehan.mvncheck.core.component.artifact.version.ArtifactAvailableVersions;
import com.github.alexisjehan.mvncheck.core.component.repository.Repository;
import com.github.alexisjehan.mvncheck.core.component.repository.RepositoryType;
import com.github.alexisjehan.mvncheck.core.component.session.MavenSession;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.version.Version;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@ExtendWith(MockitoExtension.class)
final class MavenArtifactAvailableVersionsResolverTest {

	private static final class SimpleVersion implements Version {

		private final String string;

		private SimpleVersion(final String string) {
			this.string = string;
		}

		@Override
		public int compareTo(final Version other) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean equals(final Object object) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int hashCode() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String toString() {
			return string;
		}
	}

	@Mock
	private MavenSession mockedMavenSession;

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException().isThrownBy(() -> new MavenArtifactAvailableVersionsResolver(null));
	}

	@Test
	void testResolve() throws VersionRangeResolutionException {
		final var versionRangeResult = new VersionRangeResult(new VersionRangeRequest());
		versionRangeResult.setVersions(
				List.of(
						new SimpleVersion("foo-version1"),
						new SimpleVersion("foo-version2")
				)
		);
		versionRangeResult.addException(new RuntimeException());
		Mockito.when(mockedMavenSession.resolve(Mockito.notNull()))
				.thenAnswer(invocation -> invocation.getArgument(0));
		Mockito.when(mockedMavenSession.request(Mockito.notNull()))
				.thenReturn(versionRangeResult);
		final var mavenArtifactAvailableVersionsResolver = new MavenArtifactAvailableVersionsResolver(
				mockedMavenSession
		);
		final var repositories = List.of(
				new Repository(RepositoryType.NORMAL, "foo-id", "https://foo-host"),
				new Repository(RepositoryType.PLUGIN, "foo-plugin-id", "https://foo-plugin-host")
		);
		assertThat(
				new Artifact<>(
						MavenArtifactType.DEPENDENCY,
						new ArtifactIdentifier("foo-group-id", "foo-artifact-id"),
						"foo-version"
				)
		).satisfies(
				artifact -> assertThat(mavenArtifactAvailableVersionsResolver.resolve(artifact, repositories))
						.isEqualTo(new ArtifactAvailableVersions(artifact, List.of("foo-version1", "foo-version2")))
		);
		assertThat(
				new Artifact<>(
						MavenArtifactType.BUILD_PLUGIN,
						new ArtifactIdentifier("foo-plugin-group-id", "foo-plugin-artifact-id"),
						"foo-plugin-version"
				)
		).satisfies(
				artifact -> assertThat(mavenArtifactAvailableVersionsResolver.resolve(artifact, repositories))
						.isEqualTo(new ArtifactAvailableVersions(artifact, List.of("foo-version1", "foo-version2")))
		);
	}

	@Test
	void testResolveInvalid() throws VersionRangeResolutionException {
		Mockito.when(mockedMavenSession.resolve(Mockito.notNull()))
				.thenAnswer(invocation -> invocation.getArgument(0));
		Mockito.when(mockedMavenSession.request(Mockito.notNull()))
				.thenThrow(VersionRangeResolutionException.class);
		final var mavenArtifactAvailableVersionsResolver = new MavenArtifactAvailableVersionsResolver(
				mockedMavenSession
		);
		final var artifact = new Artifact<>(
				MavenArtifactType.DEPENDENCY,
				new ArtifactIdentifier("foo-group-id", "foo-artifact-id"),
				"foo-version"
		);
		final var repositories = List.of(
				new Repository(RepositoryType.NORMAL, "foo-id", "https://foo-host"),
				new Repository(RepositoryType.PLUGIN, "foo-plugin-id", "https://foo-plugin-host")
		);
		assertThatNullPointerException()
				.isThrownBy(
						() -> mavenArtifactAvailableVersionsResolver.resolve(null, repositories)
				);
		assertThatNullPointerException()
				.isThrownBy(
						() -> mavenArtifactAvailableVersionsResolver.resolve(artifact, null)
				);
		assertThatNullPointerException()
				.isThrownBy(
						() -> mavenArtifactAvailableVersionsResolver.resolve(artifact, Collections.singletonList(null))
				);
		assertThatExceptionOfType(ArtifactAvailableVersionsResolveException.class)
				.isThrownBy(
						() -> mavenArtifactAvailableVersionsResolver.resolve(artifact, List.of())
				);
		assertThatExceptionOfType(AssertionError.class)
				.isThrownBy(
						() -> mavenArtifactAvailableVersionsResolver.resolve(artifact, repositories)
				)
				.withCauseInstanceOf(VersionRangeResolutionException.class);
	}
}