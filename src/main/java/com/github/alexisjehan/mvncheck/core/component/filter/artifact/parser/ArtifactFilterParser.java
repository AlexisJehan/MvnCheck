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

import com.github.alexisjehan.javanilla.io.Readers;
import com.github.alexisjehan.javanilla.lang.Strings;
import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import com.github.alexisjehan.mvncheck.core.component.filter.artifact.ArtifactFilter;
import com.github.alexisjehan.mvncheck.core.component.filter.artifact.CompositeArtifactFilter;
import com.github.alexisjehan.mvncheck.core.component.filter.artifact.WildcardArtifactFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.HashSet;

/**
 * <p>Class that describes an artifact filter parser.</p>
 * @since 1.0.0
 */
public final class ArtifactFilterParser {

	/**
	 * <p>Start of a comment line.</p>
	 * @since 1.0.0
	 */
	private static final char COMMENT_START = '#';

	/**
	 * <p>Separator.</p>
	 * @since 1.0.0
	 */
	private static final char SEPARATOR = ':';

	/**
	 * <p>Logger.</p>
	 * @since 1.0.0
	 */
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * <p>Constructor.</p>
	 * @since 1.0.0
	 */
	private ArtifactFilterParser() {}

	/**
	 * <p>Parse an artifact filter from the given ignore file.</p>
	 * @param ignoreFile an ignore file
	 * @return the artifact filter
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the ignore file is {@code null}
	 * @throws IllegalArgumentException if the ignore file does not exist
	 * @throws ArtifactFilterParseException if the content is unexpected
	 * @since 1.0.0
	 */
	public static ArtifactFilter parse(final Path ignoreFile) throws IOException {
		Ensure.notNullAndFile("ignoreFile", ignoreFile);
		logger.info("Parsing the {} ignore file", () -> ToString.toString(ignoreFile));
		try (var reader = Readers.of(ignoreFile)) {
			reader.mark(1);
			if ('\ufeff' != reader.read()) { // UTF-8 BOM character
				reader.reset();
			}
			return parse(reader);
		} catch (final ArtifactFilterParseException e) {
			throw e.with(ignoreFile);
		}
	}

	/**
	 * <p>Parse an artifact filter from the given {@link Reader}.</p>
	 * @param reader a {@link Reader}
	 * @return the artifact filter
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the {@link Reader} is {@code null}
	 * @throws ArtifactFilterParseException if the content is unexpected
	 * @since 1.0.0
	 */
	static ArtifactFilter parse(final Reader reader) throws IOException {
		Ensure.notNull("reader", reader);
		final var artifactFilters = new HashSet<ArtifactFilter>();
		try (var bufferedReader = Readers.buffered(reader)) {
			String line;
			var lineNumber = 1L;
			while (null != (line = bufferedReader.readLine())) {
				final var expression = Strings.substringBefore(line, COMMENT_START).trim();
				if (expression.isEmpty()) {
					continue;
				}
				try {
					artifactFilters.add(parse(expression));
				} catch (final ArtifactFilterParseException e) {
					throw e.with(lineNumber);
				}
				++lineNumber;
			}
		}
		if (artifactFilters.isEmpty()) {
			return ArtifactFilter.ACCEPT_ALL;
		}
		return CompositeArtifactFilter.none(artifactFilters.toArray(ArtifactFilter[]::new));
	}

	/**
	 * <p>Parse an artifact filter from the given expression.</p>
	 * @param expression an expression
	 * @return the artifact filter
	 * @throws NullPointerException if the expression is {@code null}
	 * @throws IllegalArgumentException if the expression is empty
	 * @throws ArtifactFilterParseException if the content is unexpected
	 * @since 1.7.0
	 */
	public static ArtifactFilter parse(final String expression) {
		Ensure.notNullAndNotEmpty("expression", expression);
		logger.debug("Parsing the {} expression", () -> ToString.toString(expression));
		final var frequency = Strings.frequency(expression, SEPARATOR);
		if (2 < frequency) {
			throw new ArtifactFilterParseException(
					"Unexpected expression format",
					expression
			);
		}
		final var parts = Strings.split(SEPARATOR, expression);
		final var groupIdExpression = parts.get(0);
		if (groupIdExpression.isEmpty()) {
			throw new ArtifactFilterParseException(
					"Unexpected expression format, the group identifier is empty",
					expression
			);
		}
		if (1 == parts.size()) {
			return new WildcardArtifactFilter(groupIdExpression);
		}
		final var artifactIdExpression = parts.get(1);
		if (artifactIdExpression.isEmpty()) {
			throw new ArtifactFilterParseException(
					"Unexpected expression format, the artifact identifier is empty",
					expression
			);
		}
		if (2 == parts.size()) {
			return new WildcardArtifactFilter(groupIdExpression, artifactIdExpression);
		}
		final var updateVersionExpression = parts.get(2);
		if (updateVersionExpression.isEmpty()) {
			throw new ArtifactFilterParseException(
					"Unexpected expression format, the update version is empty",
					expression
			);
		}
		return new WildcardArtifactFilter(groupIdExpression, artifactIdExpression, updateVersionExpression);
	}
}