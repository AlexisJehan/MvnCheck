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
package com.github.alexisjehan.mvncheck.core;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.util.Comparators;
import com.github.alexisjehan.mvncheck.core.component.artifact.version.ArtifactUpdateVersion;
import com.github.alexisjehan.mvncheck.core.component.artifact.version.resolver.ArtifactAvailableVersionsResolver;
import com.github.alexisjehan.mvncheck.core.component.artifact.version.resolver.MavenArtifactAvailableVersionsResolver;
import com.github.alexisjehan.mvncheck.core.component.build.Build;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFile;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFileType;
import com.github.alexisjehan.mvncheck.core.component.build.resolver.BuildResolver;
import com.github.alexisjehan.mvncheck.core.component.build.resolver.GradleBuildResolver;
import com.github.alexisjehan.mvncheck.core.component.build.resolver.MavenBuildResolver;
import com.github.alexisjehan.mvncheck.core.component.filter.artifact.ArtifactFilter;
import com.github.alexisjehan.mvncheck.core.component.filter.artifact.CompositeArtifactFilter;
import com.github.alexisjehan.mvncheck.core.component.filter.artifact.parser.ArtifactFilterParser;
import com.github.alexisjehan.mvncheck.core.component.filter.version.VersionFilter;
import com.github.alexisjehan.mvncheck.core.component.filter.version.factory.CompositeVersionFilterFactory;
import com.github.alexisjehan.mvncheck.core.component.filter.version.factory.QualifierVersionFilterFactory;
import com.github.alexisjehan.mvncheck.core.component.filter.version.factory.ReleaseVersionFilterFactory;
import com.github.alexisjehan.mvncheck.core.component.filter.version.factory.VersionFilterFactory;
import com.github.alexisjehan.mvncheck.core.component.session.MavenSession;
import com.github.alexisjehan.mvncheck.core.util.SystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Class that describes the service.</p>
 * @since 1.0.0
 */
public final class Service {

	/**
	 * <p>Ignore file name.</p>
	 * @since 1.0.0
	 */
	private static final String IGNORE_FILE_NAME = ".mvnchk-ignore";

	/**
	 * <p>{@link Set} of build resolvers.</p>
	 * @since 1.0.0
	 */
	private final Set<BuildResolver> buildResolvers;

	/**
	 * <p>Artifact available versions resolver.</p>
	 * @since 1.0.0
	 */
	private final ArtifactAvailableVersionsResolver artifactAvailableVersionsResolver;

	/**
	 * <p>Version filter factory.</p>
	 * @since 1.0.0
	 */
	private final VersionFilterFactory versionFilterFactory = new CompositeVersionFilterFactory(
			new QualifierVersionFilterFactory(),
			new ReleaseVersionFilterFactory()
	);

	/**
	 * <p>User artifact filter.</p>
	 * @since 1.0.0
	 */
	private final ArtifactFilter userArtifactFilter;

	/**
	 * <p>Constructor with a <i>Maven</i> session.</p>
	 * @param mavenSession a <i>Maven</i> session
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the <i>Maven</i> session is {@code null}
	 * @since 1.0.0
	 */
	public Service(final MavenSession mavenSession) throws IOException {
		this(
				Set.of(
						new MavenBuildResolver(Ensure.notNull("mavenSession", mavenSession)),
						new GradleBuildResolver()
				),
				new MavenArtifactAvailableVersionsResolver(mavenSession)
		);
	}

	/**
	 * <p>Constructor with a {@link Set} of build resolvers and an artifact available versions resolver.</p>
	 * @param buildResolvers a {@link Set} of build resolvers
	 * @param artifactAvailableVersionsResolver an artifact available versions resolver
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the {@link Set} of build resolvers, any of them or the artifact available
	 *         versions resolver is {@code null}
	 * @since 1.0.0
	 */
	Service(
			final Set<BuildResolver> buildResolvers,
			final ArtifactAvailableVersionsResolver artifactAvailableVersionsResolver
	) throws IOException {
		Ensure.notNullAndNotNullElements("buildResolvers", buildResolvers);
		Ensure.notNull("artifactAvailableVersionsResolver", artifactAvailableVersionsResolver);
		this.buildResolvers = Set.copyOf(buildResolvers);
		this.artifactAvailableVersionsResolver = artifactAvailableVersionsResolver;
		userArtifactFilter = createUserArtifactFilter();
	}

	/**
	 * <p>Find a {@link List} of build files in the given path, recursively.</p>
	 * @param path a path
	 * @return the {@link List} of build files
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the path is {@code null}
	 * @throws IllegalArgumentException if the path does not exist
	 * @deprecated since 1.1.0, use {@link #findBuildFiles(Path, int)} instead
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.1.0")
	public List<BuildFile> findBuildFiles(final Path path) throws IOException {
		return findBuildFiles(path, Integer.MAX_VALUE);
	}

	/**
	 * <p>Find a {@link List} of build files in the given path, recursively.</p>
	 * @param path a path
	 * @param maxDepth a maximum depth
	 * @return the {@link List} of build files
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the path is {@code null}
	 * @throws IllegalArgumentException if the path does not exist or if the maximum depth is lower than {@code 0}
	 * @since 1.1.0
	 */
	public List<BuildFile> findBuildFiles(final Path path, final int maxDepth) throws IOException {
		Ensure.notNullAndExists("path", path);
		Ensure.greaterThanOrEqualTo("maxDepth", maxDepth, 0);
		try (var stream = Files.walk(path, Integer.MAX_VALUE > maxDepth ? maxDepth + 1 : Integer.MAX_VALUE)) {
			return stream
					.filter(Files::isRegularFile)
					.map(file -> {
						final var fileName = file.getFileName().toString();
						return BuildFileType.optionalValueOf(fileName).map(type -> new BuildFile(type, file));
					})
					.flatMap(Optional::stream)
					.sorted(
							Comparator
									.<BuildFile, String>comparing(
											buildFile -> buildFile.getFile().getParent().toString(),
											Comparators.NUMBER_AWARE
									)
									.thenComparing(
											buildFile -> buildFile.getFile().getFileName().toString(),
											Comparators.NUMBER_AWARE
									)
					)
					.collect(Collectors.toUnmodifiableList());
		}
	}

