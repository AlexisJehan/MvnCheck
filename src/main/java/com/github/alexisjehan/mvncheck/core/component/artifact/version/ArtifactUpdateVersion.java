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
package com.github.alexisjehan.mvncheck.core.component.artifact.version;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.Equals;
import com.github.alexisjehan.javanilla.misc.quality.HashCode;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import com.github.alexisjehan.javanilla.misc.tuple.Pair;
import com.github.alexisjehan.mvncheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mvncheck.core.component.artifact.type.ArtifactType;

/**
 * Class that describes an update version for an artifact.
 *
 * <p><b>Note</b>: This class implements its own {@link #equals(Object)}, {@link #hashCode()} and {@link #toString()}
 * methods.</p>
 * @since 1.0.0
 */
public final class ArtifactUpdateVersion {

	/**
	 * Artifact.
	 * @since 1.0.0
	 */
	private final Artifact<ArtifactType> artifact;

	/**
	 * Update version.
	 * @since 1.0.0
	 */
	private final String updateVersion;

	/**
	 * Constructor.
	 * @param artifact an artifact
	 * @param updateVersion an update version
	 * @throws NullPointerException if the artifact or the update version is {@code null}
	 * @throws IllegalArgumentException if the update version is empty
	 * @since 1.0.0
	 */
	@SuppressWarnings("unchecked")
	public ArtifactUpdateVersion(final Artifact<?> artifact, final String updateVersion) {
		Ensure.notNull("artifact", artifact);
		Ensure.notNullAndNotEmpty("updateVersion", updateVersion);
		this.artifact = (Artifact<ArtifactType>) artifact;
		this.updateVersion = updateVersion;
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof final ArtifactUpdateVersion other)) {
			return false;
		}
		return Equals.equals(artifact, other.artifact)
				&& Equals.equals(updateVersion, other.updateVersion);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public int hashCode() {
		return HashCode.of(
				HashCode.hashCode(artifact),
				HashCode.hashCode(updateVersion)
		);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public String toString() {
		return ToString.of(
				this,
				Pair.of("artifact", ToString.toString(artifact)),
				Pair.of("updateVersion", ToString.toString(updateVersion))
		);
	}

	/**
	 * Get the artifact.
	 * @return the artifact
	 * @since 1.0.0
	 */
	public Artifact<ArtifactType> getArtifact() {
		return artifact;
	}

	/**
	 * Get the update version.
	 * @return the update version
	 * @since 1.0.0
	 */
	public String getUpdateVersion() {
		return updateVersion;
	}
}