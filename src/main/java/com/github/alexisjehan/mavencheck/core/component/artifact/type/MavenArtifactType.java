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
package com.github.alexisjehan.mavencheck.core.component.artifact.type;

import com.github.alexisjehan.mavencheck.core.component.repository.RepositoryType;

/**
 * <p>Enumeration of <i>Maven</i> artifact types.</p>
 * <p><b>Note</b>: Based on {@code org.apache.maven:maven-core:3.9.0}.</p>
 * @see <a href="https://maven.apache.org/ref/current/maven-model/maven.html">https://maven.apache.org/ref/current/maven-model/maven.html</a>
 * @since 1.0.0
 */
public enum MavenArtifactType implements ArtifactType {

	/**
	 * <p>The parent artifact.</p>
	 * @since 1.0.0
	 */
	PARENT,

	/**
	 * <p>Dependency management dependency artifacts.</p>
	 * @since 1.0.0
	 */
	DEPENDENCY_MANAGEMENT_DEPENDENCY,

	/**
	 * <p>Dependency artifacts.</p>
	 * @since 1.0.0
	 */
	DEPENDENCY,

	/**
	 * <p>Build extension artifacts.</p>
	 * @since 1.0.0
	 */
	BUILD_EXTENSION,

	/**
	 * <p>Build plugin management plugin artifacts.</p>
	 * @since 1.0.0
	 */
	BUILD_PLUGIN_MANAGEMENT_PLUGIN(true),

	/**
	 * <p>Build plugin management plugin dependency artifacts.</p>
	 * @since 1.0.0
	 */
	BUILD_PLUGIN_MANAGEMENT_PLUGIN_DEPENDENCY,

	/**
	 * <p>Build plugin artifacts.</p>
	 * @since 1.0.0
	 */
	BUILD_PLUGIN(true),

	/**
	 * <p>Build plugin dependency artifacts.</p>
	 * @since 1.0.0
	 */
	BUILD_PLUGIN_DEPENDENCY,

	/**
	 * <p>Reporting plugin artifacts.</p>
	 * @since 1.0.0
	 */
	REPORTING_PLUGIN(true),

	/**
	 * <p>Profile build plugin management plugin artifacts.</p>
	 * @since 1.0.0
	 */
	PROFILE_BUILD_PLUGIN_MANAGEMENT_PLUGIN(true),

	/**
	 * <p>Profile build plugin management plugin dependency artifacts.</p>
	 * @since 1.0.0
	 */
	PROFILE_BUILD_PLUGIN_MANAGEMENT_PLUGIN_DEPENDENCY,

	/**
	 * <p>Profile build plugin artifacts.</p>
	 * @since 1.0.0
	 */
	PROFILE_BUILD_PLUGIN(true),

	/**
	 * <p>Profile build plugin dependency artifacts.</p>
	 * @since 1.0.0
	 */
	PROFILE_BUILD_PLUGIN_DEPENDENCY,

	/**
	 * <p>Profile dependency management dependency artifacts.</p>
	 * @since 1.0.0
	 */
	PROFILE_DEPENDENCY_MANAGEMENT_DEPENDENCY,

	/**
	 * <p>Profile dependency artifacts.</p>
	 * @since 1.0.0
	 */
	PROFILE_DEPENDENCY,

	/**
	 * <p>Profile reporting plugin artifacts.</p>
	 * @since 1.0.0
	 */
	PROFILE_REPORTING_PLUGIN(true);

	/**
	 * <p>Default value depending on whether any related artifact is a plugin or not.</p>
	 * @since 1.0.0
	 */
	private static final boolean DEFAULT_PLUGIN = false;

	/**
	 * <p>Repository type.</p>
	 * @since 1.0.0
	 */
	private final RepositoryType repositoryType;

	/**
	 * <p>Constructor by default.</p>
	 * @since 1.0.0
	 */
	MavenArtifactType() {
		this(DEFAULT_PLUGIN);
	}

	/**
	 * <p>Constructor depending on whether any related artifact is a plugin or not.</p>
	 * @param plugin {@code true} if any related artifact is a plugin
	 * @since 1.0.0
	 */
	MavenArtifactType(final boolean plugin) {
		repositoryType = plugin
				? RepositoryType.PLUGIN
				: RepositoryType.NORMAL;
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public final RepositoryType getRepositoryType() {
		return repositoryType;
	}
}