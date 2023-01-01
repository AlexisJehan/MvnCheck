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
package com.github.alexisjehan.mavencheck.core.component.artifact.version;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.Equals;
import com.github.alexisjehan.javanilla.misc.quality.HashCode;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import com.github.alexisjehan.javanilla.misc.tuple.Pair;
import com.github.alexisjehan.mavencheck.core.component.artifact.Artifact;

import java.util.List;

/**
 * <p>Class that describes available versions for an artifact.</p>
 * @since 1.0.0
 */
public final class ArtifactAvailableVersions {

	/**
	 * <p>Artifact.</p>
	 * @since 1.0.0
	 */
	private final Artifact<?> artifact;

	/**
	 * <p>{@link List} of available versions.</p>
	 * @since 1.0.0
	 */
	private final List<String> availableVersions;

	/**
	 * <p>Constructor.</p>
	 * @param artifact an artifact
	 * @param availableVersions a {@link List} of available versions
	 * @throws NullPointerException if the artifact, the {@link List} of available versions or any of them is
	 *         {@code null}
	 * @since 1.0.0
	 */
	public ArtifactAvailableVersions(final Artifact<?> artifact, final List<String> availableVersions) {
		Ensure.notNull("artifact", artifact);
		Ensure.notNullAndNotNullElements("availableVersions", availableVersions);
		this.artifact = artifact;
		this.availableVersions = List.copyOf(availableVersions);
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
		if (!(object instanceof ArtifactAvailableVersions)) {
			return false;
		}
		final var other = (ArtifactAvailableVersions) object;
		return Equals.equals(artifact, other.artifact)
				&& Equals.equals(availableVersions, other.availableVersions);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public int hashCode() {
		return HashCode.of(
				HashCode.hashCode(artifact),
				HashCode.hashCode(availableVersions)
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
				Pair.of("availableVersions", ToString.toString(availableVersions))
		);
	}

	/**
	 * <p>Get the artifact.</p>
	 * @return the artifact
	 * @since 1.0.0
	 */
	public Artifact<?> getArtifact() {
		return artifact;
	}

	/**
	 * <p>Get the {@link List} of available versions.</p>
	 * @return the {@link List} of available versions
	 * @since 1.0.0
	 */
	public List<String> getAvailableVersions() {
		return availableVersions;
	}
}