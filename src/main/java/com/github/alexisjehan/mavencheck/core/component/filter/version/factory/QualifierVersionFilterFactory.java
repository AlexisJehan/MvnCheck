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
package com.github.alexisjehan.mavencheck.core.component.filter.version.factory;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.mavencheck.core.component.filter.version.VersionFilter;

import java.util.regex.Pattern;

/**
 * <p>Enumeration of a singleton qualifier version filter factory.</p>
 * @since 1.0.0
 */
public enum QualifierVersionFilterFactory implements VersionFilterFactory {

	/**
	 * <p>Singleton instance.</p>
	 * @since 1.0.0
	 */
	INSTANCE;

	/**
	 * <p>Pattern to extract the qualifier from a version.</p>
	 * @since 1.0.0
	 */
	private static final Pattern PATTERN = Pattern.compile(
			"^.*[.\\-]?([a-z]+)[.\\-]?\\d*$",
			Pattern.CASE_INSENSITIVE
	);

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the artifact version is {@code null}
	 * @throws IllegalArgumentException if the artifact version is empty
	 * @since 1.0.0
	 */
	@Override
	public VersionFilter create(final String artifactVersion) {
		Ensure.notNullAndNotEmpty("artifactVersion", artifactVersion);
		final var artifactVersionQualifier = extractQualifier(artifactVersion);
		return version -> {
			Ensure.notNullAndNotEmpty("version", version);
			final var versionQualifier = extractQualifier(version);
			return null == versionQualifier
					|| null == artifactVersionQualifier
					|| versionQualifier.equalsIgnoreCase(artifactVersionQualifier);
		};
	}

	/**
	 * <p>Extract the qualifier from a version.</p>
	 * @param version a version
	 * @return the qualifier
	 * @since 1.0.0
	 */
	private static String extractQualifier(final String version) {
		final var matcher = PATTERN.matcher(version);
		return matcher.matches()
				? matcher.group(1)
				: null;
	}
}