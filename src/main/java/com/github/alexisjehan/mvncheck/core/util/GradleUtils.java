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
package com.github.alexisjehan.mvncheck.core.util;

import com.github.alexisjehan.javanilla.lang.Strings;
import org.gradle.util.GradleVersion;

import java.io.File;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * <p>Utility class that helps dealing with <i>Gradle</i>.</p>
 * @since 1.0.0
 */
public final class GradleUtils {

	/**
	 * <p><i>Gradle</i> version.</p>
	 * @since 1.0.0
	 */
	public static final String VERSION = GradleVersion.current().getVersion();

	/**
	 * <p>Pattern to extract the <i>Gradle</i> home from the {@code PATH} environment variable.</p>
	 * @since 1.0.0
	 */
	private static final Pattern PATH_PATTERN = Pattern.compile(
			"^(.*[/\\\\]+gradle-\\d+\\.\\d+(?:\\.\\d+)?)[/\\\\]+bin[/\\\\]*$"
	);

	/**
	 * <p>Constructor.</p>
	 * @since 1.0.0
	 */
	private GradleUtils() {}

	/**
	 * <p>Retrieve an {@link Optional} of the <i>Gradle</i> home.</p>
	 * @return the {@link Optional} of the <i>Gradle</i> home
	 * @since 1.0.0
	 */
	public static Optional<String> retrieveOptionalHome() {
		final var optionalGradleHome = SystemUtils.getEnvironmentVariable("GRADLE_HOME");
		if (optionalGradleHome.isPresent()) {
			return optionalGradleHome;
		}
		return Strings.split(File.pathSeparatorChar, SystemUtils.getPathEnvironmentVariable())
				.stream()
				.map(segment -> {
					final var matcher = PATH_PATTERN.matcher(segment);
					if (matcher.find()) {
						return Optional.of(matcher.group(1));
					}
					return Optional.<String>empty();
				})
				.flatMap(Optional::stream)
				.findAny();
	}

	/**
	 * <p>Get the <i>Gradle</i> version.</p>
	 * @return the <i>Gradle</i> version
	 * @deprecated since 1.4.0, use {@link #VERSION} instead
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.4.0")
	public static String getVersion() {
		return VERSION;
	}
}