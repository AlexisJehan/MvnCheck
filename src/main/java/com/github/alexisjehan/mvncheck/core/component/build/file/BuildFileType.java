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
package com.github.alexisjehan.mvncheck.core.component.build.file;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;

import java.util.Arrays;
import java.util.Optional;

/**
 * <p>Enumeration of build file types.</p>
 * @since 1.0.0
 */
public enum BuildFileType {

	/**
	 * <p>The <i>Maven</i> build file type.</p>
	 * @since 1.0.0
	 */
	MAVEN("pom.xml"),

	/**
	 * <p>The <i>Gradle Groovy</i> build file type.</p>
	 * @since 1.0.0
	 */
	GRADLE_GROOVY("build.gradle"),

	/**
	 * <p>The <i>Gradle Kotlin</i> build file type.</p>
	 * @since 1.0.0
	 */
	GRADLE_KOTLIN("build.gradle.kts");

	/**
	 * <p>File name.</p>
	 * @since 1.0.0
	 */
	private final String fileName;

	/**
	 * <p>Constructor.</p>
	 * @param fileName a file name
	 * @since 1.0.0
	 */
	BuildFileType(final String fileName) {
		this.fileName = fileName;
	}

	/**
	 * <p>Get the file name.</p>
	 * @return the file name
	 * @since 1.0.0
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * <p>Return an {@link Optional} of the value for the given file name.</p>
	 * @param fileName a file name
	 * @return the {@link Optional} of the value
	 * @throws NullPointerException if the file name is {@code null}
	 * @throws IllegalArgumentException if the file name is empty
	 * @since 1.0.0
	 */
	public static Optional<BuildFileType> optionalValueOf(final String fileName) {
		Ensure.notNullAndNotEmpty("fileName", fileName);
		return Arrays.stream(values())
				.filter(value -> value.fileName.equals(fileName))
				.findAny();
	}
}