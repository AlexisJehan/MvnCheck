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
package com.github.alexisjehan.mavencheck.core.component.filter.artifact;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.mavencheck.core.component.artifact.Artifact;

/**
 * <p>Interface that describes an artifact filter.</p>
 * @since 1.0.0
 */
public interface ArtifactFilter {

	/**
	 * <p>Artifact filter that reject all artifacts.</p>
	 * @since 1.0.0
	 */
	ArtifactFilter ALL = new ArtifactFilter() {

		/**
		 * {@inheritDoc}
		 * @throws NullPointerException if the artifact is {@code null}
		 * @since 1.0.0
		 */
		@Override
		public boolean accept(final Artifact<?> artifact) {
			Ensure.notNull("artifact", artifact);
			return false;
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
			return false;
		}
	};

	/**
	 * <p>Artifact filter that accept all artifacts.</p>
	 * @since 1.0.0
	 */
	ArtifactFilter NONE = new ArtifactFilter() {

		/**
		 * {@inheritDoc}
		 * @throws NullPointerException if the artifact is {@code null}
		 * @since 1.0.0
		 */
		@Override
		public boolean accept(final Artifact<?> artifact) {
			Ensure.notNull("artifact", artifact);
			return true;
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
			return true;
		}
	};

	/**
	 * <p>Test if an artifact is accepted.</p>
	 * @param artifact an artifact
	 * @return {@code true} if the artifact is accepted
	 * @since 1.0.0
	 */
	boolean accept(final Artifact<?> artifact);

	/**
	 * <p>Test if an artifact and an update version are accepted.</p>
	 * @param artifact an artifact
	 * @param updateVersion an update version
	 * @return {@code true} if the artifact and the update version are accepted
	 * @since 1.0.0
	 */
	boolean accept(final Artifact<?> artifact, final String updateVersion);
}