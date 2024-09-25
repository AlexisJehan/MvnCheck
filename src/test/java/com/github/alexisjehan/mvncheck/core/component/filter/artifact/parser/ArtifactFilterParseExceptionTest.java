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
package com.github.alexisjehan.mvncheck.core.component.filter.artifact.parser;

import com.github.alexisjehan.javanilla.io.Serializables;
import com.github.alexisjehan.javanilla.lang.Strings;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class ArtifactFilterParseExceptionTest {

	private static final String REASON = "foo-reason";
	private static final String EXPRESSION = "foo-expression";
	private static final Long LINE_NUMBER = 1L;
	private static final Path FILE = Path.of("foo-file");

	private final ArtifactFilterParseException artifactFilterParseException = new ArtifactFilterParseException(
			REASON,
			EXPRESSION
	);

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException().isThrownBy(() -> {
			throw new ArtifactFilterParseException(null, EXPRESSION);
		});
		assertThatIllegalArgumentException().isThrownBy(() -> {
			throw new ArtifactFilterParseException(Strings.EMPTY, EXPRESSION);
		});
		assertThatNullPointerException().isThrownBy(() -> {
			throw new ArtifactFilterParseException(REASON, null);
		});
		assertThatIllegalArgumentException().isThrownBy(() -> {
			throw new ArtifactFilterParseException(REASON, Strings.EMPTY);
		});
	}

	@Test
	@Deprecated
	void testConstructorDeprecated() {
		final var otherArtifactFilterParseException = new ArtifactFilterParseException(REASON, EXPRESSION, LINE_NUMBER);
		assertThat(otherArtifactFilterParseException.getReason()).isEqualTo(REASON);
		assertThat(otherArtifactFilterParseException.getExpression()).isEqualTo(EXPRESSION);
		assertThat(otherArtifactFilterParseException.getOptionalLineNumber()).hasValue(LINE_NUMBER);
	}

	@Test
	@Deprecated
	void testConstructorDeprecatedInvalid() {
		assertThatNullPointerException().isThrownBy(() -> {
			throw new ArtifactFilterParseException(null, EXPRESSION, LINE_NUMBER);
		});
		assertThatIllegalArgumentException().isThrownBy(() -> {
			throw new ArtifactFilterParseException(Strings.EMPTY, EXPRESSION, LINE_NUMBER);
		});
		assertThatNullPointerException().isThrownBy(() -> {
			throw new ArtifactFilterParseException(REASON, null, LINE_NUMBER);
		});
		assertThatIllegalArgumentException().isThrownBy(() -> {
			throw new ArtifactFilterParseException(REASON, Strings.EMPTY, LINE_NUMBER);
		});
		assertThatIllegalArgumentException().isThrownBy(() -> {
			throw new ArtifactFilterParseException(REASON, EXPRESSION, 0L);
		});
	}

	@Test
	void testWithLineNumber() {
		final var otherArtifactFilterParseException = artifactFilterParseException.with(LINE_NUMBER);
		assertThat(otherArtifactFilterParseException.getOptionalLineNumber())
				.hasValue(LINE_NUMBER);
		assertThat(artifactFilterParseException.getReason())
				.isEqualTo(otherArtifactFilterParseException.getReason());
		assertThat(artifactFilterParseException.getExpression())
				.isEqualTo(otherArtifactFilterParseException.getExpression());
		assertThat(artifactFilterParseException.getOptionalFile())
				.isEqualTo(otherArtifactFilterParseException.getOptionalFile());
	}

	@Test
	void testWithLineNumberInvalid() {
		assertThatNullPointerException().isThrownBy(() -> artifactFilterParseException.with((Long) null));
		assertThatIllegalArgumentException().isThrownBy(() -> artifactFilterParseException.with(0L));
	}

	@Test
	void testWithFile() {
		final var otherArtifactFilterParseException = artifactFilterParseException.with(FILE);
		assertThat(otherArtifactFilterParseException.getOptionalFile())
				.hasValue(FILE);
		assertThat(artifactFilterParseException.getReason())
				.isEqualTo(otherArtifactFilterParseException.getReason());
		assertThat(artifactFilterParseException.getExpression())
				.isEqualTo(otherArtifactFilterParseException.getExpression());
		assertThat(artifactFilterParseException.getOptionalLineNumber())
				.isEqualTo(otherArtifactFilterParseException.getOptionalLineNumber());
	}

	@Test
	void testWithFileInvalid() {
		assertThatNullPointerException().isThrownBy(() -> artifactFilterParseException.with((Path) null));
	}

	@Test
	void testGetMessage() {
		assertThat(artifactFilterParseException.getMessage()).isEqualTo(
				REASON + ": \"" + EXPRESSION + "\""
		);
		assertThat(artifactFilterParseException.with(LINE_NUMBER).getMessage()).isEqualTo(
				REASON + ": \"" + EXPRESSION + "\" (at line " + LINE_NUMBER + ")"
		);
		assertThat(artifactFilterParseException.with(LINE_NUMBER).with(FILE).getMessage()).isEqualTo(
				REASON + ": \"" + EXPRESSION + "\" (at line " + LINE_NUMBER + " of the " + FILE + " file)"
		);
	}

	@Test
	void testGetReason() {
		assertThat(artifactFilterParseException.getReason()).isEqualTo(REASON);
	}

	@Test
	@Deprecated
	void testGetLine() {
		assertThat(artifactFilterParseException.getLine()).isEqualTo(EXPRESSION);
	}

	@Test
	void testGetExpression() {
		assertThat(artifactFilterParseException.getExpression()).isEqualTo(EXPRESSION);
	}

	@Test
	@Deprecated
	void testGetLineNumber() {
		assertThat(artifactFilterParseException.getLineNumber()).isNull();
	}

	@Test
	void testGetOptionalLineNumber() {
		assertThat(artifactFilterParseException.getOptionalLineNumber()).isEmpty();
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