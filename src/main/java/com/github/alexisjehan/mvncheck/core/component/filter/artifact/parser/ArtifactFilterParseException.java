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
package com.github.alexisjehan.mvncheck.core.component.filter.artifact.parser;

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
	private static final long serialVersionUID = 8647765091802310213L;

	/**
	 * <p>Reason.</p>
	 * @since 1.0.0
	 */
	private final String reason;

	/**
	 * <p>Line.</p>
	 * @since 1.0.0
	 */
	private final String line;

	/**
	 * <p>Line number.</p>
	 * @since 1.0.0
	 */
	private final long lineNumber;

	/**
	 * <p>File.</p>
	 * @since 1.0.0
	 */
	private final transient Path file;

	/**
	 * <p>Constructor without a file.</p>
	 * @param reason a reason
	 * @param line a line
	 * @param lineNumber a line number
	 * @throws NullPointerException if the reason or the line is {@code null}
	 * @throws IllegalArgumentException if the reason or the line is empty or if the line number is lower than or equal
	 *         to 0
	 * @since 1.0.0
	 */
	ArtifactFilterParseException(
			final String reason,
			final String line,
			final long lineNumber
	) {
		this(reason, line, lineNumber, null);
	}

	/**
	 * <p>Constructor with a file.</p>
	 * @param reason a reason
	 * @param line a line
	 * @param lineNumber a line number
	 * @param file a file or {@code null}
	 * @throws NullPointerException if the reason or the line is {@code null}
	 * @throws IllegalArgumentException if the reason or the line is empty or if the line number is lower than or equal
	 *         to 0
	 * @since 1.0.0
	 */
	private ArtifactFilterParseException(
			final String reason,
			final String line,
			final long lineNumber,
			final Path file
	) {
		super(
				Ensure.notNullAndNotEmpty("reason", reason)
						+ ": "
						+ ToString.toString(Ensure.notNullAndNotEmpty("line", line))
						+ " ("
						+ "at line " + Ensure.greaterThan("lineNumber", lineNumber, 0L)
						+ (null != file ? " of the " + ToString.toString(file) + " file" : "")
						+ ")"
		);
		this.reason = reason;
		this.line = line;
		this.lineNumber = lineNumber;
		this.file = file;
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
		return new ArtifactFilterParseException(reason, line, lineNumber, file);
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
	 * <p>Get the line.</p>
	 * @return the line
	 * @since 1.0.0
	 */
	public String getLine() {
		return line;
	}

	/**
	 * <p>Get the line number.</p>
	 * @return the line number
	 * @since 1.0.0
	 */
	public long getLineNumber() {
		return lineNumber;
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