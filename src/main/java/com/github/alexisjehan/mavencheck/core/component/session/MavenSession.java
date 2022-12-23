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
package com.github.alexisjehan.mavencheck.core.component.session;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.util.Lists;
import com.github.alexisjehan.mavencheck.core.util.MavenUtils;
import org.apache.maven.model.building.ModelCache;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectModelResolver;
import org.apache.maven.project.PublicReactorModelCache;
import org.apache.maven.project.PublicReactorModelPool;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.SettingsBuildingException;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;

import java.util.List;

/**
 * <p>Class that describes a <i>Maven</i> session.</p>
 * @since 1.0.0
 */
public final class MavenSession {

	/**
	 * <p>Repository system.</p>
	 * @since 1.0.0
	 */
	private final RepositorySystem repositorySystem;

	/**
	 * <p>Repository system session.</p>
	 * @since 1.0.0
	 */
	private final RepositorySystemSession repositorySystemSession;

	/**
	 * <p>{@link List} of remote repositories.</p>
	 * @since 1.0.0
	 */
	private final List<RemoteRepository> remoteRepositories;

	/**
	 * <p>Model resolver.</p>
	 * @since 1.0.0
	 */
	private final ModelResolver modelResolver;

	/**
	 * <p>Model cache.</p>
	 * @since 1.0.0
	 */
	private final ModelCache modelCache;

	/**
	 * <p>Constructor.</p>
	 * @throws MavenSessionException might occur while resolving <i>Maven</i> settings
	 * @since 1.0.0
	 */
	public MavenSession() {
		final var serviceLocator = MavenUtils.makeServiceLocator();
		repositorySystem = MavenUtils.makeRepositorySystem(serviceLocator);
		final Settings settings;
		try {
			settings = MavenUtils.makeSettings();
		} catch (final SettingsBuildingException e) {
			throw new MavenSessionException(e);
		}
		final var decryptedSettings = MavenUtils.makeDecryptedSettings(settings);
		repositorySystemSession = MavenUtils.makeRepositorySystemSession(settings, decryptedSettings, repositorySystem);
		remoteRepositories = MavenUtils.makeRemoteRepositories(settings);
		modelResolver = new ProjectModelResolver(
				repositorySystemSession,
				null,
				repositorySystem,
				MavenUtils.makeRemoteRepositoryManager(serviceLocator),
				remoteRepositories,
				ProjectBuildingRequest.RepositoryMerging.POM_DOMINANT,
				new PublicReactorModelPool()
		);
		modelCache = new PublicReactorModelCache();
	}

	/**
	 * <p>Resolve remote repositories using the repository system session.</p>
	 * @param remoteRepositories a {@link List} of remote repositories
	 * @return the {@link List} of remote repositories
	 * @throws NullPointerException if the {@link List} of remote repositories or any of them is {@code null}
	 * @since 1.0.0
	 */
	public List<RemoteRepository> resolve(final List<RemoteRepository> remoteRepositories) {
		Ensure.notNullAndNotNullElements("remoteRepositories", remoteRepositories);
		return repositorySystem.newResolutionRepositories(
				repositorySystemSession,
				Lists.concat(this.remoteRepositories, remoteRepositories)
		);
	}

	/**
	 * <p>Request a version range using the repository system session.</p>
	 * @param request a request
	 * @return the result
	 * @throws VersionRangeResolutionException might occur while requesting an invalid version range
	 * @throws NullPointerException if the request is {@code null}
	 * @since 1.0.0
	 */
	public VersionRangeResult request(final VersionRangeRequest request) throws VersionRangeResolutionException {
		Ensure.notNull("request", request);
		return repositorySystem.resolveVersionRange(repositorySystemSession, request);
	}

	/**
	 * <p>Get the model resolver.</p>
	 * @return the model resolver
	 * @since 1.0.0
	 */
	public ModelResolver getModelResolver() {
		return modelResolver;
	}

	/**
	 * <p>Get the model cache.</p>
	 * @return the model cache
	 * @since 1.0.0
	 */
	public ModelCache getModelCache() {
		return modelCache;
	}
}