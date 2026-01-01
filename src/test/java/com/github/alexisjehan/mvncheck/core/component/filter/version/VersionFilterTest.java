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
package com.github.alexisjehan.mvncheck.core.component.filter.version;

import com.github.alexisjehan.javanilla.lang.Strings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class VersionFilterTest {

	@Test
	void testSnapshotAccept() {
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3snapshot")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3snapshot123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3snapshot.123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3snapshot-123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3SNAPSHOT")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3SNAPSHOT123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3SNAPSHOT.123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3SNAPSHOT-123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3.snapshot")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3.snapshot123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3.snapshot.123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3.snapshot-123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3.SNAPSHOT")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3.SNAPSHOT123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3.SNAPSHOT.123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3.SNAPSHOT-123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3-snapshot")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3-snapshot123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3-snapshot.123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3-snapshot-123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3-SNAPSHOT")).isTrue();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3-SNAPSHOT123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3-SNAPSHOT.123")).isFalse();
		assertThat(VersionFilter.SNAPSHOT.accept("1.2.3-SNAPSHOT-123")).isFalse();
	}

	@Test
	void testSnapshotAcceptInvalid() {
		assertThatNullPointerException().isThrownBy(() -> VersionFilter.SNAPSHOT.accept(null));
		assertThatIllegalArgumentException().isThrownBy(() -> VersionFilter.SNAPSHOT.accept(Strings.EMPTY));
	}
}