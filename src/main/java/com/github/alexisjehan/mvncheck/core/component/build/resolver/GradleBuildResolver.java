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
package com.github.alexisjehan.mvncheck.core.component.build.resolver;

import com.github.alexisjehan.javanilla.io.Readers;
import com.github.alexisjehan.javanilla.lang.Strings;
import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import com.github.alexisjehan.mvncheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mvncheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mvncheck.core.component.artifact.type.GradleArtifactType;
import com.github.alexisjehan.mvncheck.core.component.build.Build;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFile;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFileType;
import com.github.alexisjehan.mvncheck.core.component.repository.Repository;
import com.github.alexisjehan.mvncheck.core.component.repository.RepositoryType;
import com.github.alexisjehan.mvncheck.core.util.GradleUtils;
import internal.ExcludeFromJacocoGeneratedReport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gradle.tooling.GradleConnectionException;
import org.gradle.tooling.GradleConnector;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Class that describes a <i>Gradle</i> resolver of the build for a file.</p>
 * <p><b>Note</b>: Compatible starting with <i>Gradle 4.8</i>.</p>
 * @since 1.0.0
 */
public final class GradleBuildResolver implements BuildResolver {

	/**
	 * <p>Initialisation file name.</p>
	 * @since 1.0.0
	 */
	private static final String INIT_FILE_NAME = "init.gradle";

	/**
	 * <p>{@link Set} of file types.</p>
	 * @since 1.0.0
	 */
	private static final Set<BuildFileType> FILE_TYPES = Set.of(
			BuildFileType.GRADLE_GROOVY,
			BuildFileType.GRADLE_KOTLIN
	);

