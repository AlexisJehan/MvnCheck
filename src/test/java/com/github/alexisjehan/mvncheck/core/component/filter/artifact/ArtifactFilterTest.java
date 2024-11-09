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
package com.github.alexisjehan.mvncheck.core.component.filter.artifact;

import com.github.alexisjehan.javanilla.lang.Strings;
import com.github.alexisjehan.mvncheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mvncheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mvncheck.core.component.artifact.type.ArtifactType;
import com.github.alexisjehan.mvncheck.core.component.artifact.type.MavenArtifactType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class ArtifactFilterTest {

	private static final Artifact<ArtifactType> ARTIFACT = new Artifact<>(
			MavenArtifactType.DEPENDENCY,
			new ArtifactIdentifier("foo-group-id", "foo-artifact-id")
	);

	private static final String UPDATE_VERSION = "foo-version";

	@Test
	void testAcceptAllAccept() {
		assertThat(ArtifactFilter.ACCEPT_ALL.accept(ARTIFACT)).isTrue();
	}

	@Test
	void testAcceptAllAcceptInvalid() {
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilter.ACCEPT_ALL.accept(null));
	}

	@Test
	void testAcceptAllAcceptUpdateVersion() {
		assertThat(ArtifactFilter.ACCEPT_ALL.accept(ARTIFACT, UPDATE_VERSION)).isTrue();
	}

	@Test
	void testAcceptAllAcceptUpdateVersionInvalid() {
		assertThatNullPointerException()
				.isThrownBy(() -> ArtifactFilter.ACCEPT_ALL.accept(null, UPDATE_VERSION));
		assertThatNullPointerException()
				.isThrownBy(() -> ArtifactFilter.ACCEPT_ALL.accept(ARTIFACT, null));
		assertThatIllegalArgumentException()
				.isThrownBy(() -> ArtifactFilter.ACCEPT_ALL.accept(ARTIFACT, Strings.EMPTY));
	}

	@Test
	void testAcceptNoneAccept() {
		assertThat(ArtifactFilter.ACCEPT_NONE.accept(ARTIFACT)).isFalse();
	}

	@Test
	void testAcceptNoneAcceptInvalid() {
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilter.ACCEPT_NONE.accept(null));
	}

	@Test
	void testAcceptNoneAcceptUpdateVersion() {
		assertThat(ArtifactFilter.ACCEPT_NONE.accept(ARTIFACT, UPDATE_VERSION)).isFalse();
	}

	@Test
	void testAcceptNoneAcceptUpdateVersionInvalid() {
		assertThatNullPointerException()
				.isThrownBy(() -> ArtifactFilter.ACCEPT_NONE.accept(null, UPDATE_VERSION));
		assertThatNullPointerException()
				.isThrownBy(() -> ArtifactFilter.ACCEPT_NONE.accept(ARTIFACT, null));
		assertThatIllegalArgumentException()
				.isThrownBy(() -> ArtifactFilter.ACCEPT_NONE.accept(ARTIFACT, Strings.EMPTY));
	}

	@Test
	@Deprecated
	void testAllAccept() {
		assertThat(ArtifactFilter.ALL.accept(ARTIFACT)).isFalse();
	}

	@Test
	@Deprecated
	void testAllAcceptInvalid() {
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilter.ALL.accept(null));
	}

	@Test
	@Deprecated
	void testAllAcceptUpdateVersion() {
		assertThat(ArtifactFilter.ALL.accept(ARTIFACT, UPDATE_VERSION)).isFalse();
	}

	@Test
	@Deprecated
	void testAllAcceptUpdateVersionInvalid() {
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilter.ALL.accept(null, UPDATE_VERSION));
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilter.ALL.accept(ARTIFACT, null));
		assertThatIllegalArgumentException().isThrownBy(() -> ArtifactFilter.ALL.accept(ARTIFACT, Strings.EMPTY));
	}

	@Test
	@Deprecated
	void testNoneAccept() {
		assertThat(ArtifactFilter.NONE.accept(ARTIFACT)).isTrue();
	}

	@Test
	@Deprecated
	void testNoneAcceptInvalid() {
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilter.NONE.accept(null));
	}

	@Test
	@Deprecated
	void testNoneAcceptUpdateVersion() {
		assertThat(ArtifactFilter.NONE.accept(ARTIFACT, UPDATE_VERSION)).isTrue();
	}

	@Test
	@Deprecated
	void testNoneAcceptUpdateVersionInvalid() {
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilter.NONE.accept(null, UPDATE_VERSION));
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilter.NONE.accept(ARTIFACT, null));
		assertThatIllegalArgumentException().isThrownBy(() -> ArtifactFilter.NONE.accept(ARTIFACT, Strings.EMPTY));
	}
}