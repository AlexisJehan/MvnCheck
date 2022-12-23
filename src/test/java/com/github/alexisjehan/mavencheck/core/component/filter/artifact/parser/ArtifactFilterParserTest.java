/*
 * MIT License
 *
 * Copyright (c) 2022 Alexis Jehan
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
package com.github.alexisjehan.mavencheck.core.component.filter.artifact.parser;

import com.github.alexisjehan.javanilla.lang.Strings;
import com.github.alexisjehan.mavencheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mavencheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mavencheck.core.component.artifact.type.MavenArtifactType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class ArtifactFilterParserTest {

	private static final Artifact<?> ARTIFACT = new Artifact<>(
			MavenArtifactType.DEPENDENCY,
			new ArtifactIdentifier("foo-group-id", "foo-artifact-id")
	);
	private static final String UPDATE_VERSION = "foo-version";

	@Test
	void testParsePath() {
		assertThatNoException().isThrownBy(
				() -> ArtifactFilterParser.parse(Path.of("src", "test", "resources", ".mvnchk-ignore_foo"))
		);
	}

	@Test
	void testParsePathInvalid() {
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilterParser.parse((Path) null));
		assertThat(Path.of("src", "test", "resources", ".mvnchk-ignore_not-found")).satisfies(
				ignoreFile -> assertThatIllegalArgumentException()
						.isThrownBy(() -> ArtifactFilterParser.parse(ignoreFile))
		);
		assertThat(Path.of("src", "test", "resources", ".mvnchk-ignore_error")).satisfies(
				ignoreFile -> assertThatExceptionOfType(ArtifactFilterParseException.class)
						.isThrownBy(() -> ArtifactFilterParser.parse(ignoreFile))
		);
	}

	@Test
	void testParseReader() {
		try (final var reader = new StringReader(Strings.EMPTY)) {
			assertThatNoException().isThrownBy(() -> ArtifactFilterParser.parse(reader));
		}
	}

	@Test
	void testParseReaderInvalid() {
		assertThatNullPointerException().isThrownBy(() -> ArtifactFilterParser.parse((Reader) null));
		try (final var reader = new StringReader("foo-group-id")) {
			assertThatExceptionOfType(ArtifactFilterParseException.class)
					.isThrownBy(() -> ArtifactFilterParser.parse(reader));
		}
		try (final var reader = new StringReader("foo-group-id:foo-artifact-id:foo-version:foo")) {
			assertThatExceptionOfType(ArtifactFilterParseException.class)
					.isThrownBy(() -> ArtifactFilterParser.parse(reader));
		}
		try (final var reader = new StringReader(":foo-artifact-id")) {
			assertThatExceptionOfType(ArtifactFilterParseException.class)
					.isThrownBy(() -> ArtifactFilterParser.parse(reader));
		}
		try (final var reader = new StringReader("foo-group-id:")) {
			assertThatExceptionOfType(ArtifactFilterParseException.class)
					.isThrownBy(() -> ArtifactFilterParser.parse(reader));
		}
		try (final var reader = new StringReader("foo-group-id:foo-artifact-id:")) {
			assertThatExceptionOfType(ArtifactFilterParseException.class)
					.isThrownBy(() -> ArtifactFilterParser.parse(reader));
		}
	}

	@Test
	void testParseReaderAccept() throws IOException {
		try (final var reader = new StringReader("#foo-group-id:foo-artifact-id")) {
			assertThat(ArtifactFilterParser.parse(reader).accept(ARTIFACT)).isTrue();
		}
		try (final var reader = new StringReader("foo-group-id:foo-artifact-id")) {
			assertThat(ArtifactFilterParser.parse(reader).accept(ARTIFACT)).isFalse();
		}
		try (final var reader = new StringReader("foo-group-id:bar-artifact-id")) {
			assertThat(ArtifactFilterParser.parse(reader).accept(ARTIFACT)).isTrue();
		}
	}

	@Test
	void testParseReaderAcceptInvalid() throws IOException {
		final var artifactFilter = ArtifactFilterParser.parse(new StringReader(Strings.EMPTY));
		assertThatNullPointerException().isThrownBy(() -> artifactFilter.accept(null));
	}

	@Test
	void testParseReaderAcceptUpdateVersion() throws IOException {
		try (final var reader = new StringReader("#foo-group-id:foo-artifact-id:foo-version")) {
			assertThat(ArtifactFilterParser.parse(reader).accept(ARTIFACT, UPDATE_VERSION)).isTrue();
		}
		try (final var reader = new StringReader("foo-group-id:foo-artifact-id:foo-version")) {
			assertThat(ArtifactFilterParser.parse(reader).accept(ARTIFACT, UPDATE_VERSION)).isFalse();
		}
		try (final var reader = new StringReader("foo-group-id:foo-artifact-id:bar-version")) {
			assertThat(ArtifactFilterParser.parse(reader).accept(ARTIFACT, UPDATE_VERSION)).isTrue();
		}
		try (final var reader = new StringReader("foo-group-id:foo-artifact-id:FOO-VERSION")) {
			assertThat(ArtifactFilterParser.parse(reader).accept(ARTIFACT, UPDATE_VERSION)).isFalse();
		}
		try (final var reader = new StringReader("foo-group-id:foo-artifact-id:foo-version?")) {
			assertThat(ArtifactFilterParser.parse(reader).accept(ARTIFACT, UPDATE_VERSION)).isTrue();
		}
		try (final var reader = new StringReader("foo-group-id:foo-artifact-id:foo-?")) {
			assertThat(ArtifactFilterParser.parse(reader).accept(ARTIFACT, UPDATE_VERSION)).isTrue();
		}
		try (final var reader = new StringReader("foo-group-id:foo-artifact-id:foo-???????")) {
			assertThat(ArtifactFilterParser.parse(reader).accept(ARTIFACT, UPDATE_VERSION)).isFalse();
		}
		try (final var reader = new StringReader("foo-group-id:foo-artifact-id:foo-version*")) {
			assertThat(ArtifactFilterParser.parse(reader).accept(ARTIFACT, UPDATE_VERSION)).isFalse();
		}
		try (final var reader = new StringReader("foo-group-id:foo-artifact-id:foo-*")) {
			assertThat(ArtifactFilterParser.parse(reader).accept(ARTIFACT, UPDATE_VERSION)).isFalse();
		}
		try (final var reader = new StringReader("foo-group-id:foo-artifact-id:foo-*******")) {
			assertThat(ArtifactFilterParser.parse(reader).accept(ARTIFACT, UPDATE_VERSION)).isFalse();
		}
	}

	@Test
	void testParseReaderAcceptUpdateVersionInvalid() throws IOException {
		final var artifactFilter = ArtifactFilterParser.parse(new StringReader(Strings.EMPTY));
		assertThatNullPointerException().isThrownBy(() -> artifactFilter.accept(null, UPDATE_VERSION));
		assertThatNullPointerException().isThrownBy(() -> artifactFilter.accept(ARTIFACT, null));
		assertThatIllegalArgumentException().isThrownBy(() -> artifactFilter.accept(ARTIFACT, Strings.EMPTY));
	}
}