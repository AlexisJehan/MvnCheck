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
package com.github.alexisjehan.mavencheck.core.component.artifact.version.resolver;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import com.github.alexisjehan.mavencheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mavencheck.core.component.artifact.version.ArtifactAvailableVersions;
import com.github.alexisjehan.mavencheck.core.component.repository.Repository;
import com.github.alexisjehan.mavencheck.core.component.repository.RepositoryType;
import com.github.alexisjehan.mavencheck.core.component.session.MavenSession;
import com.github.alexisjehan.mavencheck.core.util.MavenUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.version.Version;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Class that describes a <i>Maven</i> resolver of available versions for an artifact.</p>
 * @since 1.0.0
 */
public final class MavenArtifactAvailableVersionsResolver implements ArtifactAvailableVersionsResolver {

	/**
	 * <p>Logger.</p>
	 * @since 1.0.0
	 */
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * <p><i>Maven</i> session.</p>
	 * @since 1.0.0
	 */
	private final MavenSession session;

	/**
	 * <p>Constructor.</p>
	 * @param session a <i>Maven</i> session
	 * @since 1.0.0
	 */
	public MavenArtifactAvailableVersionsResolver(final MavenSession session) {
		Ensure.notNull("session", session);
		this.session = session;
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the artifact, the {@link List} of repositories or any of them is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public ArtifactAvailableVersions resolve(final Artifact<?> artifact, final List<Repository> repositories) {
		Ensure.notNull("artifact", artifact);
		Ensure.notNullAndNotNullElements("repositories", repositories);
		logger.info("Resolving {} artifact available versions", () -> ToString.toString(artifact));
		final var artifactType = artifact.getType();
		final var artifactIdentifier = artifact.getIdentifier();
		final var resolvedRepositories = session.resolve(
				repositories.stream()
						.filter(
								repository -> RepositoryType.PLUGIN == artifactType.getRepositoryType()
										|| RepositoryType.NORMAL == repository.getType()
						)
						.map(
								repository -> MavenUtils.createRemoteRepository(
										repository.getId(),
										repository.getUrl()
								)
						)
						.collect(Collectors.toUnmodifiableList())
		);
		if (resolvedRepositories.isEmpty()) {
			throw new ArtifactAvailableVersionsResolveException("No remote repository has been resolved");
		}
		final var request = new VersionRangeRequest()
				.setArtifact(
						new DefaultArtifact(
								artifactIdentifier.getGroupId(),
								artifactIdentifier.getArtifactId(),
								null,
								"(,]"
						)
				)
				.setRepositories(resolvedRepositories);
		final VersionRangeResult result;
		try {
			result = session.request(request);
		} catch (final VersionRangeResolutionException e) {
			throw new AssertionError(e);
		}
		for (final var exception : result.getExceptions()) {
			logger.warn(exception::getMessage);
		}
		return new ArtifactAvailableVersions(
				artifact,
				result.getVersions()
						.stream()
						.map(Version::toString)
						.collect(Collectors.toUnmodifiableList())
		);
	}
}