/*
 * MIT License
 *
 * Copyright (c) 2022-2026 Alexis Jehan
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

import com.github.alexisjehan.javanilla.misc.quality.Ensure;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Utility class that helps to deal with the system.
 * @since 1.0.0
 */
public final class SystemUtils {

	/**
	 * {@code PATH} environment variable.
	 * @since 1.0.0
	 */
	private static final String PATH_ENVIRONMENT_VARIABLE = System.getenv("PATH");

	/**
	 * User home directory.
	 * @since 1.0.0
	 */
	private static final Path USER_HOME_DIRECTORY = Path.of(System.getProperty("user.home"));

	/**
	 * Constructor.
	 * @since 1.0.0
	 */
	private SystemUtils() {}

	/**
	 * Get an {@link Optional} environment variable for the given name.
	 * @param name a name
	 * @return the {@link Optional} environment variable
	 * @throws NullPointerException if the name is {@code null}
	 * @throws IllegalArgumentException if the name is empty
	 * @since 1.0.0
	 */
	public static Optional<String> getEnvironmentVariable(final String name) {
		Ensure.notNullAndNotEmpty("name", name);
		return Optional.ofNullable(System.getenv(name));
	}

	/**
	 * Get the {@code PATH} environment variable.
	 * @return the {@code PATH} environment variable
	 * @since 1.0.0
	 */
	public static String getPathEnvironmentVariable() {
		return PATH_ENVIRONMENT_VARIABLE;
	}

	/**
	 * Get the user home directory.
	 * @return the user home directory
	 * @since 1.0.0
	 */
	public static Path getUserHomeDirectory() {
		return USER_HOME_DIRECTORY;
	}
}