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
package com.github.alexisjehan.mavencheck.core.component.filter.artifact;

import com.github.alexisjehan.javanilla.lang.Strings;
import com.github.alexisjehan.mavencheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mavencheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mavencheck.core.component.artifact.type.MavenArtifactType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class ArtifactFilterTest {

	private static final Artifact<?> ARTIFACT = new Artifact<>(
			MavenArtifactType.DEPENDENCY,
			new ArtifactIdentifier("foo-group-id", "foo-artifact-id")
	);
	private static final String UPDATE_VERSION = "foo-version";

	@Test
	void testAllAccept() {
		assertThat(ArtifactFilter.ALL.accept(ARTIFACT)).isFalse();
	}

	@Test
	void testAllAcceptInvalid() {
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilter.ALL.accept(null));
	}

	@Test
	void testAllAcceptUpdateVersion() {
		assertThat(ArtifactFilter.ALL.accept(ARTIFACT, UPDATE_VERSION)).isFalse();
	}

	@Test
	void testAllAcceptUpdateVersionInvalid() {
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilter.ALL.accept(null, UPDATE_VERSION));
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilter.ALL.accept(ARTIFACT, null));
		assertThatIllegalArgumentException().isThrownBy(() -> ArtifactFilter.ALL.accept(ARTIFACT, Strings.EMPTY));
	}

	@Test
	void testNoneAccept() {
		assertThat(ArtifactFilter.NONE.accept(ARTIFACT)).isTrue();
	}

	@Test
	void testNoneAcceptInvalid() {
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilter.NONE.accept(null));
	}

	@Test
	void testNoneAcceptUpdateVersion() {
		assertThat(ArtifactFilter.NONE.accept(ARTIFACT, UPDATE_VERSION)).isTrue();
	}

	@Test
	void testNoneAcceptUpdateVersionInvalid() {
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilter.NONE.accept(null, UPDATE_VERSION));
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilter.NONE.accept(ARTIFACT, null));
		assertThatIllegalArgumentException().isThrownBy(() -> ArtifactFilter.NONE.accept(ARTIFACT, Strings.EMPTY));
	}
}