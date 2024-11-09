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

import java.util.Arrays;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class that describes a composite artifact filter.
 * @since 1.0.0
 */
public final class CompositeArtifactFilter implements ArtifactFilter {

	/**
	 * Match {@link BiPredicate}.
	 * @since 1.7.0
	 */
	private final BiPredicate<Stream<ArtifactFilter>, Predicate<ArtifactFilter>> matchFunction;

	/**
	 * {@link Set} of artifact filters.
	 * @since 1.0.0
	 */
	private final Set<ArtifactFilter> filters;

	/**
	 * Constructor.
	 * @param filters an array of artifact filters
	 * @throws NullPointerException if the array of artifacts or any of them is {@code null}
	 * @throws IllegalArgumentException if the array of artifacts is empty
	 * @deprecated since 1.7.0, use {@link #all(ArtifactFilter...)} instead
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.7.0")
	public CompositeArtifactFilter(final ArtifactFilter... filters) {
		this(
				Stream::allMatch,
				Ensure.notNullAndNotNullElements("filters", Ensure.notNullAndNotEmpty("filters", filters))
		);
	}

	/**
	 * Private constructor.
	 * @param matchFunction a match {@link BiPredicate}
	 * @param filters an array of artifact filters
	 * @since 1.7.0
	 */
	private CompositeArtifactFilter(
			final BiPredicate<Stream<ArtifactFilter>, Predicate<ArtifactFilter>> matchFunction,
			final ArtifactFilter... filters
	) {
		this.matchFunction = matchFunction;
		this.filters = Arrays.stream(filters)
				.collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the artifact is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public boolean accept(final Artifact<?> artifact) {
		Ensure.notNull("artifact", artifact);
		return matchFunction.test(filters.stream(), filter -> filter.accept(artifact));
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
		return matchFunction.test(filters.stream(), filter -> filter.accept(artifact, updateVersion));
	}

	/**
	 * Create an instance that test whether all filters are accepted.
	 * @param filters an array of artifact filters
	 * @return the created instance
	 * @throws NullPointerException if the array of artifacts or any of them is {@code null}
	 * @throws IllegalArgumentException if the array of artifacts is empty
	 * @since 1.7.0
	 */
	public static CompositeArtifactFilter all(final ArtifactFilter... filters) {
		Ensure.notNullAndNotEmpty("filters", filters);
		Ensure.notNullAndNotNullElements("filters", filters);
		return new CompositeArtifactFilter(Stream::allMatch, filters);
	}

	/**
	 * Create an instance that test whether any filters are accepted.
	 * @param filters an array of artifact filters
	 * @return the created instance
	 * @throws NullPointerException if the array of artifacts or any of them is {@code null}
	 * @throws IllegalArgumentException if the array of artifacts is empty
	 * @since 1.7.0
	 */
	public static CompositeArtifactFilter any(final ArtifactFilter... filters) {
		Ensure.notNullAndNotEmpty("filters", filters);
		Ensure.notNullAndNotNullElements("filters", filters);
		return new CompositeArtifactFilter(Stream::anyMatch, filters);
	}

	/**
	 * Create an instance that test whether no filters are accepted.
	 * @param filters an array of artifact filters
	 * @return the created instance
	 * @throws NullPointerException if the array of artifacts or any of them is {@code null}
	 * @throws IllegalArgumentException if the array of artifacts is empty
	 * @since 1.7.0
	 */
	public static CompositeArtifactFilter none(final ArtifactFilter... filters) {
		Ensure.notNullAndNotEmpty("filters", filters);
		Ensure.notNullAndNotNullElements("filters", filters);
		return new CompositeArtifactFilter(Stream::noneMatch, filters);
	}
}