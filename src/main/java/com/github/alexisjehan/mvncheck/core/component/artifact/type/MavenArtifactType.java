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
package com.github.alexisjehan.mvncheck.core.component.artifact.type;

import com.github.alexisjehan.mvncheck.core.component.repository.RepositoryType;

/**
 * Enumeration of <i>Maven</i> artifact types.
 *
 * <p><b>Note</b>: Based on {@code org.apache.maven:maven-core:3.9.12}.</p>
 * @see <a href="https://maven.apache.org/ref/current/maven-model/maven.html">https://maven.apache.org/ref/current/maven-model/maven.html</a>
 * @since 1.0.0
 */
public enum MavenArtifactType implements ArtifactType {

	/**
	 * The parent artifact.
	 * @since 1.0.0
	 */
	PARENT,

	/**
	 * Dependency management dependency artifacts.
	 * @since 1.0.0
	 */
	DEPENDENCY_MANAGEMENT_DEPENDENCY,

	/**
	 * Dependency artifacts.
	 * @since 1.0.0
	 */
	DEPENDENCY,

	/**
	 * Build extension artifacts.
	 * @since 1.0.0
	 */
	BUILD_EXTENSION,

	/**
	 * Build plugin management plugin artifacts.
	 * @since 1.0.0
	 */
	BUILD_PLUGIN_MANAGEMENT_PLUGIN(true),

	/**
	 * Build plugin management plugin dependency artifacts.
	 * @since 1.0.0
	 */
	BUILD_PLUGIN_MANAGEMENT_PLUGIN_DEPENDENCY,

	/**
	 * Build plugin artifacts.
	 * @since 1.0.0
	 */
	BUILD_PLUGIN(true),

	/**
	 * Build plugin dependency artifacts.
	 * @since 1.0.0
	 */
	BUILD_PLUGIN_DEPENDENCY,

	/**
	 * Reporting plugin artifacts.
	 * @since 1.0.0
	 */
	REPORTING_PLUGIN(true),

	/**
	 * Profile build plugin management plugin artifacts.
	 * @since 1.0.0
	 */
	PROFILE_BUILD_PLUGIN_MANAGEMENT_PLUGIN(true),

	/**
	 * Profile build plugin management plugin dependency artifacts.
	 * @since 1.0.0
	 */
	PROFILE_BUILD_PLUGIN_MANAGEMENT_PLUGIN_DEPENDENCY,

	/**
	 * Profile build plugin artifacts.
	 * @since 1.0.0
	 */
	PROFILE_BUILD_PLUGIN(true),

	/**
	 * Profile build plugin dependency artifacts.
	 * @since 1.0.0
	 */
	PROFILE_BUILD_PLUGIN_DEPENDENCY,

	/**
	 * Profile dependency management dependency artifacts.
	 * @since 1.0.0
	 */
	PROFILE_DEPENDENCY_MANAGEMENT_DEPENDENCY,

	/**
	 * Profile dependency artifacts.
	 * @since 1.0.0
	 */
	PROFILE_DEPENDENCY,

	/**
	 * Profile reporting plugin artifacts.
	 * @since 1.0.0
	 */
	PROFILE_REPORTING_PLUGIN(true);

	/**
	 * Default value depending on whether any related artifact is a plugin.
	 * @since 1.0.0
	 */
	private static final boolean DEFAULT_PLUGIN = false;

	/**
	 * Repository type.
	 * @since 1.0.0
	 */
	private final RepositoryType repositoryType;

	/**
	 * Constructor.
	 * @since 1.0.0
	 */
	MavenArtifactType() {
		this(DEFAULT_PLUGIN);
	}

	/**
	 * Constructor depending on whether any related artifact is a plugin.
	 * @param plugin {@code true} if any related artifact is a plugin
	 * @since 1.0.0
	 */
	MavenArtifactType(final boolean plugin) {
		repositoryType = plugin ? RepositoryType.PLUGIN : RepositoryType.NORMAL;
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public RepositoryType getRepositoryType() {
		return repositoryType;
	}
}