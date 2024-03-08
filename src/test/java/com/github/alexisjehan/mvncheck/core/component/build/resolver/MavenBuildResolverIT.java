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
package com.github.alexisjehan.mvncheck.core.component.build.resolver;

import com.github.alexisjehan.mvncheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mvncheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mvncheck.core.component.artifact.type.MavenArtifactType;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFile;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFileType;
import com.github.alexisjehan.mvncheck.core.component.repository.Repository;
import com.github.alexisjehan.mvncheck.core.component.repository.RepositoryType;
import com.github.alexisjehan.mvncheck.core.component.session.MavenSession;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

final class MavenBuildResolverIT {

	private final MavenBuildResolver mavenBuildResolver = new MavenBuildResolver(new MavenSession());

	@Test
	void testResolve() {
		final var buildFile = new BuildFile(
				BuildFileType.MAVEN,
				Path.of("src", "test", "resources", "pom_it.xml")
		);
		final var build = mavenBuildResolver.resolve(buildFile);
		assertThat(build.getFile()).isSameAs(buildFile);
		assertThat(build.getRepositories()).containsExactly(
				new Repository(
						RepositoryType.NORMAL,
						"google",
						"https://maven.google.com"
				),
				new Repository(
						RepositoryType.NORMAL,
						"central",
						"https://repo.maven.apache.org/maven2"
				),
				new Repository(
						RepositoryType.PLUGIN,
						"central",
						"https://repo.maven.apache.org/maven2"
				)
		);
		assertThat(build.getArtifacts()).containsExactly(
				new Artifact<>(
						MavenArtifactType.DEPENDENCY_MANAGEMENT_DEPENDENCY,
						new ArtifactIdentifier("org.springframework.boot", "spring-boot-dependencies"),
						"1.0.0.RELEASE"
				),
				new Artifact<>(
						MavenArtifactType.DEPENDENCY,
						new ArtifactIdentifier("com.google.android.material", "material"),
						"1.0.0"
				),
				new Artifact<>(
						MavenArtifactType.DEPENDENCY,
						new ArtifactIdentifier("com.google.guava", "guava"),
						"10.0"
				),
				new Artifact<>(
						MavenArtifactType.DEPENDENCY,
						new ArtifactIdentifier("org.springframework", "spring-core"),
						"3.0.0.RELEASE"
				),
				new Artifact<>(
						MavenArtifactType.PROFILE_DEPENDENCY,
						new ArtifactIdentifier("com.google.guava", "guava"),
						"23.1-jre"
				),
				new Artifact<>(
						MavenArtifactType.PROFILE_DEPENDENCY,
						new ArtifactIdentifier("com.google.guava", "guava"),
						"23.1-android"
				)
		);
	}
}