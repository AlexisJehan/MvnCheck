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
package com.github.alexisjehan.mavencheck.core.component.artifact.version;

import com.github.alexisjehan.mavencheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mavencheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mavencheck.core.component.artifact.type.ArtifactType;
import com.github.alexisjehan.mavencheck.core.component.artifact.type.MavenArtifactType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class ArtifactAvailableVersionsTest {

	private static final Artifact<ArtifactType> ARTIFACT = new Artifact<>(
			MavenArtifactType.DEPENDENCY,
			new ArtifactIdentifier("foo-group-id", "foo-artifact-id")
	);
	private static final List<String> AVAILABLE_VERSIONS = List.of("foo-version");
	private static final Artifact<ArtifactType> OTHER_ARTIFACT = new Artifact<>(
			MavenArtifactType.BUILD_PLUGIN,
			new ArtifactIdentifier("bar-group-id", "bar-artifact-id")
	);
	private static final List<String> OTHER_AVAILABLE_VERSIONS = List.of();

	private final ArtifactAvailableVersions artifactAvailableVersions = new ArtifactAvailableVersions(
			ARTIFACT,
			AVAILABLE_VERSIONS
	);

	@Test
	void testConstructorImmutable() {
		final var availableVersions = new ArrayList<>(AVAILABLE_VERSIONS);
		final var artifactAvailableVersions = new ArtifactAvailableVersions(ARTIFACT, availableVersions);
		availableVersions.clear();
		assertThat(artifactAvailableVersions.getAvailableVersions()).containsExactlyElementsOf(AVAILABLE_VERSIONS);
	}

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException()
				.isThrownBy(() -> new ArtifactAvailableVersions(null, AVAILABLE_VERSIONS));
		assertThatNullPointerException()
				.isThrownBy(() -> new ArtifactAvailableVersions(ARTIFACT, null));
		assertThatNullPointerException()
				.isThrownBy(() -> new ArtifactAvailableVersions(ARTIFACT, Collections.singletonList(null)));
	}

	@Test
	void testEqualsAndHashCodeAndToString() {
		assertThat(artifactAvailableVersions.equals(artifactAvailableVersions)).isTrue();
		assertThat(artifactAvailableVersions).isNotEqualTo(new Object());
		assertThat(new ArtifactAvailableVersions(ARTIFACT, AVAILABLE_VERSIONS))
				.satisfies(otherArtifactAvailableVersions -> {
					assertThat(artifactAvailableVersions)
							.isNotSameAs(otherArtifactAvailableVersions);
					assertThat(artifactAvailableVersions)
							.isEqualTo(otherArtifactAvailableVersions);
					assertThat(artifactAvailableVersions)
							.hasSameHashCodeAs(otherArtifactAvailableVersions);
					assertThat(artifactAvailableVersions)
							.hasToString(otherArtifactAvailableVersions.toString());
				});
		assertThat(new ArtifactAvailableVersions(OTHER_ARTIFACT, AVAILABLE_VERSIONS))
				.satisfies(otherArtifactAvailableVersions -> {
					assertThat(artifactAvailableVersions)
							.isNotSameAs(otherArtifactAvailableVersions);
					assertThat(artifactAvailableVersions)
							.isNotEqualTo(otherArtifactAvailableVersions);
					assertThat(artifactAvailableVersions)
							.doesNotHaveSameHashCodeAs(otherArtifactAvailableVersions);
					assertThat(artifactAvailableVersions)
							.doesNotHaveToString(otherArtifactAvailableVersions.toString());
				});
		assertThat(new ArtifactAvailableVersions(ARTIFACT, OTHER_AVAILABLE_VERSIONS))
				.satisfies(otherArtifactAvailableVersions -> {
					assertThat(artifactAvailableVersions)
							.isNotSameAs(otherArtifactAvailableVersions);
					assertThat(artifactAvailableVersions)
							.isNotEqualTo(otherArtifactAvailableVersions);
					assertThat(artifactAvailableVersions)
							.doesNotHaveSameHashCodeAs(otherArtifactAvailableVersions);
					assertThat(artifactAvailableVersions)
							.doesNotHaveToString(otherArtifactAvailableVersions.toString());
				});
	}

	@Test
	void testGetArtifact() {
		assertThat(artifactAvailableVersions.getArtifact()).isEqualTo(ARTIFACT);
	}

	@Test
	void testGetAvailableVersions() {
		assertThat(artifactAvailableVersions.getAvailableVersions()).isEqualTo(AVAILABLE_VERSIONS);
	}

	@Test
	void testGetAvailableVersionsImmutable() {
		final var availableVersions = artifactAvailableVersions.getAvailableVersions();
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(availableVersions::clear);
	}
}