	/**
	 * <p>Find the build for the given build file.</p>
	 * @param buildFile a build file
	 * @return the build
	 * @throws NullPointerException if the build file is {@code null}
	 * @since 1.0.0
	 */
	public Build findBuild(final BuildFile buildFile) {
		Ensure.notNull("buildFile", buildFile);
		return buildResolvers.stream()
				.filter(buildResolver -> buildResolver.getFileTypes().contains(buildFile.getType()))
				.findAny()
				.map(buildResolver -> buildResolver.resolve(buildFile))
				.orElseThrow();
	}

	/**
	 * <p>Find a {@link List} of artifact update versions for the given build.</p>
	 * @param build a build
	 * @param ignoreSnapshots {@code true} if build file artifacts with a snapshot version should be ignored
	 * @return the {@link List} of artifact update versions
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the build is {@code null}
	 * @deprecated since 1.5.0, use {@link #findArtifactUpdateVersions(Build, boolean, boolean)} instead
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.5.0")
	public List<ArtifactUpdateVersion> findArtifactUpdateVersions(
			final Build build,
			final boolean ignoreSnapshots
	) throws IOException {
		return findArtifactUpdateVersions(build, false, ignoreSnapshots);
	}

	/**
	 * <p>Find a {@link List} of artifact update versions for the given build.</p>
	 * @param build a build
	 * @param ignoreInherited {@code true} if build file artifacts with an inherited version should be ignored
	 * @param ignoreSnapshots {@code true} if build file artifacts with a snapshot version should be ignored
	 * @return the {@link List} of artifact update versions
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the build is {@code null}
	 * @since 1.5.0
	 */
	public List<ArtifactUpdateVersion> findArtifactUpdateVersions(
			final Build build,
			final boolean ignoreInherited,
			final boolean ignoreSnapshots
	) throws IOException {
		Ensure.notNull("build", build);
		final var artifactFilter = new CompositeArtifactFilter(
				userArtifactFilter,
				createBuildArtifactFilter(build.getFile())
		);
		return build.getArtifacts()
				.parallelStream()
				.filter(artifactFilter::accept)
				.filter(artifact -> !ignoreInherited || !artifact.isVersionInherited())
				.filter(
						artifact -> artifact.getOptionalVersion()
								.filter(version -> !ignoreSnapshots || !VersionFilter.SNAPSHOT.accept(version))
								.isPresent()
				)
				.map(artifact -> artifactAvailableVersionsResolver.resolve(artifact, build.getRepositories()))
				.map(artifactAvailableVersions -> {
					final var artifact = artifactAvailableVersions.getArtifact();
					final var artifactVersion = artifact.getOptionalVersion().orElseThrow();
					final var availableVersions = new ArrayList<>(artifactAvailableVersions.getAvailableVersions());
					if (availableVersions.isEmpty()) {
						return Optional.<ArtifactUpdateVersion>empty();
					}
					Collections.reverse(availableVersions);
					return availableVersions.stream()
							.filter(updateVersion -> artifactFilter.accept(artifact, updateVersion))
							.filter(versionFilterFactory.create(artifactVersion)::accept)
							.findAny()
							.filter(updateVersion -> !artifactVersion.equals(updateVersion))
							.map(updateVersion -> new ArtifactUpdateVersion(artifact, updateVersion));
				})
				.flatMap(Optional::stream)
				.collect(Collectors.toUnmodifiableList());
	}

	/**
	 * <p>Create the user artifact filter.</p>
	 * @return the user artifact filter
	 * @throws IOException might occur with input/output operations
	 * @since 1.0.0
	 */
	static ArtifactFilter createUserArtifactFilter() throws IOException {
		final var userIgnoreFile = SystemUtils.getUserHomeDirectory().resolve(IGNORE_FILE_NAME);
		return Files.isRegularFile(userIgnoreFile)
				? ArtifactFilterParser.parse(userIgnoreFile)
				: ArtifactFilter.NONE;
	}

	/**
	 * <p>Create the build artifact filter for the given build file.</p>
	 * @param buildFile a build file
	 * @return the build artifact filter
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the build file is {@code null}
	 * @since 1.0.0
	 */
	static ArtifactFilter createBuildArtifactFilter(final BuildFile buildFile) throws IOException {
		Ensure.notNull("buildFile", buildFile);
		final var buildIgnoreFile = buildFile.getFile().getParent().resolve(IGNORE_FILE_NAME);
		return Files.isRegularFile(buildIgnoreFile)
				? ArtifactFilterParser.parse(buildIgnoreFile)
				: ArtifactFilter.NONE;
	}
}