	/**
	 * <p>Logger.</p>
	 * @since 1.0.0
	 */
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * <p>Constructor.</p>
	 * @since 1.0.0
	 */
	public GradleBuildResolver() {
		// Empty
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the file is {@code null}
	 * @since 1.0.0
	 */
	@Override
	@ExcludeFromJacocoGeneratedReport
	public Build resolve(final BuildFile file) {
		Ensure.notNull("file", file);
		try (var outputStream = new ByteArrayOutputStream()) {
			return resolve(file, outputStream);
		} catch (final IOException e) {
			throw new AssertionError(e);
		}
	}

	/**
	 * <p>Resolve the build for a file using an {@link OutputStream}.</p>
	 * @param file a file
	 * @param outputStream an {@link OutputStream}
	 * @return the build
	 * @throws IOException might occur with input/output operations
	 * @throws BuildResolveException might occur while resolving the build
	 * @since 1.0.0
	 */
	private Build resolve(final BuildFile file, final OutputStream outputStream) throws IOException {
		logger.info("Resolving the {} build", () -> ToString.toString(file));
		final var connector = GradleConnector.newConnector()
				.forProjectDirectory(file.getFile().getParent().toFile());
		GradleUtils.retrieveOptionalHome()
				.map(Path::of)
				.ifPresent(installation -> {
					logger.debug("Using the {} installation", () -> ToString.toString(installation));
					connector.useInstallation(installation.toFile());
				});
		try (var connection = connector.connect()) {
			connection.newBuild()
					.forTasks("repositories", "dependencies")
					.withArguments("--init-script=" + getClass().getClassLoader().getResource(INIT_FILE_NAME))
					.setStandardOutput(outputStream)
					.run();
		} catch (final GradleConnectionException e) {
			throw new BuildResolveException(e);
		} finally {
			connector.disconnect();
		}
		try (var reader = Readers.buffered(Readers.of(outputStream.toString()))) {
			logger.trace("Parsing repositories");
			final var repositories = parseRepositories(reader);
			logger.debug("Parsed repositories:");
			repositories.forEach(
					repository -> logger.debug("- {}", () -> ToString.toString(repository))
			);

			logger.trace("Filtering repositories");
			final var filteredRepositories = filterRepositories(repositories);
			logger.debug("Filtered repositories:");
			filteredRepositories.forEach(
					filteredRepository -> logger.debug("- {}", () -> ToString.toString(filteredRepository))
			);

			logger.trace("Parsing artifacts");
			final var artifacts = parseArtifacts(reader);
			logger.debug("Parsed artifacts:");
			artifacts.forEach(
					artifact -> logger.debug("- {}", () -> ToString.toString(artifact))
			);

			logger.trace("Filtering artifacts");
			final var filteredArtifacts = filterArtifacts(artifacts);
			logger.debug("Filtered artifacts:");
			filteredArtifacts.forEach(
					filteredArtifact -> logger.debug("- {}", () -> ToString.toString(filteredArtifact))
			);

			return new Build(file, filteredRepositories, filteredArtifacts);
		}
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public Set<BuildFileType> getFileTypes() {
		return FILE_TYPES;
	}

	/**
	 * <p>Parse a {@link List} of repositories from a {@link BufferedReader} of the <i>Gradle</i> {@code :repositories}
	 * task.</p>
	 * @param bufferedReader a {@link BufferedReader}
	 * @return the {@link List} of repositories
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the {@link BufferedReader} is {@code null}
	 * @throws BuildResolveException if the <i>Gradle</i> {@code :repositories} task output is unexpected
	 * @since 1.0.0
	 */
	static List<Repository> parseRepositories(final BufferedReader bufferedReader) throws IOException {
		Ensure.notNull("bufferedReader", bufferedReader);
		final var repositories = new ArrayList<Repository>();
		String line;
		while (null != (line = bufferedReader.readLine())) {
			if ("> Task :repositories".equals(line)) {
				while (null != (line = bufferedReader.readLine())) {
					if (line.isEmpty()) {
						break;
					}
					if (2 > Strings.frequency(line, ':')) {
						throw new BuildResolveException("Unexpected Gradle \":repositories\" repository format");
					}
					final var parts = Strings.split(':', line, 3);
					final RepositoryType repositoryType;
					try {
						repositoryType = RepositoryType.valueOf(parts.get(0));
					} catch (final IllegalArgumentException e) {
						throw new BuildResolveException(
								"Unexpected Gradle \":repositories\" repository type format",
								e
						);
					}
					final var id = parts.get(1);
					final var url = parts.get(2);
					repositories.add(new Repository(repositoryType, id, url));
				}
				break;
			}
		}
		return repositories;
	}

	/**
	 * <p>Parse a {@link List} of artifacts from a {@link BufferedReader} of the <i>Gradle</i> {@code :dependencies}
	 * task.</p>
	 * @param bufferedReader a {@link BufferedReader}
	 * @return the {@link List} of artifacts
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the {@link BufferedReader} is {@code null}
	 * @throws BuildResolveException if the <i>Gradle</i> {@code :dependencies} task output is unexpected
	 * @since 1.0.0
	 */
	static List<Artifact<GradleArtifactType>> parseArtifacts(final BufferedReader bufferedReader) throws IOException {
		Ensure.notNull("bufferedReader", bufferedReader);
		final var artifacts = new ArrayList<Artifact<GradleArtifactType>>();
		String line;
		while (null != (line = bufferedReader.readLine())) {
			if ("> Task :dependencies".equals(line)) {
				if (!Strings.EMPTY.equals(bufferedReader.readLine())
						|| !"-".repeat(60).equals(bufferedReader.readLine())

						// Until Gradle 6.7: "Root project"
						// Since Gradle 6.8: "Root project 'foo'"
						|| !Strings.nullToEmpty(bufferedReader.readLine()).startsWith("Root project")

						|| !"-".repeat(60).equals(bufferedReader.readLine())
						|| !Strings.EMPTY.equals(bufferedReader.readLine())) {
					throw new BuildResolveException("Unexpected Gradle \":dependencies\" header format");
				}
				while (null != (line = bufferedReader.readLine())) {
					if ("A web-based, searchable dependency report is available by adding the --scan option."
							.equals(line)) {
						break;
					}
					final var artifactTypeString = Strings.substringBefore(line, " - ");
					final var optionalArtifactType = Arrays.stream(GradleArtifactType.values())
							.filter(artifactType -> artifactType.dependenciesTaskName().equals(artifactTypeString))
							.findAny();
					if (optionalArtifactType.isEmpty()) {
						while (null != (line = bufferedReader.readLine())) {
							if (line.isEmpty()) {
								break;
							}
						}
					} else {
						final var artifactType = optionalArtifactType.orElseThrow();
						while (null != (line = bufferedReader.readLine())) {
							if (line.isEmpty()) {
								break;
							}
							if ("No dependencies".equals(line)
									|| line.startsWith("|    ")
									|| line.startsWith("     ")) {
								continue;
							}
							if (!line.startsWith("+--- ") && !line.startsWith("\\--- ")) {
								throw new BuildResolveException("Unexpected Gradle \":dependencies\" artifact format");
							}
							var artifactString = Strings.substringBefore(
									Strings.substringBefore(
											Strings.substringAfter(
													line,
													"--- "
											),
											" ("
									),
									" FAILED"
							);
							var frequency = Strings.frequency(artifactString, " -> ");
							if (1 < frequency) {
								throw new BuildResolveException("Unexpected Gradle \":dependencies\" artifact format");
							}
							var parts = Strings.split(" -> ", artifactString);
							artifactString = parts.get(0);
							final var resolvedVersion = 2 == parts.size()
									? parts.get(1)
									: null;
							frequency = Strings.frequency(artifactString, ':');
							if (1 > frequency || 2 < frequency) {
								throw new BuildResolveException("Unexpected Gradle \":dependencies\" artifact format");
							}
							parts = Strings.split(':', artifactString);
							final var groupId = parts.get(0);
							final var artifactId = parts.get(1);
							final var version = 3 == parts.size()
									? parts.get(2)
									: resolvedVersion;
							artifacts.add(
									new Artifact<>(
											artifactType,
											new ArtifactIdentifier(groupId, artifactId),
											version
									)
							);
						}
					}
				}
				break;
			}
		}
		return artifacts;
	}

	/**
	 * <p>Filter a modifiable {@link List} of repositories, removing irrelevant ones.</p>
	 * @param repositories a modifiable {@link List} of repositories
	 * @return the {@link List} of repositories
	 * @throws NullPointerException if the {@link List} of repositories or any of them is {@code null}
	 * @since 1.2.0
	 */
	static List<Repository> filterRepositories(final List<Repository> repositories) {
		Ensure.notNullAndNotNullElements("repositories", repositories);
		return repositories.stream()
				.filter(repository -> !repository.getUrl().startsWith("file:"))
				.collect(Collectors.toUnmodifiableList());
	}

	/**
	 * <p>Filter a modifiable {@link List} of artifacts, removing redundant duplicates.</p>
	 * @param artifacts a modifiable {@link List} of artifacts
	 * @return the {@link List} of artifacts
	 * @throws NullPointerException if the {@link List} of artifacts or any of them is {@code null}
	 * @deprecated since 1.2.0, use {@link #filterArtifacts(List)} instead
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.2.0")
	static List<Artifact<GradleArtifactType>> filter(final List<Artifact<GradleArtifactType>> artifacts) {
		return filterArtifacts(artifacts);
	}

	/**
	 * <p>Filter a modifiable {@link List} of artifacts, removing irrelevant ones.</p>
	 * @param artifacts a modifiable {@link List} of artifacts
	 * @return the {@link List} of artifacts
	 * @throws NullPointerException if the {@link List} of artifacts or any of them is {@code null}
	 * @since 1.2.0
	 */
	static List<Artifact<GradleArtifactType>> filterArtifacts(final List<Artifact<GradleArtifactType>> artifacts) {
		Ensure.notNullAndNotNullElements("artifacts", artifacts);
		artifacts.stream()
				.filter(artifact -> {
					final var artifactType = artifact.getType();
					return !artifactType.isClasspath();
				})
				.collect(Collectors.toUnmodifiableList())
				.forEach(artifact -> {
					artifacts.remove(artifact.withType(GradleArtifactType.COMPILE_CLASSPATH));
					artifacts.remove(artifact.withType(GradleArtifactType.RUNTIME_CLASSPATH));
					artifacts.remove(artifact.withType(GradleArtifactType.TEST_COMPILE_CLASSPATH));
					artifacts.remove(artifact.withType(GradleArtifactType.TEST_RUNTIME_CLASSPATH));
				});
		artifacts.stream()
				.filter(artifact -> GradleArtifactType.COMPILE_CLASSPATH == artifact.getType())
				.collect(Collectors.toUnmodifiableList())
				.forEach(artifact -> {
					artifacts.remove(artifact.withType(GradleArtifactType.RUNTIME_CLASSPATH));
					artifacts.remove(artifact.withType(GradleArtifactType.TEST_COMPILE_CLASSPATH));
					artifacts.remove(artifact.withType(GradleArtifactType.TEST_RUNTIME_CLASSPATH));
				});
		artifacts.stream()
				.filter(artifact -> GradleArtifactType.RUNTIME_CLASSPATH == artifact.getType())
				.collect(Collectors.toUnmodifiableList())
				.forEach(artifact -> {
					artifacts.remove(artifact.withType(GradleArtifactType.TEST_COMPILE_CLASSPATH));
					artifacts.remove(artifact.withType(GradleArtifactType.TEST_RUNTIME_CLASSPATH));
				});
		artifacts.stream()
				.filter(artifact -> GradleArtifactType.TEST_COMPILE_CLASSPATH == artifact.getType())
				.collect(Collectors.toUnmodifiableList())
				.forEach(artifact -> artifacts.remove(artifact.withType(GradleArtifactType.TEST_RUNTIME_CLASSPATH)));
		return artifacts.stream()
				.map(artifact -> {
					switch (artifact.getType()) {
						case COMPILE_CLASSPATH:
							return artifact.withType(GradleArtifactType.IMPLEMENTATION);
						case RUNTIME_CLASSPATH:
							return artifact.withType(GradleArtifactType.RUNTIME_ONLY);
						case TEST_COMPILE_CLASSPATH:
							return artifact.withType(GradleArtifactType.TEST_IMPLEMENTATION);
						case TEST_RUNTIME_CLASSPATH:
							return artifact.withType(GradleArtifactType.TEST_RUNTIME_ONLY);
						default:
							return artifact;
					}
				})
				.collect(Collectors.toUnmodifiableList());
	}
}