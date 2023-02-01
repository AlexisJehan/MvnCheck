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
package com.github.alexisjehan.mavencheck;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.assertj.core.api.Assertions.assertThat;

final class ApplicationIT {

	@Test
	void testRun(@TempDir final Path tmpDirectory) throws IOException {
		final var tmpMavenBuildFile = tmpDirectory.resolve("pom.xml");
		Files.copy(
				Path.of("src", "test", "resources", "pom_it.xml"),
				tmpMavenBuildFile,
				StandardCopyOption.REPLACE_EXISTING
		);
		final var tmpGroovyBuildDirectory = tmpDirectory.resolve("groovy");
		Files.createDirectory(tmpGroovyBuildDirectory);
		final var tmpGroovyBuildFile = tmpGroovyBuildDirectory.resolve("build.gradle");
		Files.copy(
				Path.of("src", "test", "resources", "build_it.gradle"),
				tmpGroovyBuildFile,
				StandardCopyOption.REPLACE_EXISTING
		);
		final var tmpKotlinBuildDirectory = tmpDirectory.resolve("kotlin");
		Files.createDirectory(tmpKotlinBuildDirectory);
		final var tmpKotlinBuildFile = tmpKotlinBuildDirectory.resolve("build.gradle.kts");
		Files.copy(
				Path.of("src", "test", "resources", "build_it.gradle.kts"),
				tmpKotlinBuildFile,
				StandardCopyOption.REPLACE_EXISTING
		);
		try (var outputStream = new ByteArrayOutputStream()) {
			try (var printStream = new PrintStream(outputStream)) {
				final var application = new Application(printStream);
				application.run(tmpDirectory, 1, false, false);
			}
			assertThat(outputStream.toString()).matches(
					"^"
							+ "3 build file\\(s\\) found, checking for artifact updates\\R"
							+ "\\R"
							+ ".+[/\\\\]pom\\.xml\\R"
							+ "\\[DEPENDENCY] com\\.google\\.android\\.material:material 1\\.0\\.0 -> "
							+ "\\d+\\.\\d+\\.\\d+\\R"
							+ "\\[DEPENDENCY] com\\.google\\.guava:guava 10\\.0 -> \\d+\\.\\d+(?:\\.\\d+)?-jre\\R"
							+ "\\[DEPENDENCY] org\\.springframework:spring-core 3\\.0\\.0\\.RELEASE -> "
							+ "\\d+\\.\\d+\\.\\d+\\R"
							+ "\\[PROFILE DEPENDENCY] com\\.google\\.guava:guava 23\\.1-jre -> "
							+ "\\d+\\.\\d+(?:\\.\\d+)?-jre\\R"
							+ "\\[PROFILE DEPENDENCY] com\\.google\\.guava:guava 23\\.1-android -> "
							+ "\\d+\\.\\d+(?:\\.\\d+)?-android\\R"
							+ "5 artifact update\\(s\\) available\\R"
							+ "\\R"
							+ ".+[/\\\\]groovy[/\\\\]build\\.gradle\\R"
							+ "\\[COMPILE ONLY] com\\.google\\.android\\.material:material 1\\.0\\.0 -> "
							+ "\\d+\\.\\d+\\.\\d+\\R"
							+ "\\[COMPILE ONLY] com\\.google\\.guava:guava 10\\.0 -> \\d+\\.\\d+(?:\\.\\d+)?-jre\\R"
							+ "\\[COMPILE ONLY] com\\.google\\.guava:guava 23\\.1-jre -> \\d+\\.\\d+(?:\\.\\d+)?-jre\\R"
							+ "\\[COMPILE ONLY] com\\.google\\.guava:guava 23\\.1-android -> "
							+ "\\d+\\.\\d+(?:\\.\\d+)?-android\\R"
							+ "\\[COMPILE ONLY] org\\.springframework:spring-core 3\\.0\\.0\\.RELEASE -> "
							+ "\\d+\\.\\d+\\.\\d+\\R"
							+ "5 artifact update\\(s\\) available\\R"
							+ "\\R"
							+ ".+[/\\\\]kotlin[/\\\\]build\\.gradle\\.kts\\R"
							+ "\\[COMPILE ONLY] com\\.google\\.android\\.material:material 1\\.0\\.0 -> "
							+ "\\d+\\.\\d+\\.\\d+\\R"
							+ "\\[COMPILE ONLY] com\\.google\\.guava:guava 10\\.0 -> \\d+\\.\\d+(?:\\.\\d+)?-jre\\R"
							+ "\\[COMPILE ONLY] com\\.google\\.guava:guava 23\\.1-jre -> \\d+\\.\\d+(?:\\.\\d+)?-jre\\R"
							+ "\\[COMPILE ONLY] com\\.google\\.guava:guava 23\\.1-android -> "
							+ "\\d+\\.\\d+(?:\\.\\d+)?-android\\R"
							+ "\\[COMPILE ONLY] org\\.springframework:spring-core 3\\.0\\.0\\.RELEASE -> "
							+ "\\d+\\.\\d+\\.\\d+\\R"
							+ "5 artifact update\\(s\\) available\\R"
							+ "\\R"
							+ "3/3 build file\\(s\\) checked, 15 artifact update\\(s\\) available\\R"
							+ "$"
			);
		}
	}
}