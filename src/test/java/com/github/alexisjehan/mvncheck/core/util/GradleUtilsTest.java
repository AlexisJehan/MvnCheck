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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

final class GradleUtilsTest {

	@Test
	void testRetrieveOptionalHome() {
		try (var mockedStaticSystemUtils = Mockito.mockStatic(SystemUtils.class)) {
			mockedStaticSystemUtils.when(() -> SystemUtils.getEnvironmentVariable("GRADLE_HOME"))
					.thenReturn(
							Optional.empty(),
							Optional.empty(),
							Optional.of(File.separatorChar + "gradle-1.0"),
							Optional.of(File.separatorChar + "gradle-1.0")
					);
			mockedStaticSystemUtils.when(SystemUtils::getPathEnvironmentVariable)
					.thenReturn(
							String.join(
									File.pathSeparator,
									"foo",
									"bar"
							),
							String.join(
									File.pathSeparator,
									"foo",
									File.separator + "gradle-1.0" + File.separator + "bin",
									"bar"
							),
							String.join(
									File.pathSeparator,
									"foo",
									"bar"
							),
							String.join(
									File.pathSeparator,
									"foo",
									File.separator + "gradle-1.0" + File.separator + "bin",
									"bar"
							)
					);
			assertThat(GradleUtils.retrieveOptionalHome()).isEmpty();
			assertThat(GradleUtils.retrieveOptionalHome()).contains(File.separator + "gradle-1.0");
			assertThat(GradleUtils.retrieveOptionalHome()).contains(File.separator + "gradle-1.0");
			assertThat(GradleUtils.retrieveOptionalHome()).contains(File.separator + "gradle-1.0");
		}
	}
}