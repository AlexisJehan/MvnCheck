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
package com.github.alexisjehan.mavencheck.core.component.filter.artifact.parser;

import com.github.alexisjehan.javanilla.io.Serializables;
import com.github.alexisjehan.javanilla.lang.Strings;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class ArtifactFilterParseExceptionTest {

	private static final String REASON = "foo-reason";
	private static final String LINE = "foo-line";
	private static final long LINE_NUMBER = 1;
	private static final Path FILE = Path.of("foo-file");

	private final ArtifactFilterParseException artifactFilterParseException = new ArtifactFilterParseException(
			REASON,
			LINE,
			LINE_NUMBER
	);

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException().isThrownBy(() -> {
			throw new ArtifactFilterParseException(null, LINE, LINE_NUMBER);
		});
		assertThatIllegalArgumentException().isThrownBy(() -> {
			throw new ArtifactFilterParseException(Strings.EMPTY, LINE, LINE_NUMBER);
		});
		assertThatNullPointerException().isThrownBy(() -> {
			throw new ArtifactFilterParseException(REASON, null, LINE_NUMBER);
		});
		assertThatIllegalArgumentException().isThrownBy(() -> {
			throw new ArtifactFilterParseException(REASON, Strings.EMPTY, LINE_NUMBER);
		});
		assertThatIllegalArgumentException().isThrownBy(() -> {
			throw new ArtifactFilterParseException(REASON, LINE, 0);
		});
	}

	@Test
	void testWith() {
		final var otherArtifactFilterParseException = artifactFilterParseException.with(FILE);
		assertThat(otherArtifactFilterParseException.getOptionalFile())
				.hasValue(FILE);
		assertThat(artifactFilterParseException.getReason())
				.isEqualTo(otherArtifactFilterParseException.getReason());
		assertThat(artifactFilterParseException.getLine())
				.isEqualTo(otherArtifactFilterParseException.getLine());
		assertThat(artifactFilterParseException.getLineNumber())
				.isEqualTo(otherArtifactFilterParseException.getLineNumber());
	}

	@Test
	void testWithInvalid() {
		assertThatNullPointerException().isThrownBy(() -> artifactFilterParseException.with(null));
	}

	@Test
	void testGetMessage() {
		assertThat(artifactFilterParseException.getMessage()).isEqualTo(
				REASON + ": \"" + LINE + "\" (at line " + LINE_NUMBER + ")"
		);
		assertThat(artifactFilterParseException.with(FILE).getMessage()).isEqualTo(
				REASON + ": \"" + LINE + "\" (at line " + LINE_NUMBER + " of the " + FILE + " file)"
		);
	}

	@Test
	void testGetReason() {
		assertThat(artifactFilterParseException.getReason()).isEqualTo(REASON);
	}

	@Test
	void testGetLine() {
		assertThat(artifactFilterParseException.getLine()).isEqualTo(LINE);
	}

	@Test
	void testGetLineNumber() {
		assertThat(artifactFilterParseException.getLineNumber()).isEqualTo(LINE_NUMBER);
	}

	@Test
	void testGetOptionalFile() {
		assertThat(artifactFilterParseException.getOptionalFile()).isEmpty();
	}

	@Test
	void testSerializable() {
		assertThat(
				Serializables.<ArtifactFilterParseException>deserialize(
						Serializables.serialize(artifactFilterParseException)
				)
		).hasSameClassAs(artifactFilterParseException);
	}
}