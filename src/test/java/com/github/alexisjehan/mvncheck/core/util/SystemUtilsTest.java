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
package com.github.alexisjehan.mvncheck.core.util;

import com.github.alexisjehan.javanilla.lang.Strings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class SystemUtilsTest {

	@Test
	void testGetEnvironmentVariable() {
		assertThat(SystemUtils.getEnvironmentVariable("PATH")).isPresent();
		assertThat(SystemUtils.getEnvironmentVariable("NOT_FOUND")).isEmpty();
	}

	@Test
	void testGetEnvironmentVariableInvalid() {
		assertThatNullPointerException().isThrownBy(() -> SystemUtils.getEnvironmentVariable(null));
		assertThatIllegalArgumentException().isThrownBy(() -> SystemUtils.getEnvironmentVariable(Strings.EMPTY));
	}

	@Test
	void testGetPathEnvironmentVariable() {
		assertThat(SystemUtils.getPathEnvironmentVariable()).isNotEmpty();
	}

	@Test
	void testGetUserHomeDirectory() {
		assertThat(SystemUtils.getUserHomeDirectory()).isDirectory();
	}
}