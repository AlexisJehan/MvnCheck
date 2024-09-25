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

final class WildcardArtifactFilterTest {

	private static final String GROUP_ID = "foo-group-id";
	private static final String ARTIFACT_ID = "foo-artifact-id";
	private static final Artifact<ArtifactType> ARTIFACT = new Artifact<>(
			MavenArtifactType.DEPENDENCY,
			new ArtifactIdentifier(GROUP_ID, ARTIFACT_ID)
	);
	private static final String UPDATE_VERSION = "foo-version";

	private final WildcardArtifactFilter wildcardArtifactFilter = new WildcardArtifactFilter("*");

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException()
				.isThrownBy(() -> new WildcardArtifactFilter(null));
		assertThatIllegalArgumentException()
				.isThrownBy(() -> new WildcardArtifactFilter(Strings.EMPTY));
		assertThatIllegalArgumentException()
				.isThrownBy(() -> new WildcardArtifactFilter(GROUP_ID, Strings.EMPTY));
		assertThatIllegalArgumentException()
				.isThrownBy(() -> new WildcardArtifactFilter(GROUP_ID, ARTIFACT_ID, Strings.EMPTY));
	}

	@Test
	void testAccept() {
		assertThat(
				new WildcardArtifactFilter("foo-group-id")
						.accept(ARTIFACT)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter("bar-group-id")
						.accept(ARTIFACT)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter("FOO-GROUP-ID")
						.accept(ARTIFACT)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter("foo-group-id?")
						.accept(ARTIFACT)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter("foo-group-?")
						.accept(ARTIFACT)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter("foo-group-??")
						.accept(ARTIFACT)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter("foo-group-id*")
						.accept(ARTIFACT)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter("foo-group-*")
						.accept(ARTIFACT)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter("foo-group-**")
						.accept(ARTIFACT)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "foo-artifact-id")
						.accept(ARTIFACT)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "bar-artifact-id")
						.accept(ARTIFACT)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "FOO-ARTIFACT-ID")
						.accept(ARTIFACT)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "foo-artifact-id?")
						.accept(ARTIFACT)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "foo-artifact-?")
						.accept(ARTIFACT)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "foo-artifact-??")
						.accept(ARTIFACT)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "foo-artifact-id*")
						.accept(ARTIFACT)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "foo-artifact-*")
						.accept(ARTIFACT)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "foo-artifact-**")
						.accept(ARTIFACT)
		).isTrue();
	}

	@Test
	void testAcceptInvalid() {
		assertThatNullPointerException().isThrownBy(() -> wildcardArtifactFilter.accept(null));
	}

	@Test
	void testAcceptUpdateVersion() {
		assertThat(
				new WildcardArtifactFilter("foo-group-id")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter("bar-group-id")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter("FOO-GROUP-ID")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter("foo-group-id?")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter("foo-group-?")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter("foo-group-??")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter("foo-group-id*")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter("foo-group-*")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter("foo-group-**")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "foo-artifact-id")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "bar-artifact-id")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "FOO-ARTIFACT-ID")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "foo-artifact-id?")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "foo-artifact-?")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "foo-artifact-??")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "foo-artifact-id*")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "foo-artifact-*")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, "foo-artifact-**")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, ARTIFACT_ID, "foo-version")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, ARTIFACT_ID, "bar-version")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, ARTIFACT_ID, "FOO-VERSION")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, ARTIFACT_ID, "foo-version?")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, ARTIFACT_ID, "foo-?")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isFalse();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, ARTIFACT_ID, "foo-???????")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, ARTIFACT_ID, "foo-version*")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, ARTIFACT_ID, "foo-*")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
		assertThat(
				new WildcardArtifactFilter(GROUP_ID, ARTIFACT_ID, "foo-*******")
						.accept(ARTIFACT, UPDATE_VERSION)
		).isTrue();
	}

	@Test
	void testAcceptUpdateVersionInvalid() {
		assertThatNullPointerException().isThrownBy(() -> wildcardArtifactFilter.accept(null, UPDATE_VERSION));
		assertThatNullPointerException().isThrownBy(() -> wildcardArtifactFilter.accept(ARTIFACT, null));
		assertThatIllegalArgumentException().isThrownBy(() -> wildcardArtifactFilter.accept(ARTIFACT, Strings.EMPTY));
	}
}