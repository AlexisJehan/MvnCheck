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

import com.github.alexisjehan.mavencheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mavencheck.core.component.artifact.version.ArtifactAvailableVersions;
import com.github.alexisjehan.mavencheck.core.component.repository.Repository;

import java.util.List;

/**
 * <p>Interface that describes a resolver of available versions for an artifact.</p>
 * @since 1.0.0
 */
@FunctionalInterface
public interface ArtifactAvailableVersionsResolver {

	/**
	 * <p>Resolve available versions for an artifact.</p>
	 * @param artifact an artifact
	 * @param repositories a {@link List} of repositories
	 * @return available versions
	 * @since 1.0.0
	 */
	ArtifactAvailableVersions resolve(Artifact<?> artifact, List<Repository> repositories);
}