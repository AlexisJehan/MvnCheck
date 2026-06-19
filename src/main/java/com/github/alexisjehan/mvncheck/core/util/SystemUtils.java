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

import com.github.alexisjehan.javanilla.lang.Strings;
import com.github.alexisjehan.javanilla.misc.quality.Ensure;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Utility class that helps to deal with the system.
 * @since 1.0.0
 */
public final class SystemUtils {

	/**
	 * XDG directory name.
	 * @since 2.3.0
	 */
	static final String XDG_DIRECTORY_NAME = "mvnchk";

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
	 * Default XDG configuration user base directory.
	 * @since 2.3.0
	 */
	private static final Path DEFAULT_XDG_CONFIG_USER_BASE_DIRECTORY = USER_HOME_DIRECTORY.resolve(".config");

	/**
	 * Default XDG configuration global base directory.
	 * @since 2.3.0
	 */
	private static final Path DEFAULT_XDG_CONFIG_GLOBAL_BASE_DIRECTORY = Path.of("/etc/xdg");

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

	/**
	 * Get a {@link List} of XDG configuration directories, sorted by priority.
	 * @return the {@link List} of XDG configuration directories
	 * @see <a href="https://specifications.freedesktop.org/basedir">https://specifications.freedesktop.org/basedir</a>
	 * @since 2.3.0
	 */
	public static List<Path> getXdgConfigDirectories() {
		return Stream.concat(
						getEnvironmentVariable("XDG_CONFIG_HOME")
								.filter(Predicate.not(String::isEmpty))
								.map(Path::of)
								.map(Stream::of)
								.orElseGet(() -> Stream.of(DEFAULT_XDG_CONFIG_USER_BASE_DIRECTORY)),
						getEnvironmentVariable("XDG_CONFIG_DIRS")
								.filter(Predicate.not(String::isEmpty))
								.map(
										directories -> Strings.split(File.pathSeparatorChar, directories)
												.stream()
												.map(Path::of)
								)
								.orElseGet(() -> Stream.of(DEFAULT_XDG_CONFIG_GLOBAL_BASE_DIRECTORY))
				)
				.map(baseDirectory -> baseDirectory.resolve(XDG_DIRECTORY_NAME))
				.toList();
	}
}