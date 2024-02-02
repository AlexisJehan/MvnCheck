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

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.Optional;

/**
 * <p>Utility class that helps dealing with <i>GitHub</i>.</p>
 * @since 1.4.0
 */
public final class GithubUtils {

	/**
	 * <p>Threshold of the rate limit number of remaining requests available.</p>
	 * @since 1.4.0
	 */
	private static final int RATE_LIMIT_REMAINING_THRESHOLD = 3;

	/**
	 * <p>Constructor.</p>
	 * @since 1.4.0
	 */
	private GithubUtils() {}

	/**
	 * <p>Retrieve an {@link Optional} of the latest release name.</p>
	 * @param ownerName an owner name
	 * @param repositoryName a repository name
	 * @return the {@link Optional} of the latest release name
	 * @throws NullPointerException if the owner name or the repository name is {@code null}
	 * @throws IllegalArgumentException if the owner name or the repository name is empty
	 * @since 1.4.0
	 */
	public static Optional<String> retrieveOptionalLatestReleaseName(
			final String ownerName,
			final String repositoryName
	) {
		Ensure.notNullAndNotEmpty("ownerName", ownerName);
		Ensure.notNullAndNotEmpty("repositoryName", repositoryName);
		try {
			final var github = GitHub.connectAnonymously();
			final var rateLimit = github.getRateLimit();
			if (RATE_LIMIT_REMAINING_THRESHOLD > rateLimit.getRemaining()) {
				return Optional.empty();
			}
			final var repository = github.getRepository(ownerName + "/" + repositoryName);
			return Optional.ofNullable(repository.getLatestRelease())
					.map(GHRelease::getName);
		} catch (final IOException e) {
			return Optional.empty();
		}
	}
}