/*
 * MIT License
 *
 * Copyright (c) 2022 Alexis Jehan
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

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class BuildFileTest {

	private static final BuildFileType TYPE = BuildFileType.MAVEN;
	private static final Path FILE = Path.of("foo-file");
	private static final BuildFileType OTHER_TYPE = BuildFileType.GRADLE_GROOVY;
	private static final Path OTHER_FILE = Path.of("bar-file");

	private final BuildFile buildFile = new BuildFile(TYPE, FILE);

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException().isThrownBy(() -> new BuildFile(null, FILE));
		assertThatNullPointerException().isThrownBy(() -> new BuildFile(TYPE, null));
	}

	@Test
	void testEqualsAndHashCodeAndToString() {
		assertThat(buildFile.equals(buildFile)).isTrue();
		assertThat(buildFile).isNotEqualTo(new Object());
		assertThat(new BuildFile(TYPE, FILE)).satisfies(otherBuildFile -> {
			assertThat(buildFile).isNotSameAs(otherBuildFile);
			assertThat(buildFile).isEqualTo(otherBuildFile);
			assertThat(buildFile).hasSameHashCodeAs(otherBuildFile);
			assertThat(buildFile).hasToString(otherBuildFile.toString());
		});
		assertThat(new BuildFile(OTHER_TYPE, FILE)).satisfies(otherBuildFile -> {
			assertThat(buildFile).isNotSameAs(otherBuildFile);
			assertThat(buildFile).isNotEqualTo(otherBuildFile);
			assertThat(buildFile).doesNotHaveSameHashCodeAs(otherBuildFile);
			assertThat(buildFile).doesNotHaveToString(otherBuildFile.toString());
		});
		assertThat(new BuildFile(TYPE, OTHER_FILE)).satisfies(otherBuildFile -> {
			assertThat(buildFile).isNotSameAs(otherBuildFile);
			assertThat(buildFile).isNotEqualTo(otherBuildFile);
			assertThat(buildFile).doesNotHaveSameHashCodeAs(otherBuildFile);
			assertThat(buildFile).doesNotHaveToString(otherBuildFile.toString());
		});
	}

	@Test
	void testGetType() {
		assertThat(buildFile.getType()).isEqualTo(TYPE);
	}

	@Test
	void testGetFile() {
		assertThat(buildFile.getFile()).isEqualTo(FILE);
	}
}