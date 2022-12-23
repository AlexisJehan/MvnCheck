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

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Class that describes a composite version filter factory.</p>
 * @since 1.0.0
 */
public final class CompositeVersionFilterFactory implements VersionFilterFactory {

	/**
	 * <p>{@link Set} of version filter factories.</p>
	 * @since 1.0.0
	 */
	private final Set<VersionFilterFactory> factories;

	/**
	 * <p>Constructor.</p>
	 * @param factories an array of factories
	 * @throws NullPointerException if the array of factories or any of them is {@code null}
	 * @throws IllegalArgumentException if the array of factories is empty
	 * @since 1.0.0
	 */
	public CompositeVersionFilterFactory(final VersionFilterFactory... factories) {
		Ensure.notNullAndNotEmpty("factories", factories);
		Ensure.notNullAndNotNullElements("factories", factories);
		this.factories = Arrays.stream(factories)
				.collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the artifact version is {@code null}
	 * @throws IllegalArgumentException if the artifact version is empty
	 * @since 1.0.0
	 */
	@Override
	public VersionFilter create(final String artifactVersion) {
		Ensure.notNullAndNotEmpty("artifactVersion", artifactVersion);
		final var filters = factories.stream()
				.map(factory -> factory.create(artifactVersion))
				.collect(Collectors.toUnmodifiableSet());
		return version -> {
			Ensure.notNullAndNotEmpty("version", version);
			return filters.stream()
					.allMatch(filter -> filter.accept(version));
		};
	}
}