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
package com.github.alexisjehan.mvncheck.core.component.artifact;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.Equals;
import com.github.alexisjehan.javanilla.misc.quality.HashCode;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import com.github.alexisjehan.javanilla.misc.tuple.Pair;

/**
 * <p>Class that describes an artifact identifier.</p>
 * <p><b>Note</b>: This class implements its own {@link #equals(Object)}, {@link #hashCode()} and {@link #toString()}
 * methods.</p>
 * @since 1.0.0
 */
public final class ArtifactIdentifier {

	/**
	 * <p>Group identifier.</p>
	 * @since 1.0.0
	 */
	private final String groupId;

	/**
	 * <p>Artifact identifier.</p>
	 * @since 1.0.0
	 */
	private final String artifactId;

	/**
	 * <p>Constructor.</p>
	 * @param groupId a group identifier
	 * @param artifactId an artifact identifier
	 * @throws NullPointerException if the group identifier or the artifact identifier is {@code null}
	 * @throws IllegalArgumentException if the group identifier or the artifact identifier is empty
	 * @since 1.0.0
	 */
	public ArtifactIdentifier(final String groupId, final String artifactId) {
		Ensure.notNullAndNotEmpty("groupId", groupId);
		Ensure.notNullAndNotEmpty("artifactId", artifactId);
		this.groupId = groupId;
		this.artifactId = artifactId;
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
		if (!(object instanceof ArtifactIdentifier)) {
			return false;
		}
		final var other = (ArtifactIdentifier) object;
		return Equals.equals(groupId, other.groupId)
				&& Equals.equals(artifactId, other.artifactId);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public int hashCode() {
		return HashCode.of(
				HashCode.hashCode(groupId),
				HashCode.hashCode(artifactId)
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
				Pair.of("groupId", ToString.toString(groupId)),
				Pair.of("artifactId", ToString.toString(artifactId))
		);
	}

	/**
	 * <p>Get the group identifier.</p>
	 * @return the group identifier
	 * @since 1.0.0
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * <p>Get the artifact identifier.</p>
	 * @return the artifact identifier
	 * @since 1.0.0
	 */
	public String getArtifactId() {
		return artifactId;
	}
}