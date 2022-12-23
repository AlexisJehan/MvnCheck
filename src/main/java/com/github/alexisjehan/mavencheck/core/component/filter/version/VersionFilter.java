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
package com.github.alexisjehan.mavencheck.core.component.filter.version;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;

/**
 * <p>Interface that describes a version filter.</p>
 * @since 1.0.0
 */
@FunctionalInterface
public interface VersionFilter {

	/**
	 * <p>Version filter that reject snapshots.</p>
	 * @since 1.0.0
	 */
	VersionFilter SNAPSHOT = version -> {
		Ensure.notNullAndNotEmpty("version", version);
		return version.endsWith("-SNAPSHOT");
	};

	/**
	 * <p>Test if a version is accepted.</p>
	 * @param version a version
	 * @return {@code true} if the version is accepted
	 * @since 1.0.0
	 */
	boolean accept(final String version);
}