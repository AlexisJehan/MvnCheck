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
package com.github.alexisjehan.mvncheck.core.component.filter.version.factory;

import com.github.alexisjehan.javanilla.lang.Strings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class QualifierVersionFilterFactoryTest {

	private final QualifierVersionFilterFactory qualifierVersionFilterFactory = new QualifierVersionFilterFactory();

	@Test
	void testCreateInvalid() {
		assertThatNullPointerException().isThrownBy(() -> qualifierVersionFilterFactory.create(null));
		assertThatIllegalArgumentException().isThrownBy(() -> qualifierVersionFilterFactory.create(Strings.EMPTY));
	}

	@Test
	void testCreateAccept() {
		// No qualifier
		assertThat(qualifierVersionFilterFactory.create("1.0.0")).satisfies(versionFilter -> {
			assertThat(versionFilter.accept("1.2.3")).isTrue();
			assertThat(versionFilter.accept("1.2.3foo")).isTrue();
			assertThat(versionFilter.accept("1.2.3foo123")).isTrue();
			assertThat(versionFilter.accept("1.2.3foo.123")).isTrue();
			assertThat(versionFilter.accept("1.2.3foo-123")).isTrue();
			assertThat(versionFilter.accept("1.2.3FOO")).isTrue();
			assertThat(versionFilter.accept("1.2.3FOO123")).isTrue();
			assertThat(versionFilter.accept("1.2.3FOO.123")).isTrue();
			assertThat(versionFilter.accept("1.2.3FOO-123")).isTrue();
			assertThat(versionFilter.accept("1.2.3.foo")).isTrue();
			assertThat(versionFilter.accept("1.2.3.foo123")).isTrue();
			assertThat(versionFilter.accept("1.2.3.foo.123")).isTrue();
			assertThat(versionFilter.accept("1.2.3.foo-123")).isTrue();
			assertThat(versionFilter.accept("1.2.3.FOO")).isTrue();
			assertThat(versionFilter.accept("1.2.3.FOO123")).isTrue();
			assertThat(versionFilter.accept("1.2.3.FOO.123")).isTrue();
			assertThat(versionFilter.accept("1.2.3.FOO-123")).isTrue();
			assertThat(versionFilter.accept("1.2.3-foo")).isTrue();
			assertThat(versionFilter.accept("1.2.3-foo123")).isTrue();
			assertThat(versionFilter.accept("1.2.3-foo.123")).isTrue();
			assertThat(versionFilter.accept("1.2.3-foo-123")).isTrue();
			assertThat(versionFilter.accept("1.2.3-FOO")).isTrue();
			assertThat(versionFilter.accept("1.2.3-FOO123")).isTrue();
			assertThat(versionFilter.accept("1.2.3-FOO.123")).isTrue();
			assertThat(versionFilter.accept("1.2.3-FOO-123")).isTrue();
		});

		// Same qualifier
		assertThat(qualifierVersionFilterFactory.create("1.0.0-foo")).satisfies(versionFilter -> {
			assertThat(versionFilter.accept("1.2.3")).isTrue();
			assertThat(versionFilter.accept("1.2.3foo")).isTrue();
			assertThat(versionFilter.accept("1.2.3foo123")).isTrue();
			assertThat(versionFilter.accept("1.2.3foo.123")).isTrue();
			assertThat(versionFilter.accept("1.2.3foo-123")).isTrue();
			assertThat(versionFilter.accept("1.2.3FOO")).isTrue();
			assertThat(versionFilter.accept("1.2.3FOO123")).isTrue();
			assertThat(versionFilter.accept("1.2.3FOO.123")).isTrue();
			assertThat(versionFilter.accept("1.2.3FOO-123")).isTrue();
			assertThat(versionFilter.accept("1.2.3.foo")).isTrue();
			assertThat(versionFilter.accept("1.2.3.foo123")).isTrue();
			assertThat(versionFilter.accept("1.2.3.foo.123")).isTrue();
			assertThat(versionFilter.accept("1.2.3.foo-123")).isTrue();
			assertThat(versionFilter.accept("1.2.3.FOO")).isTrue();
			assertThat(versionFilter.accept("1.2.3.FOO123")).isTrue();
			assertThat(versionFilter.accept("1.2.3.FOO.123")).isTrue();
			assertThat(versionFilter.accept("1.2.3.FOO-123")).isTrue();
			assertThat(versionFilter.accept("1.2.3-foo")).isTrue();
			assertThat(versionFilter.accept("1.2.3-foo123")).isTrue();
			assertThat(versionFilter.accept("1.2.3-foo.123")).isTrue();
			assertThat(versionFilter.accept("1.2.3-foo-123")).isTrue();
			assertThat(versionFilter.accept("1.2.3-FOO")).isTrue();
			assertThat(versionFilter.accept("1.2.3-FOO123")).isTrue();
			assertThat(versionFilter.accept("1.2.3-FOO.123")).isTrue();
			assertThat(versionFilter.accept("1.2.3-FOO-123")).isTrue();
		});

		// Not same qualifier
		assertThat(qualifierVersionFilterFactory.create("1.0.0-bar")).satisfies(versionFilter -> {
			assertThat(versionFilter.accept("1.2.3")).isTrue();
			assertThat(versionFilter.accept("1.2.3foo")).isFalse();
			assertThat(versionFilter.accept("1.2.3foo123")).isFalse();
			assertThat(versionFilter.accept("1.2.3foo.123")).isFalse();
			assertThat(versionFilter.accept("1.2.3foo-123")).isFalse();
			assertThat(versionFilter.accept("1.2.3FOO")).isFalse();
			assertThat(versionFilter.accept("1.2.3FOO123")).isFalse();
			assertThat(versionFilter.accept("1.2.3FOO.123")).isFalse();
			assertThat(versionFilter.accept("1.2.3FOO-123")).isFalse();
			assertThat(versionFilter.accept("1.2.3.foo")).isFalse();
			assertThat(versionFilter.accept("1.2.3.foo123")).isFalse();
			assertThat(versionFilter.accept("1.2.3.foo.123")).isFalse();
			assertThat(versionFilter.accept("1.2.3.foo-123")).isFalse();
			assertThat(versionFilter.accept("1.2.3.FOO")).isFalse();
			assertThat(versionFilter.accept("1.2.3.FOO123")).isFalse();
			assertThat(versionFilter.accept("1.2.3.FOO.123")).isFalse();
			assertThat(versionFilter.accept("1.2.3.FOO-123")).isFalse();
			assertThat(versionFilter.accept("1.2.3-foo")).isFalse();
			assertThat(versionFilter.accept("1.2.3-foo123")).isFalse();
			assertThat(versionFilter.accept("1.2.3-foo.123")).isFalse();
			assertThat(versionFilter.accept("1.2.3-foo-123")).isFalse();
			assertThat(versionFilter.accept("1.2.3-FOO")).isFalse();
			assertThat(versionFilter.accept("1.2.3-FOO123")).isFalse();
			assertThat(versionFilter.accept("1.2.3-FOO.123")).isFalse();
			assertThat(versionFilter.accept("1.2.3-FOO-123")).isFalse();
		});
	}

	@Test
	void testCreateAcceptInvalid() {
		final var versionFilter = qualifierVersionFilterFactory.create("1.0.0");
		assertThatNullPointerException().isThrownBy(() -> versionFilter.accept(null));
		assertThatIllegalArgumentException().isThrownBy(() -> versionFilter.accept(Strings.EMPTY));
	}
}