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

import com.github.alexisjehan.javanilla.lang.Strings;
import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.ToString;

import java.nio.file.Path;
import java.util.Optional;

/**
 * <p>Unchecked {@link Exception} related to artifact filter parsing.</p>
 * <p><b>Note</b>: This class is {@link java.io.Serializable}.</p>
 * @since 1.0.0
 */
public final class ArtifactFilterParseException extends RuntimeException {

	/**
	 * <p>Serial version unique identifier.</p>
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = -5557892849559209991L;

	/**
	 * <p>Reason.</p>
	 * @since 1.0.0
	 */
	private final String reason;

	/**
	 * <p>Expression.</p>
	 * @since 1.7.0
	 */
	private final String expression;

	/**
	 * <p>Line number.</p>
	 * @since 1.0.0
	 */
	private final Long lineNumber;

	/**
	 * <p>File.</p>
	 * @since 1.0.0
	 */
	private final transient Path file;

	/**
	 * <p>Constructor with an expression.</p>
	 * @param reason a reason
	 * @param expression an expression
	 * @throws NullPointerException if the reason or the expression is {@code null}
	 * @throws IllegalArgumentException if the reason or the expression is empty
	 * @since 1.7.0
	 */
	ArtifactFilterParseException(
			final String reason,
			final String expression
	) {
		this(reason, expression, null, null);
	}

	/**
	 * <p>Constructor with an expression at a line number.</p>
	 * @param reason a reason
	 * @param expression an expression
	 * @param lineNumber a line number or {@code null}
	 * @throws NullPointerException if the reason or the expression is {@code null}
	 * @throws IllegalArgumentException if the reason or the expression is empty or if the line number is lower than or
	 *         equal to 0
	 * @deprecated since 1.7.0, use {@link #ArtifactFilterParseException(String, String)} instead
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.7.0")
	ArtifactFilterParseException(
			final String reason,
			final String expression,
			final Long lineNumber
	) {
		this(reason, expression, lineNumber, null);
	}

	/**
	 * <p>Constructor with an expression at a line number of a file.</p>
	 * @param reason a reason
	 * @param expression an expression
	 * @param lineNumber a line number or {@code null}
	 * @param file a file or {@code null}
	 * @throws NullPointerException if the reason or the expression is {@code null}
	 * @throws IllegalArgumentException if the reason or the expression is empty or if the line number is lower than or
	 *         equal to 0
	 * @since 1.0.0
	 */
	private ArtifactFilterParseException(
			final String reason,
			final String expression,
			final Long lineNumber,
			final Path file
	) {
		super(
				Ensure.notNullAndNotEmpty("reason", reason)
						+ ": "
						+ ToString.toString(Ensure.notNullAndNotEmpty("expression", expression))
						+ (
						null != lineNumber
								? " (at line " + Ensure.greaterThan("lineNumber", lineNumber, 0L)
								+ (null != file ? " of the " + ToString.toString(file) + " file" : Strings.EMPTY)
								+ ")"
								: Strings.EMPTY
				)
		);
		this.reason = reason;
		this.expression = expression;
		this.lineNumber = lineNumber;
		this.file = file;
	}

	/**
	 * <p>Return a copy of the current exception with the given line number.</p>
	 * @param lineNumber a line number
	 * @return the copy of the current exception
	 * @throws NullPointerException if the line number is {@code null}
	 * @throws IllegalArgumentException if the line number is lower than or equal to 0
	 * @since 1.7.0
	 */
	public ArtifactFilterParseException with(final Long lineNumber) {
		Ensure.notNull("lineNumber", lineNumber);
		Ensure.greaterThan("lineNumber", lineNumber, 0L);
		return new ArtifactFilterParseException(reason, expression, lineNumber, file);
	}

	/**
	 * <p>Return a copy of the current exception with the given file.</p>
	 * @param file a file
	 * @return the copy of the current exception
	 * @throws NullPointerException if the file is {@code null}
	 * @since 1.0.0
	 */
	public ArtifactFilterParseException with(final Path file) {
		Ensure.notNull("file", file);
		return new ArtifactFilterParseException(reason, expression, lineNumber, file);
	}

	/**
	 * <p>Get the reason.</p>
	 * @return the reason
	 * @since 1.0.0
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * <p>Get the expression.</p>
	 * @return the expression
	 * @deprecated since 1.7.0, use {@link #getExpression()} instead
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.7.0")
	public String getLine() {
		return getExpression();
	}

	/**
	 * <p>Get the expression.</p>
	 * @return the expression
	 * @since 1.7.0
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * <p>Get the line number.</p>
	 * @return the line number
	 * @deprecated since 1.7.0, use {@link #getOptionalLineNumber()} instead
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.7.0")
	public Long getLineNumber() {
		return getOptionalLineNumber().orElse(null);
	}

	/**
	 * <p>Get an {@link Optional} of the line number.</p>
	 * @return the {@link Optional} of the line number
	 * @since 1.7.0
	 */
	public Optional<Long> getOptionalLineNumber() {
		return Optional.ofNullable(lineNumber);
	}

	/**
	 * <p>Get an {@link Optional} of the file.</p>
	 * @return the {@link Optional} of the file
	 * @since 1.0.0
	 */
	public Optional<Path> getOptionalFile() {
		return Optional.ofNullable(file);
	}
}