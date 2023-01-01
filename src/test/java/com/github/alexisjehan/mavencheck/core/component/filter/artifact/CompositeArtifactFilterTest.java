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

final class CompositeArtifactFilterTest {

	private static final Artifact<?> ARTIFACT = new Artifact<>(
			MavenArtifactType.DEPENDENCY,
			new ArtifactIdentifier("foo-group-id", "foo-artifact-id")
	);
	private static final String UPDATE_VERSION = "foo-version";

	private final CompositeArtifactFilter compositeArtifactFilter = new CompositeArtifactFilter(ArtifactFilter.ALL);

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException().isThrownBy(() -> new CompositeArtifactFilter((ArtifactFilter[]) null));
		assertThatIllegalArgumentException().isThrownBy(CompositeArtifactFilter::new);
		assertThatNullPointerException().isThrownBy(() -> new CompositeArtifactFilter((ArtifactFilter) null));
	}

	@Test
	void testAccept() {
		assertThat(new CompositeArtifactFilter(ArtifactFilter.ALL).accept(ARTIFACT)).isFalse();
		assertThat(new CompositeArtifactFilter(ArtifactFilter.NONE).accept(ARTIFACT)).isTrue();
		assertThat(new CompositeArtifactFilter(ArtifactFilter.ALL, ArtifactFilter.ALL).accept(ARTIFACT)).isFalse();
		assertThat(new CompositeArtifactFilter(ArtifactFilter.ALL, ArtifactFilter.NONE).accept(ARTIFACT)).isFalse();
		assertThat(new CompositeArtifactFilter(ArtifactFilter.NONE, ArtifactFilter.NONE).accept(ARTIFACT)).isTrue();
	}

	@Test
	void testAcceptInvalid() {
		assertThatNullPointerException().isThrownBy(() -> compositeArtifactFilter.accept(null));
	}

	@Test
	void testAcceptUpdateVersion() {
		assertThat(
				new CompositeArtifactFilter(ArtifactFilter.ALL).accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				new CompositeArtifactFilter(ArtifactFilter.NONE).accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new CompositeArtifactFilter(ArtifactFilter.ALL, ArtifactFilter.ALL).accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				new CompositeArtifactFilter(ArtifactFilter.ALL, ArtifactFilter.NONE).accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				new CompositeArtifactFilter(ArtifactFilter.NONE, ArtifactFilter.NONE).accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
	}

	@Test
	void testAcceptUpdateVersionInvalid() {
		assertThatNullPointerException().isThrownBy(() -> compositeArtifactFilter.accept(null, UPDATE_VERSION));
		assertThatNullPointerException().isThrownBy(() -> compositeArtifactFilter.accept(ARTIFACT, null));
		assertThatIllegalArgumentException().isThrownBy(() -> compositeArtifactFilter.accept(ARTIFACT, Strings.EMPTY));
	}
}