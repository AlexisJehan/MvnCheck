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
package com.github.alexisjehan.mavencheck.core.component.artifact.version.resolver;

import com.github.alexisjehan.mavencheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mavencheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mavencheck.core.component.artifact.type.MavenArtifactType;
import com.github.alexisjehan.mavencheck.core.component.repository.Repository;
import com.github.alexisjehan.mavencheck.core.component.repository.RepositoryType;
import com.github.alexisjehan.mavencheck.core.component.session.MavenSession;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

final class MavenArtifactAvailableVersionsResolverIT {

	private final MavenArtifactAvailableVersionsResolver mavenArtifactAvailableVersionsResolver
			= new MavenArtifactAvailableVersionsResolver(new MavenSession());

	@Test
	void testResolve() {
		assertThat(
				new Artifact<>(
						MavenArtifactType.DEPENDENCY,
						new ArtifactIdentifier("org.apache.maven", "maven-core")
				)
		).satisfies(artifact -> {
			assertThat(
					mavenArtifactAvailableVersionsResolver.resolve(
							artifact,
							List.of()
					).getAvailableVersions()
			).isEmpty();
			assertThat(
					mavenArtifactAvailableVersionsResolver.resolve(
							artifact,
							List.of(
									new Repository(
											RepositoryType.NORMAL,
											"central",
											"https://repo.maven.apache.org/maven2"
									)
							)
					).getAvailableVersions()
			).isNotEmpty();
			assertThat(
					mavenArtifactAvailableVersionsResolver.resolve(
							artifact,
							List.of(
									new Repository(
											RepositoryType.PLUGIN,
											"central",
											"https://repo.maven.apache.org/maven2"
									)
							)
					).getAvailableVersions()
			).isEmpty();
		});
		assertThat(
				new Artifact<>(
						MavenArtifactType.BUILD_PLUGIN,
						new ArtifactIdentifier("org.apache.maven.plugins", "maven-compiler-plugin")
				)
		).satisfies(artifact -> {
			assertThat(
					mavenArtifactAvailableVersionsResolver.resolve(
							artifact,
							List.of()
					).getAvailableVersions()
			).isEmpty();
			assertThat(
					mavenArtifactAvailableVersionsResolver.resolve(
							artifact,
							List.of(
									new Repository(
											RepositoryType.NORMAL,
											"central",
											"https://repo.maven.apache.org/maven2"
									)
							)
					).getAvailableVersions()
			).isNotEmpty();
			assertThat(
					mavenArtifactAvailableVersionsResolver.resolve(
							artifact,
							List.of(
									new Repository(
											RepositoryType.PLUGIN,
											"central",
											"https://repo.maven.apache.org/maven2"
									)
							)
					).getAvailableVersions()
			).isNotEmpty();
		});
	}
}