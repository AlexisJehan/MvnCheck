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
package com.github.alexisjehan.mvncheck.core.component.repository;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.Equals;
import com.github.alexisjehan.javanilla.misc.quality.HashCode;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import com.github.alexisjehan.javanilla.misc.tuple.Pair;

/**
 * <p>Class that describes a repository.</p>
 * <p><b>Note</b>: This class implements its own {@link #equals(Object)}, {@link #hashCode()} and {@link #toString()}
 * methods.</p>
 * @since 1.0.0
 */
public final class Repository {

	/**
	 * <p>Type.</p>
	 * @since 1.0.0
	 */
	private final RepositoryType type;

	/**
	 * <p>Identifier.</p>
	 * @since 1.0.0
	 */
	private final String id;

	/**
	 * <p>URL.</p>
	 * @since 1.0.0
	 */
	private final String url;

	/**
	 * <p>Constructor.</p>
	 * @param type a type
	 * @param id an identifier
	 * @param url an URL
	 * @throws NullPointerException if the type, the identifier or the URL is {@code null}
	 * @throws IllegalArgumentException if the identifier or the URL is empty
	 * @since 1.0.0
	 */
	public Repository(final RepositoryType type, final String id, final String url) {
		Ensure.notNull("type", type);
		Ensure.notNullAndNotEmpty("id", id);
		Ensure.notNullAndNotEmpty("url", url);
		this.type = type;
		this.id = id;
		this.url = url;
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
		if (!(object instanceof Repository)) {
			return false;
		}
		final var other = (Repository) object;
		return Equals.equals(type, other.type)
				&& Equals.equals(id, other.id)
				&& Equals.equals(url, other.url);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public int hashCode() {
		return HashCode.of(
				HashCode.hashCode(type),
				HashCode.hashCode(id),
				HashCode.hashCode(url)
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
				Pair.of("id", ToString.toString(id)),
				Pair.of("url", ToString.toString(url))
		);
	}

	/**
	 * <p>Get the type.</p>
	 * @return the type
	 * @since 1.0.0
	 */
	public RepositoryType getType() {
		return type;
	}

	/**
	 * <p>Get the identifier.</p>
	 * @return the identifier
	 * @since 1.0.0
	 */
	public String getId() {
		return id;
	}

	/**
	 * <p>Get the URL.</p>
	 * @return the URL
	 * @since 1.0.0
	 */
	public String getUrl() {
		return url;
	}
}