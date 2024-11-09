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

import com.github.alexisjehan.javanilla.lang.Strings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class RepositoryTest {

	private static final RepositoryType TYPE = RepositoryType.NORMAL;

	private static final String ID = "foo-id";

	private static final String URL = "https://foo-host";

	private static final RepositoryType OTHER_TYPE = RepositoryType.PLUGIN;

	private static final String OTHER_ID = "bar-id";

	private static final String OTHER_URL = "https://bar-host";

	private final Repository repository = new Repository(TYPE, ID, URL);

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException().isThrownBy(() -> new Repository(null, ID, URL));
		assertThatNullPointerException().isThrownBy(() -> new Repository(TYPE, null, URL));
		assertThatIllegalArgumentException().isThrownBy(() -> new Repository(TYPE, Strings.EMPTY, URL));
		assertThatNullPointerException().isThrownBy(() -> new Repository(TYPE, ID, null));
		assertThatIllegalArgumentException().isThrownBy(() -> new Repository(TYPE, ID, Strings.EMPTY));
	}

	@Test
	void testEqualsAndHashCodeAndToString() {
		assertThat(repository.equals(repository)).isTrue();
		assertThat(repository).isNotEqualTo(new Object());
		assertThat(new Repository(TYPE, ID, URL)).satisfies(otherRepository -> {
			assertThat(otherRepository).isNotSameAs(repository);
			assertThat(otherRepository).isEqualTo(repository);
			assertThat(otherRepository).hasSameHashCodeAs(repository);
			assertThat(otherRepository).hasToString(repository.toString());
		});
		assertThat(new Repository(OTHER_TYPE, ID, URL)).satisfies(otherRepository -> {
			assertThat(otherRepository).isNotSameAs(repository);
			assertThat(otherRepository).isNotEqualTo(repository);
			assertThat(otherRepository).doesNotHaveSameHashCodeAs(repository);
			assertThat(otherRepository).doesNotHaveToString(repository.toString());
		});
		assertThat(new Repository(TYPE, OTHER_ID, URL)).satisfies(otherRepository -> {
			assertThat(otherRepository).isNotSameAs(repository);
			assertThat(otherRepository).isNotEqualTo(repository);
			assertThat(otherRepository).doesNotHaveSameHashCodeAs(repository);
			assertThat(otherRepository).doesNotHaveToString(repository.toString());
		});
		assertThat(new Repository(TYPE, ID, OTHER_URL)).satisfies(otherRepository -> {
			assertThat(otherRepository).isNotSameAs(repository);
			assertThat(otherRepository).isNotEqualTo(repository);
			assertThat(otherRepository).doesNotHaveSameHashCodeAs(repository);
			assertThat(otherRepository).doesNotHaveToString(repository.toString());
		});
	}

	@Test
	void testGetType() {
		assertThat(repository.getType()).isEqualTo(TYPE);
	}

	@Test
	void testGetId() {
		assertThat(repository.getId()).isEqualTo(ID);
	}

	@Test
	void testGetUrl() {
		assertThat(repository.getUrl()).isEqualTo(URL);
	}
}