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

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.mvncheck.core.component.artifact.Artifact;

import java.util.regex.Pattern;

/**
 * Class that describes an artifact filter based on expressions that can contain wildcard characters.
 * @since 1.7.0
 */
public final class WildcardArtifactFilter implements ArtifactFilter {

	/**
	 * Single character wildcard.
	 * @since 1.7.0
	 */
	public static final char WILDCARD_SINGLE = '?';

	/**
	 * Any character wildcard.
	 * @since 1.7.0
	 */
	public static final char WILDCARD_ANY = '*';

	/**
	 * {@link Pattern} of the group identifier.
	 * @since 1.7.0
	 */
	private final Pattern groupIdPattern;

	/**
	 * {@link Pattern} of the artifact identifier or {@code null}.
	 * @since 1.7.0
	 */
	private final Pattern artifactIdPattern;

	/**
	 * {@link Pattern} of the update version or {@code null}.
	 * @since 1.7.0
	 */
	private final Pattern updateVersionPattern;

	/**
	 * Constructor with a group identifier expression.
	 * @param groupIdExpression a group identifier expression
	 * @throws NullPointerException if the group identifier expression is {@code null}
	 * @throws IllegalArgumentException if the group identifier expression is empty
	 * @since 1.7.0
	 */
	public WildcardArtifactFilter(final String groupIdExpression) {
		this(groupIdExpression, null);
	}

	/**
	 * Constructor with a group identifier expression and an artifact identifier expression.
	 * @param groupIdExpression a group identifier expression
	 * @param artifactIdExpression an artifact identifier expression or {@code null}
	 * @throws NullPointerException if the group identifier expression is {@code null}
	 * @throws IllegalArgumentException if the group identifier expression or the artifact identifier expression is
	 *         empty
	 * @since 1.7.0
	 */
	public WildcardArtifactFilter(final String groupIdExpression, final String artifactIdExpression) {
		this(groupIdExpression, artifactIdExpression, null);
	}

	/**
	 * Constructor with a group identifier expression, an artifact identifier expression and an update version
	 * expression.
	 * @param groupIdExpression a group identifier expression
	 * @param artifactIdExpression an artifact identifier expression or {@code null}
	 * @param updateVersionExpression an update version expression or {@code null}
	 * @throws NullPointerException if the group identifier expression is {@code null}
	 * @throws IllegalArgumentException if the group identifier expression, the artifact identifier expression or the
	 *         update version expression is empty
	 * @since 1.7.0
	 */
	public WildcardArtifactFilter(
			final String groupIdExpression,
			final String artifactIdExpression,
			final String updateVersionExpression
	) {
		Ensure.notNullAndNotEmpty("groupIdExpression", groupIdExpression);
		groupIdPattern = createPattern(groupIdExpression);
		if (null != artifactIdExpression) {
			Ensure.notNullAndNotEmpty("artifactIdExpression", artifactIdExpression);
			artifactIdPattern = createPattern(artifactIdExpression);
		} else {
			artifactIdPattern = null;
		}
		if (null != updateVersionExpression) {
			Ensure.notNullAndNotEmpty("updateVersionExpression", updateVersionExpression);
			updateVersionPattern = createPattern(updateVersionExpression);
		} else {
			updateVersionPattern = null;
		}
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the artifact is {@code null}
	 * @since 1.7.0
	 */
	@Override
	public boolean accept(final Artifact<?> artifact) {
		Ensure.notNull("artifact", artifact);
		final var artifactIdentifier = artifact.getIdentifier();
		final var groupId = artifactIdentifier.getGroupId();
		final var artifactId = artifactIdentifier.getArtifactId();
		return groupIdPattern.matcher(groupId).matches()
				&& (null == artifactIdPattern || artifactIdPattern.matcher(artifactId).matches());
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the artifact or the update version is {@code null}
	 * @throws IllegalArgumentException if the update version is empty
	 * @since 1.7.0
	 */
	@Override
	public boolean accept(final Artifact<?> artifact, final String updateVersion) {
		Ensure.notNull("artifact", artifact);
		Ensure.notNullAndNotEmpty("updateVersion", updateVersion);
		return accept(artifact)
				&& (null == updateVersionPattern || updateVersionPattern.matcher(updateVersion).matches());
	}

	/**
	 * Create a {@link Pattern} from an expression.
	 * @param expression an expression
	 * @return the created {@link Pattern}
	 * @since 1.7.0
	 */
	private static Pattern createPattern(final String expression) {
		return Pattern.compile(
				"^" + Pattern.quote(expression)
						.replace(String.valueOf(WILDCARD_SINGLE), "\\E.\\Q")
						.replace(String.valueOf(WILDCARD_ANY), "\\E.*\\Q") + "$",
				Pattern.CASE_INSENSITIVE
		);
	}
}