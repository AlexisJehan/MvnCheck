/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Alexis Jehan
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

final class CompositeArtifactFilterTest {

	private static final Artifact<ArtifactType> ARTIFACT = new Artifact<>(
			MavenArtifactType.DEPENDENCY,
			new ArtifactIdentifier("foo-group-id", "foo-artifact-id")
	);

	private static final String UPDATE_VERSION = "foo-version";

	private final CompositeArtifactFilter compositeArtifactFilter = CompositeArtifactFilter.all(
			ArtifactFilter.ACCEPT_NONE
	);

	@Test
	@Deprecated
	void testConstructor() {
		final var otherCompositeArtifactFilter = new CompositeArtifactFilter(ArtifactFilter.ACCEPT_NONE);
		assertThat(otherCompositeArtifactFilter.accept(ARTIFACT)).isFalse();
		assertThat(otherCompositeArtifactFilter.accept(ARTIFACT, UPDATE_VERSION)).isFalse();
	}

	@Test
	@Deprecated
	void testConstructorInvalid() {
		assertThatNullPointerException().isThrownBy(() -> new CompositeArtifactFilter((ArtifactFilter[]) null));
		assertThatIllegalArgumentException().isThrownBy(CompositeArtifactFilter::new);
		assertThatNullPointerException().isThrownBy(() -> new CompositeArtifactFilter((ArtifactFilter) null));
	}

	@Test
	void testAccept() {
		assertThat(
				CompositeArtifactFilter.all(ArtifactFilter.ACCEPT_NONE).accept(ARTIFACT)
		).isFalse();
		assertThat(
				CompositeArtifactFilter.all(ArtifactFilter.ACCEPT_ALL).accept(ARTIFACT)
		).isTrue();
		assertThat(
				CompositeArtifactFilter.all(ArtifactFilter.ACCEPT_NONE, ArtifactFilter.ACCEPT_NONE).accept(ARTIFACT)
		).isFalse();
		assertThat(
				CompositeArtifactFilter.all(ArtifactFilter.ACCEPT_NONE, ArtifactFilter.ACCEPT_ALL).accept(ARTIFACT)
		).isFalse();
		assertThat(
				CompositeArtifactFilter.all(ArtifactFilter.ACCEPT_ALL, ArtifactFilter.ACCEPT_ALL).accept(ARTIFACT)
		).isTrue();
		assertThat(
				CompositeArtifactFilter.any(ArtifactFilter.ACCEPT_NONE).accept(ARTIFACT)
		).isFalse();
		assertThat(
				CompositeArtifactFilter.any(ArtifactFilter.ACCEPT_ALL).accept(ARTIFACT)
		).isTrue();
		assertThat(
				CompositeArtifactFilter.any(ArtifactFilter.ACCEPT_NONE, ArtifactFilter.ACCEPT_NONE).accept(ARTIFACT)
		).isFalse();
		assertThat(
				CompositeArtifactFilter.any(ArtifactFilter.ACCEPT_NONE, ArtifactFilter.ACCEPT_ALL).accept(ARTIFACT)
		).isTrue();
		assertThat(
				CompositeArtifactFilter.any(ArtifactFilter.ACCEPT_ALL, ArtifactFilter.ACCEPT_ALL).accept(ARTIFACT)
		).isTrue();
		assertThat(
				CompositeArtifactFilter.none(ArtifactFilter.ACCEPT_NONE).accept(ARTIFACT)
		).isTrue();
		assertThat(
				CompositeArtifactFilter.none(ArtifactFilter.ACCEPT_ALL).accept(ARTIFACT)
		).isFalse();
		assertThat(
				CompositeArtifactFilter.none(ArtifactFilter.ACCEPT_NONE, ArtifactFilter.ACCEPT_NONE).accept(ARTIFACT)
		).isTrue();
		assertThat(
				CompositeArtifactFilter.none(ArtifactFilter.ACCEPT_NONE, ArtifactFilter.ACCEPT_ALL).accept(ARTIFACT)
		).isFalse();
		assertThat(
				CompositeArtifactFilter.none(ArtifactFilter.ACCEPT_ALL, ArtifactFilter.ACCEPT_ALL).accept(ARTIFACT)
		).isFalse();
	}

