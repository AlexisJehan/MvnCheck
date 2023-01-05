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
package com.github.alexisjehan.mavencheck.core.component.artifact;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.Equals;
import com.github.alexisjehan.javanilla.misc.quality.HashCode;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import com.github.alexisjehan.javanilla.misc.tuple.Pair;
import com.github.alexisjehan.mavencheck.core.component.artifact.type.ArtifactType;

import java.util.Optional;

/**
 * <p>Class that describes an artifact.</p>
 * <p><b>Note</b>: This class implements its own {@link #equals(Object)}, {@link #hashCode()} and {@link #toString()}
 * methods.</p>
 * @param <T> the type
 * @since 1.0.0
 */
public final class Artifact<T extends ArtifactType> {

	/**
	 * <p>Type.</p>
	 * @since 1.0.0
	 */
	private final T type;

	/**
	 * <p>Identifier.</p>
	 * @since 1.0.0
	 */
	private final ArtifactIdentifier identifier;

	/**
	 * <p>Version.</p>
	 * @since 1.0.0
	 */
	private final String version;

	/**
	 * <p>Constructor without a version.</p>
	 * @param type a type
	 * @param identifier an identifier
	 * @throws NullPointerException if the type or the identifier is {@code null}
	 * @since 1.0.0
	 */
	public Artifact(final T type, final ArtifactIdentifier identifier) {
		this(type, identifier, null);
	}

	/**
	 * <p>Constructor with a version.</p>
	 * @param type a type
	 * @param identifier an identifier
	 * @param version a version or {@code null}
	 * @throws NullPointerException if the type or the identifier is {@code null}
	 * @since 1.0.0
	 */
	public Artifact(final T type, final ArtifactIdentifier identifier, final String version) {
		Ensure.notNull("type", type);
		Ensure.notNull("identifier", identifier);
		this.type = type;
		this.identifier = identifier;
		this.version = version;
	}

	/**
	 * <p>Return a copy of the current artifact with the given type.</p>
	 * @param type a type
	 * @return the copy of the current artifact
	 * @throws NullPointerException if the type is {@code null}
	 * @since 1.0.0
	 */
	public Artifact<T> with(final T type) {
		Ensure.notNull("type", type);
		return new Artifact<>(type, identifier, version);
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
		if (!(object instanceof Artifact)) {
			return false;
		}
		final var other = (Artifact<?>) object;
		return Equals.equals(type, other.type)
				&& Equals.equals(identifier, other.identifier)
				&& Equals.equals(version, other.version);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public int hashCode() {
		return HashCode.of(
				HashCode.hashCode(type),
				HashCode.hashCode(identifier),
				HashCode.hashCode(version)
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
				Pair.of("type", ToString.toString(type)),
				Pair.of("identifier", ToString.toString(identifier)),
				Pair.of("version", ToString.toString(version))
		);
	}

	/**
	 * <p>Get the type.</p>
	 * @return the type
	 * @since 1.0.0
	 */
	public T getType() {
		return type;
	}

	/**
	 * <p>Get the identifier.</p>
	 * @return the identifier
	 * @since 1.0.0
	 */
	public ArtifactIdentifier getIdentifier() {
		return identifier;
	}

	/**
	 * <p>Get an {@link Optional} of the version.</p>
	 * @return the {@link Optional} of the version
	 * @since 1.0.0
	 */
	public Optional<String> getOptionalVersion() {
		return Optional.ofNullable(version);
	}
}