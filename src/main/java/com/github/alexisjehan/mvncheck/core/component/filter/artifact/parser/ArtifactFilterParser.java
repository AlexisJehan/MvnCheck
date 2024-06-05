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
import com.github.alexisjehan.mvncheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mvncheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mvncheck.core.component.filter.artifact.ArtifactFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

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
	 * <p>Single character wildcard.</p>
	 * @since 1.0.0
	 */
	private static final char WILDCARD_SINGLE = '?';

	/**
	 * <p>Any characters wildcard.</p>
	 * @since 1.0.0
	 */
	private static final char WILDCARD_ANY = '*';

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
		final var identifiers = new HashSet<ArtifactIdentifier>();
		final var identifiersVersions = new HashMap<ArtifactIdentifier, Set<Pattern>>();
		try (var bufferedReader = Readers.buffered(reader)) {
			String line;
			var lineNumber = 1L;
			while (null != (line = bufferedReader.readLine())) {
				line = Strings.substringBefore(line, COMMENT_START).trim();
				if (line.isEmpty()) {
					continue;
				}
				final var frequency = Strings.frequency(line, SEPARATOR);
				if (1 != frequency && 2 != frequency) {
					throw new ArtifactFilterParseException("Unexpected format", line, lineNumber);
				}
				final var parts = Strings.split(SEPARATOR, line);
				final var groupId = parts.get(0);
				if (groupId.isEmpty()) {
					throw new ArtifactFilterParseException("Unexpected format, empty groupId", line, lineNumber);
				}
				final var artifactId = parts.get(1);
				if (artifactId.isEmpty()) {
					throw new ArtifactFilterParseException("Unexpected format, empty artifactId", line, lineNumber);
				}
				final var identifier = new ArtifactIdentifier(groupId, artifactId);
				if (2 == parts.size()) {
					logger.debug("Ignoring the {} artifact identifier", () -> ToString.toString(identifier));
					identifiers.add(identifier);
				} else {
					final var versionExpression = parts.get(2);
					if (versionExpression.isEmpty()) {
						throw new ArtifactFilterParseException(
								"Unexpected format, empty version expression",
								line,
								lineNumber
						);
					}
					logger.debug(
							"Ignoring the {} artifact identifier with {} version expression",
							() -> ToString.toString(identifier),
							() -> ToString.toString(versionExpression)
					);
					identifiersVersions.computeIfAbsent(identifier, key -> new HashSet<>()).add(
							Pattern.compile(
									"^" + Pattern.quote(versionExpression)
											.replace(String.valueOf(WILDCARD_SINGLE), "\\E.\\Q")
											.replace(String.valueOf(WILDCARD_ANY), "\\E.*\\Q") + "$",
									Pattern.CASE_INSENSITIVE
							)
					);
				}
				++lineNumber;
			}
		}
		return new ArtifactFilter() {

			/**
			 * {@inheritDoc}
			 * @throws NullPointerException if the artifact is {@code null}
			 * @since 1.0.0
			 */
			@Override
			public boolean accept(final Artifact<?> artifact) {
				Ensure.notNull("artifact", artifact);
				return !identifiers.contains(artifact.getIdentifier());
			}

			/**
			 * {@inheritDoc}
			 * @throws NullPointerException if the artifact or the update version is {@code null}
			 * @throws IllegalArgumentException if the update version is empty
			 * @since 1.0.0
			 */
			@Override
			public boolean accept(final Artifact<?> artifact, final String updateVersion) {
				Ensure.notNull("artifact", artifact);
				Ensure.notNullAndNotEmpty("updateVersion", updateVersion);
				final var identifierVersions = identifiersVersions.get(artifact.getIdentifier());
				return null == identifierVersions
						|| identifierVersions.stream().noneMatch(pattern -> pattern.matcher(updateVersion).matches());
			}
		};
	}
}