	@Test
	void testAcceptInvalid() {
		assertThatNullPointerException().isThrownBy(() -> compositeArtifactFilter.accept(null));
	}

	@Test
	void testAcceptUpdateVersion() {
		assertThat(
				CompositeArtifactFilter.all(ArtifactFilter.ACCEPT_NONE)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				CompositeArtifactFilter.all(ArtifactFilter.ACCEPT_ALL)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				CompositeArtifactFilter.all(ArtifactFilter.ACCEPT_NONE, ArtifactFilter.ACCEPT_NONE)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				CompositeArtifactFilter.all(ArtifactFilter.ACCEPT_NONE, ArtifactFilter.ACCEPT_ALL)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				CompositeArtifactFilter.all(ArtifactFilter.ACCEPT_ALL, ArtifactFilter.ACCEPT_ALL)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				CompositeArtifactFilter.any(ArtifactFilter.ACCEPT_NONE)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				CompositeArtifactFilter.any(ArtifactFilter.ACCEPT_ALL)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				CompositeArtifactFilter.any(ArtifactFilter.ACCEPT_NONE, ArtifactFilter.ACCEPT_NONE)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				CompositeArtifactFilter.any(ArtifactFilter.ACCEPT_NONE, ArtifactFilter.ACCEPT_ALL)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				CompositeArtifactFilter.any(ArtifactFilter.ACCEPT_ALL, ArtifactFilter.ACCEPT_ALL)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				CompositeArtifactFilter.none(ArtifactFilter.ACCEPT_NONE)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				CompositeArtifactFilter.none(ArtifactFilter.ACCEPT_ALL)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				CompositeArtifactFilter.none(ArtifactFilter.ACCEPT_NONE, ArtifactFilter.ACCEPT_NONE)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				CompositeArtifactFilter.none(ArtifactFilter.ACCEPT_NONE, ArtifactFilter.ACCEPT_ALL)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				CompositeArtifactFilter.none(ArtifactFilter.ACCEPT_ALL, ArtifactFilter.ACCEPT_ALL)
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
	}

	@Test
	void testAcceptUpdateVersionInvalid() {
		assertThatNullPointerException().isThrownBy(() -> compositeArtifactFilter.accept(null, UPDATE_VERSION));
		assertThatNullPointerException().isThrownBy(() -> compositeArtifactFilter.accept(ARTIFACT, null));
		assertThatIllegalArgumentException().isThrownBy(() -> compositeArtifactFilter.accept(ARTIFACT, Strings.EMPTY));
	}

	@Test
	void testAllInvalid() {
		assertThatNullPointerException().isThrownBy(() -> CompositeArtifactFilter.all((ArtifactFilter[]) null));
		assertThatIllegalArgumentException().isThrownBy(CompositeArtifactFilter::all);
		assertThatNullPointerException().isThrownBy(() -> CompositeArtifactFilter.all((ArtifactFilter) null));
	}

	@Test
	void testAnyInvalid() {
		assertThatNullPointerException().isThrownBy(() -> CompositeArtifactFilter.any((ArtifactFilter[]) null));
		assertThatIllegalArgumentException().isThrownBy(CompositeArtifactFilter::any);
		assertThatNullPointerException().isThrownBy(() -> CompositeArtifactFilter.any((ArtifactFilter) null));
	}

	@Test
	void testNoneInvalid() {
		assertThatNullPointerException().isThrownBy(() -> CompositeArtifactFilter.none((ArtifactFilter[]) null));
		assertThatIllegalArgumentException().isThrownBy(CompositeArtifactFilter::none);
		assertThatNullPointerException().isThrownBy(() -> CompositeArtifactFilter.none((ArtifactFilter) null));
	}
}