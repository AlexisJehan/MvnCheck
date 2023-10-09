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
package com.github.alexisjehan.mvncheck.core.component.artifact.type;

import com.github.alexisjehan.mvncheck.core.component.repository.RepositoryType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

final class GradleArtifactTypeTest {

	@Test
	void testDependenciesTaskName() {
		assertThat(GradleArtifactType.COMPILE_ONLY.dependenciesTaskName()).isEqualTo("compileOnly");
	}

	@Test
	void testIsClasspath() {
		assertThat(GradleArtifactType.COMPILE_CLASSPATH.isClasspath()).isTrue();
		assertThat(GradleArtifactType.COMPILE_ONLY.isClasspath()).isFalse();
	}

	@Test
	void testIsDeprecated() {
		assertThat(GradleArtifactType.COMPILE.isDeprecated()).isTrue();
		assertThat(GradleArtifactType.COMPILE_ONLY.isDeprecated()).isFalse();
	}

	@Test
	void testGetRepositoryType() {
		assertThat(GradleArtifactType.COMPILE_ONLY.getRepositoryType()).isEqualTo(RepositoryType.NORMAL);
	}
}