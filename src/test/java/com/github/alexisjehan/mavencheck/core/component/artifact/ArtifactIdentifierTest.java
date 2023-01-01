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
package com.github.alexisjehan.mavencheck.core.component.artifact;

import com.github.alexisjehan.javanilla.lang.Strings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class ArtifactIdentifierTest {

	private static final String GROUP_ID = "foo-group-id";
	private static final String ARTIFACT_ID = "foo-artifact-id";
	private static final String OTHER_GROUP_ID = "bar-group-id";
	private static final String OTHER_ARTIFACT_ID = "bar-artifact-id";

	private final ArtifactIdentifier artifactIdentifier = new ArtifactIdentifier(GROUP_ID, ARTIFACT_ID);

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException().isThrownBy(() -> new ArtifactIdentifier(null, ARTIFACT_ID));
		assertThatIllegalArgumentException().isThrownBy(() -> new ArtifactIdentifier(Strings.EMPTY, ARTIFACT_ID));
		assertThatNullPointerException().isThrownBy(() -> new ArtifactIdentifier(GROUP_ID, null));
		assertThatIllegalArgumentException().isThrownBy(() -> new ArtifactIdentifier(GROUP_ID, Strings.EMPTY));
	}

	@Test
	void testEqualsAndHashCodeAndToString() {
		assertThat(artifactIdentifier.equals(artifactIdentifier)).isTrue();
		assertThat(artifactIdentifier).isNotEqualTo(new Object());
		assertThat(new ArtifactIdentifier(GROUP_ID, ARTIFACT_ID)).satisfies(otherArtifactIdentifier -> {
			assertThat(artifactIdentifier).isNotSameAs(otherArtifactIdentifier);
			assertThat(artifactIdentifier).isEqualTo(otherArtifactIdentifier);
			assertThat(artifactIdentifier).hasSameHashCodeAs(otherArtifactIdentifier);
			assertThat(artifactIdentifier).hasToString(otherArtifactIdentifier.toString());
		});
		assertThat(new ArtifactIdentifier(OTHER_GROUP_ID, ARTIFACT_ID)).satisfies(otherArtifactIdentifier -> {
			assertThat(artifactIdentifier).isNotSameAs(otherArtifactIdentifier);
			assertThat(artifactIdentifier).isNotEqualTo(otherArtifactIdentifier);
			assertThat(artifactIdentifier).doesNotHaveSameHashCodeAs(otherArtifactIdentifier);
			assertThat(artifactIdentifier).doesNotHaveToString(otherArtifactIdentifier.toString());
		});
		assertThat(new ArtifactIdentifier(GROUP_ID, OTHER_ARTIFACT_ID)).satisfies(otherArtifactIdentifier -> {
			assertThat(artifactIdentifier).isNotSameAs(otherArtifactIdentifier);
			assertThat(artifactIdentifier).isNotEqualTo(otherArtifactIdentifier);
			assertThat(artifactIdentifier).doesNotHaveSameHashCodeAs(otherArtifactIdentifier);
			assertThat(artifactIdentifier).doesNotHaveToString(otherArtifactIdentifier.toString());
		});
	}

	@Test
	void testGetGroupId() {
		assertThat(artifactIdentifier.getGroupId()).isEqualTo(GROUP_ID);
	}

	@Test
	void testGetArtifactId() {
		assertThat(artifactIdentifier.getArtifactId()).isEqualTo(ARTIFACT_ID);
	}
}