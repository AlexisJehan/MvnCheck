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
package com.github.alexisjehan.mvncheck.core.component.artifact;

import com.github.alexisjehan.mvncheck.core.component.artifact.type.MavenArtifactType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class ArtifactTest {

	private static final MavenArtifactType TYPE = MavenArtifactType.DEPENDENCY;
	private static final ArtifactIdentifier IDENTIFIER = new ArtifactIdentifier(
			"foo-group-id",
			"foo-artifact-id"
	);
	private static final String VERSION = "foo-version";
	private static final MavenArtifactType OTHER_TYPE = MavenArtifactType.BUILD_PLUGIN;
	private static final ArtifactIdentifier OTHER_IDENTIFIER = new ArtifactIdentifier(
			"bar-group-id",
			"bar-artifact-id"
	);

	private final Artifact<MavenArtifactType> artifact = new Artifact<>(TYPE, IDENTIFIER, VERSION);

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException().isThrownBy(() -> new Artifact<>(null, IDENTIFIER, VERSION));
		assertThatNullPointerException().isThrownBy(() -> new Artifact<>(TYPE, null, VERSION));
	}

	@Test
	void testWith() {
		assertThat(artifact.with(TYPE)).satisfies(otherArtifact -> {
			assertThat(otherArtifact.getType()).isEqualTo(TYPE);
			assertThat(otherArtifact.getIdentifier()).isEqualTo(artifact.getIdentifier());
			assertThat(otherArtifact.getOptionalVersion()).isEqualTo(artifact.getOptionalVersion());
		});
		assertThat(artifact.with(OTHER_TYPE)).satisfies(otherArtifact -> {
			assertThat(otherArtifact.getType()).isEqualTo(OTHER_TYPE);
			assertThat(otherArtifact.getIdentifier()).isEqualTo(artifact.getIdentifier());
			assertThat(otherArtifact.getOptionalVersion()).isEqualTo(artifact.getOptionalVersion());
		});
	}

	@Test
	void testEqualsAndHashCodeAndToString() {
		assertThat(artifact.equals(artifact)).isTrue();
		assertThat(artifact).isNotEqualTo(new Object());
		assertThat(new Artifact<>(TYPE, IDENTIFIER, VERSION)).satisfies(otherArtifact -> {
			assertThat(otherArtifact).isNotSameAs(artifact);
			assertThat(otherArtifact).isEqualTo(artifact);
			assertThat(otherArtifact).hasSameHashCodeAs(artifact);
			assertThat(otherArtifact).hasToString(artifact.toString());
		});
		assertThat(new Artifact<>(OTHER_TYPE, IDENTIFIER, VERSION)).satisfies(otherArtifact -> {
			assertThat(otherArtifact).isNotSameAs(artifact);
			assertThat(otherArtifact).isNotEqualTo(artifact);
			assertThat(otherArtifact).doesNotHaveSameHashCodeAs(artifact);
			assertThat(otherArtifact).doesNotHaveToString(artifact.toString());
		});
		assertThat(new Artifact<>(TYPE, OTHER_IDENTIFIER, VERSION)).satisfies(otherArtifact -> {
			assertThat(otherArtifact).isNotSameAs(artifact);
			assertThat(otherArtifact).isNotEqualTo(artifact);
			assertThat(otherArtifact).doesNotHaveSameHashCodeAs(artifact);
			assertThat(otherArtifact).doesNotHaveToString(artifact.toString());
		});
		assertThat(new Artifact<>(TYPE, IDENTIFIER)).satisfies(otherArtifact -> {
			assertThat(otherArtifact).isNotSameAs(artifact);
			assertThat(otherArtifact).isNotEqualTo(artifact);
			assertThat(otherArtifact).doesNotHaveSameHashCodeAs(artifact);
			assertThat(otherArtifact).doesNotHaveToString(artifact.toString());
		});
	}

	@Test
	void testGetType() {
		assertThat(artifact.getType()).isEqualTo(TYPE);
	}

	@Test
	void testGetIdentifier() {
		assertThat(artifact.getIdentifier()).isEqualTo(IDENTIFIER);
	}

	@Test
	void testGetOptionalVersion() {
		assertThat(artifact.getOptionalVersion()).hasValue(VERSION);
	}
}