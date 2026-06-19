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
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

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

	@Test
	void testGetXdgConfigDirectories() {
		try (var mockedStaticSystemUtils = Mockito.mockStatic(SystemUtils.class)) {
			mockedStaticSystemUtils.when(() -> SystemUtils.getEnvironmentVariable("XDG_CONFIG_HOME"))
					.thenReturn(
							Optional.of(File.separatorChar + "xdg-config-home"),
							Optional.of(Strings.EMPTY),
							Optional.empty()
					);
			mockedStaticSystemUtils.when(() -> SystemUtils.getEnvironmentVariable("XDG_CONFIG_DIRS"))
					.thenReturn(
							Optional.of(
									File.separatorChar + "xdg-config-dirs1"
											+ File.pathSeparatorChar
											+ File.separatorChar + "xdg-config-dirs2"
							),
							Optional.of(Strings.EMPTY),
							Optional.empty()
					);
			mockedStaticSystemUtils.when(SystemUtils::getUserHomeDirectory)
					.thenCallRealMethod();
			mockedStaticSystemUtils.when(SystemUtils::getXdgConfigDirectories)
					.thenCallRealMethod();
			assertThat(SystemUtils.getXdgConfigDirectories()).containsExactly(
					Path.of(File.separatorChar + "xdg-config-home").resolve(SystemUtils.XDG_DIRECTORY_NAME),
					Path.of(File.separatorChar + "xdg-config-dirs1").resolve(SystemUtils.XDG_DIRECTORY_NAME),
					Path.of(File.separatorChar + "xdg-config-dirs2").resolve(SystemUtils.XDG_DIRECTORY_NAME)
			);
			assertThat(SystemUtils.getXdgConfigDirectories()).containsExactly(
					SystemUtils.getUserHomeDirectory().resolve(".config").resolve(SystemUtils.XDG_DIRECTORY_NAME),
					Path.of("/etc/xdg").resolve(SystemUtils.XDG_DIRECTORY_NAME)
			);
			assertThat(SystemUtils.getXdgConfigDirectories()).containsExactly(
					SystemUtils.getUserHomeDirectory().resolve(".config").resolve(SystemUtils.XDG_DIRECTORY_NAME),
					Path.of("/etc/xdg").resolve(SystemUtils.XDG_DIRECTORY_NAME)
			);
		}
	}
}