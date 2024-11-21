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
package com.github.alexisjehan.mvncheck.core.component.artifact.version;

import com.github.alexisjehan.javanilla.lang.Strings;
import com.github.alexisjehan.mvncheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mvncheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mvncheck.core.component.artifact.type.ArtifactType;
import com.github.alexisjehan.mvncheck.core.component.artifact.type.MavenArtifactType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class ArtifactUpdateVersionTest {

	private static final Artifact<ArtifactType> ARTIFACT = new Artifact<>(
			MavenArtifactType.DEPENDENCY,
			new ArtifactIdentifier("foo-group-id", "foo-artifact-id")
	);

	private static final String UPDATE_VERSION = "foo-version";

	private static final Artifact<ArtifactType> OTHER_ARTIFACT = new Artifact<>(
			MavenArtifactType.BUILD_PLUGIN,
			new ArtifactIdentifier("bar-group-id", "bar-artifact-id")
	);

	private static final String OTHER_UPDATE_VERSION = "bar-version";

	private final ArtifactUpdateVersion artifactUpdateVersion = new ArtifactUpdateVersion(ARTIFACT, UPDATE_VERSION);

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException().isThrownBy(() -> new ArtifactUpdateVersion(null, UPDATE_VERSION));
		assertThatNullPointerException().isThrownBy(() -> new ArtifactUpdateVersion(ARTIFACT, null));
		assertThatIllegalArgumentException().isThrownBy(() -> new ArtifactUpdateVersion(ARTIFACT, Strings.EMPTY));
	}

	@Test
	void testEqualsAndHashCodeAndToString() {
		assertThat(artifactUpdateVersion.equals(artifactUpdateVersion)).isTrue();
		assertThat(artifactUpdateVersion).isNotEqualTo(new Object());
		assertThat(new ArtifactUpdateVersion(ARTIFACT, UPDATE_VERSION))
				.satisfies(otherArtifactUpdateVersion -> {
					assertThat(otherArtifactUpdateVersion).isNotSameAs(artifactUpdateVersion);
					assertThat(otherArtifactUpdateVersion).isEqualTo(artifactUpdateVersion);
					assertThat(otherArtifactUpdateVersion).hasSameHashCodeAs(artifactUpdateVersion);
					assertThat(otherArtifactUpdateVersion).hasToString(artifactUpdateVersion.toString());
				});
		assertThat(new ArtifactUpdateVersion(OTHER_ARTIFACT, UPDATE_VERSION))
				.satisfies(otherArtifactUpdateVersion -> {
					assertThat(otherArtifactUpdateVersion).isNotSameAs(artifactUpdateVersion);
					assertThat(otherArtifactUpdateVersion).isNotEqualTo(artifactUpdateVersion);
					assertThat(otherArtifactUpdateVersion).doesNotHaveSameHashCodeAs(artifactUpdateVersion);
					assertThat(otherArtifactUpdateVersion).doesNotHaveToString(artifactUpdateVersion.toString());
				});
		assertThat(new ArtifactUpdateVersion(ARTIFACT, OTHER_UPDATE_VERSION))
				.satisfies(otherArtifactUpdateVersion -> {
					assertThat(otherArtifactUpdateVersion).isNotSameAs(artifactUpdateVersion);
					assertThat(otherArtifactUpdateVersion).isNotEqualTo(artifactUpdateVersion);
					assertThat(otherArtifactUpdateVersion).doesNotHaveSameHashCodeAs(artifactUpdateVersion);
					assertThat(otherArtifactUpdateVersion).doesNotHaveToString(artifactUpdateVersion.toString());
				});
	}

	@Test
	void testGetArtifact() {
		assertThat(artifactUpdateVersion.getArtifact()).isEqualTo(ARTIFACT);
	}

	@Test
	void testGetUpdateVersion() {
		assertThat(artifactUpdateVersion.getUpdateVersion()).isEqualTo(UPDATE_VERSION);
	}
}