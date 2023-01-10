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
package com.github.alexisjehan.mavencheck.core.component.build;

import com.github.alexisjehan.mavencheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mavencheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mavencheck.core.component.artifact.type.ArtifactType;
import com.github.alexisjehan.mavencheck.core.component.artifact.type.MavenArtifactType;
import com.github.alexisjehan.mavencheck.core.component.build.file.BuildFile;
import com.github.alexisjehan.mavencheck.core.component.build.file.BuildFileType;
import com.github.alexisjehan.mavencheck.core.component.repository.Repository;
import com.github.alexisjehan.mavencheck.core.component.repository.RepositoryType;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class BuildTest {

	private static final BuildFile FILE = new BuildFile(BuildFileType.MAVEN, Path.of("foo-file"));
	private static final List<Repository> REPOSITORIES = List.of(
			new Repository(RepositoryType.NORMAL, "foo-id", "https://foo-host")
	);
	private static final List<Artifact<ArtifactType>> ARTIFACTS = List.of(
			new Artifact<>(
					MavenArtifactType.DEPENDENCY,
					new ArtifactIdentifier("foo-group-id", "foo-artifact-id"),
					"foo-version"
			)
	);
	private static final BuildFile OTHER_FILE = new BuildFile(BuildFileType.GRADLE_GROOVY, Path.of("bar-file"));
	private static final List<Repository> OTHER_REPOSITORIES = List.of(
			new Repository(RepositoryType.PLUGIN, "bar-id", "https://bar-host")
	);
	private static final List<Artifact<ArtifactType>> OTHER_ARTIFACTS = List.of(
			new Artifact<>(
					MavenArtifactType.BUILD_PLUGIN,
					new ArtifactIdentifier("bar-group-id", "bar-artifact-id")
			)
	);

	private final Build build = new Build(FILE, REPOSITORIES, ARTIFACTS);

	@Test
	void testConstructorImmutable() {
		final var repositories = new ArrayList<>(REPOSITORIES);
		final var artifacts = new ArrayList<>(ARTIFACTS);
		final var build = new Build(FILE, repositories, artifacts);
		repositories.clear();
		artifacts.clear();
		assertThat(build.getRepositories()).containsExactlyElementsOf(REPOSITORIES);
		assertThat(build.getArtifacts()).containsExactlyElementsOf(ARTIFACTS);
	}

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException()
				.isThrownBy(() -> new Build(null, REPOSITORIES, ARTIFACTS));
		assertThatNullPointerException()
				.isThrownBy(() -> new Build(FILE, null, ARTIFACTS));
		assertThatNullPointerException()
				.isThrownBy(() -> new Build(FILE, Collections.singletonList(null), ARTIFACTS));
		assertThatNullPointerException()
				.isThrownBy(() -> new Build(FILE, REPOSITORIES, null));
		assertThatNullPointerException()
				.isThrownBy(() -> new Build(FILE, REPOSITORIES, Collections.singletonList(null)));
	}

	@Test
	void testEqualsAndHashCodeAndToString() {
		assertThat(build.equals(build)).isTrue();
		assertThat(build).isNotEqualTo(new Object());
		assertThat(new Build(FILE, REPOSITORIES, ARTIFACTS)).satisfies(otherBuild -> {
			assertThat(build).isNotSameAs(otherBuild);
			assertThat(build).isEqualTo(otherBuild);
			assertThat(build).hasSameHashCodeAs(otherBuild);
			assertThat(build).hasToString(otherBuild.toString());
		});
		assertThat(new Build(OTHER_FILE, REPOSITORIES, ARTIFACTS)).satisfies(otherBuild -> {
			assertThat(build).isNotSameAs(otherBuild);
			assertThat(build).isNotEqualTo(otherBuild);
			assertThat(build).doesNotHaveSameHashCodeAs(otherBuild);
			assertThat(build).doesNotHaveToString(otherBuild.toString());
		});
		assertThat(new Build(FILE, OTHER_REPOSITORIES, ARTIFACTS)).satisfies(otherBuild -> {
			assertThat(build).isNotSameAs(otherBuild);
			assertThat(build).isNotEqualTo(otherBuild);
			assertThat(build).doesNotHaveSameHashCodeAs(otherBuild);
			assertThat(build).doesNotHaveToString(otherBuild.toString());
		});
		assertThat(new Build(FILE, REPOSITORIES, OTHER_ARTIFACTS)).satisfies(otherBuild -> {
			assertThat(build).isNotSameAs(otherBuild);
			assertThat(build).isNotEqualTo(otherBuild);
			assertThat(build).doesNotHaveSameHashCodeAs(otherBuild);
			assertThat(build).doesNotHaveToString(otherBuild.toString());
		});
	}

	@Test
	void testGetFile() {
		assertThat(build.getFile()).isEqualTo(FILE);
	}

	@Test
	void testGetRepositories() {
		assertThat(build.getRepositories()).isEqualTo(REPOSITORIES);
	}

	@Test
	void testGetRepositoriesImmutable() {
		final var repositories = build.getRepositories();
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(repositories::clear);
	}

	@Test
	void testGetArtifacts() {
		assertThat(build.getArtifacts()).isEqualTo(ARTIFACTS);
	}

	@Test
	void testGetArtifactsImmutable() {
		final var artifacts = build.getArtifacts();
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(artifacts::clear);
	}
}