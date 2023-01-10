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
package com.github.alexisjehan.mavencheck.core.component.build;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.Equals;
import com.github.alexisjehan.javanilla.misc.quality.HashCode;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import com.github.alexisjehan.javanilla.misc.tuple.Pair;
import com.github.alexisjehan.mavencheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mavencheck.core.component.artifact.type.ArtifactType;
import com.github.alexisjehan.mavencheck.core.component.build.file.BuildFile;
import com.github.alexisjehan.mavencheck.core.component.repository.Repository;

import java.util.List;

/**
 * <p>Class that describes a build.</p>
 * <p><b>Note</b>: This class implements its own {@link #equals(Object)}, {@link #hashCode()} and {@link #toString()}
 * methods.</p>
 * @since 1.0.0
 */
public final class Build {

	/**
	 * <p>File.</p>
	 * @since 1.0.0
	 */
	private final BuildFile file;

	/**
	 * <p>{@link List} of repositories.</p>
	 * @since 1.0.0
	 */
	private final List<Repository> repositories;

	/**
	 * <p>{@link List} of artifacts.</p>
	 * @since 1.0.0
	 */
	private final List<Artifact<ArtifactType>> artifacts;

	/**
	 * <p>Constructor.</p>
	 * @param file a file
	 * @param repositories a {@link List} of repositories
	 * @param artifacts a {@link List} of artifacts
	 * @throws NullPointerException if the file, the {@link List} of repositories, any of them, the {@link List} of
	 *         artifacts or any of them is {@code null}
	 * @since 1.0.0
	 */
	@SuppressWarnings("unchecked")
	public Build(
			final BuildFile file,
			final List<Repository> repositories,
			final List<? extends Artifact<?>> artifacts
	) {
		Ensure.notNull("file", file);
		Ensure.notNullAndNotNullElements("repositories", repositories);
		Ensure.notNullAndNotNullElements("artifacts", artifacts);
		this.file = file;
		this.repositories = List.copyOf(repositories);
		this.artifacts = (List<Artifact<ArtifactType>>) List.copyOf(artifacts);
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
		if (!(object instanceof Build)) {
			return false;
		}
		final var other = (Build) object;
		return Equals.equals(file, other.file)
				&& Equals.equals(repositories, other.repositories)
				&& Equals.equals(artifacts, other.artifacts);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public int hashCode() {
		return HashCode.of(
				HashCode.hashCode(file),
				HashCode.hashCode(repositories),
				HashCode.hashCode(artifacts)
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
				Pair.of("file", ToString.toString(file)),
				Pair.of("repositories", ToString.toString(repositories)),
				Pair.of("artifacts", ToString.toString(artifacts))
		);
	}

	/**
	 * <p>Get the file.</p>
	 * @return the file
	 * @since 1.0.0
	 */
	public BuildFile getFile() {
		return file;
	}

	/**
	 * <p>Get the {@link List} of repositories.</p>
	 * @return the {@link List} of repositories
	 * @since 1.0.0
	 */
	public List<Repository> getRepositories() {
		return repositories;
	}

	/**
	 * <p>Get the {@link List} of artifacts.</p>
	 * @return the {@link List} of artifacts
	 * @since 1.0.0
	 */
	public List<Artifact<ArtifactType>> getArtifacts() {
		return artifacts;
	}
}