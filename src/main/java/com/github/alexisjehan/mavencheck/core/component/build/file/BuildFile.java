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
package com.github.alexisjehan.mavencheck.core.component.build.file;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.Equals;
import com.github.alexisjehan.javanilla.misc.quality.HashCode;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import com.github.alexisjehan.javanilla.misc.tuple.Pair;

import java.nio.file.Path;

/**
 * <p>Class that describes a build file.</p>
 * @since 1.0.0
 */
public final class BuildFile {

	/**
	 * <p>Type.</p>
	 * @since 1.0.0
	 */
	private final BuildFileType type;

	/**
	 * <p>File.</p>
	 * @since 1.0.0
	 */
	private final Path file;

	/**
	 * <p>Constructor.</p>
	 * @param type a type
	 * @param file a file
	 * @throws NullPointerException if the type or the file is {@code null}
	 * @since 1.0.0
	 */
	public BuildFile(final BuildFileType type, final Path file) {
		Ensure.notNull("type", type);
		Ensure.notNull("file", file);
		this.type = type;
		this.file = file;
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
		if (!(object instanceof BuildFile)) {
			return false;
		}
		final var other = (BuildFile) object;
		return Equals.equals(type, other.type)
				&& Equals.equals(file, other.file);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public int hashCode() {
		return HashCode.of(
				HashCode.hashCode(type),
				HashCode.hashCode(file)
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
				Pair.of("file", ToString.toString(file))
		);
	}

	/**
	 * <p>Get the type.</p>
	 * @return the type
	 * @since 1.0.0
	 */
	public BuildFileType getType() {
		return type;
	}

	/**
	 * <p>Get the file.</p>
	 * @return the file
	 * @since 1.0.0
	 */
	public Path getFile() {
		return file;
	}
}