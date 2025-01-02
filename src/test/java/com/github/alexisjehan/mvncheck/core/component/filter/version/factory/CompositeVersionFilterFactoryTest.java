/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Alexis Jehan
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
package com.github.alexisjehan.mvncheck.core.component.filter.version.factory;

import com.github.alexisjehan.javanilla.lang.Strings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class CompositeVersionFilterFactoryTest {

	private static final VersionFilterFactory ALL = artifactVersion -> version -> false;

	private static final VersionFilterFactory NONE = artifactVersion -> version -> true;

	private final CompositeVersionFilterFactory compositeVersionFilterFactory = new CompositeVersionFilterFactory(ALL);

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException()
				.isThrownBy(() -> new CompositeVersionFilterFactory((VersionFilterFactory[]) null));
		assertThatIllegalArgumentException()
				.isThrownBy(CompositeVersionFilterFactory::new);
		assertThatNullPointerException()
				.isThrownBy(() -> new CompositeVersionFilterFactory((VersionFilterFactory) null));
	}

	@Test
	void testCreateInvalid() {
		assertThatNullPointerException().isThrownBy(() -> compositeVersionFilterFactory.create(null));
		assertThatIllegalArgumentException().isThrownBy(() -> compositeVersionFilterFactory.create(Strings.EMPTY));
	}

	@Test
	void testCreateAccept() {
		assertThat(new CompositeVersionFilterFactory(ALL).create("1.0.0").accept("1.2.3")).isFalse();
		assertThat(new CompositeVersionFilterFactory(NONE).create("1.0.0").accept("1.2.3")).isTrue();
		assertThat(new CompositeVersionFilterFactory(ALL, ALL).create("1.0.0").accept("1.2.3")).isFalse();
		assertThat(new CompositeVersionFilterFactory(ALL, NONE).create("1.0.0").accept("1.2.3")).isFalse();
		assertThat(new CompositeVersionFilterFactory(NONE, NONE).create("1.0.0").accept("1.2.3")).isTrue();
	}

	@Test
	void testCreateAcceptInvalid() {
		final var compositeVersionFilter = compositeVersionFilterFactory.create("1.0.0");
		assertThatNullPointerException().isThrownBy(() -> compositeVersionFilter.accept(null));
		assertThatIllegalArgumentException().isThrownBy(() -> compositeVersionFilter.accept(Strings.EMPTY));
	}
}