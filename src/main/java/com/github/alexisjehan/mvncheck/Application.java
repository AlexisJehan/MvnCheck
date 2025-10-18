/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Alexis Jehan
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
package com.github.alexisjehan.mvncheck;

import com.github.alexisjehan.javanilla.lang.Strings;
import com.github.alexisjehan.javanilla.lang.Throwables;
import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import com.github.alexisjehan.mvncheck.core.Service;
import com.github.alexisjehan.mvncheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mvncheck.core.component.artifact.type.ArtifactType;
import com.github.alexisjehan.mvncheck.core.component.artifact.version.ArtifactUpdateVersion;
import com.github.alexisjehan.mvncheck.core.component.artifact.version.resolver.ArtifactAvailableVersionsResolveException;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFileType;
import com.github.alexisjehan.mvncheck.core.component.build.resolver.BuildResolveException;
import com.github.alexisjehan.mvncheck.core.component.filter.artifact.WildcardArtifactFilter;
import com.github.alexisjehan.mvncheck.core.component.session.MavenSession;
import com.github.alexisjehan.mvncheck.core.util.GithubUtils;
import com.github.alexisjehan.mvncheck.core.util.GradleUtils;
import com.github.alexisjehan.mvncheck.core.util.MavenUtils;
import internal.ExcludeFromJacocoGeneratedReport;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.help.HelpFormatter;
import org.apache.commons.cli.help.TextHelpAppendable;
import org.jline.jansi.Ansi;
import org.jline.jansi.AnsiConsole;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Class that describes the application.
 * @since 1.0.0
 */
public final class Application {

	/**
	 * Maximum depth option long name.
	 * @since 1.1.0
	 */
	static final String OPTION_MAX_DEPTH = "max-depth";

	/**
	 * Filter option long name.
	 * @since 1.7.0
	 */
	static final String OPTION_FILTER = "filter";

	/**
	 * Help option long name.
	 * @since 1.0.0
	 */
	static final String OPTION_HELP = "help";

	/**
	 * Ignore snapshots option long name.
	 * @since 1.0.0
	 */
	static final String OPTION_IGNORE_SNAPSHOTS = "ignore-snapshots";

	/**
	 * Ignore inherited option long name.
	 * @since 1.5.0
	 */
	static final String OPTION_IGNORE_INHERITED = "ignore-inherited";

	/**
	 * Include output option long name.
	 * @since 1.7.0
	 */
	static final String OPTION_INCLUDE_OUTPUT = "include-output";

	/**
	 * Short option long name.
	 * @since 1.0.0
	 */
	static final String OPTION_SHORT = "short";

	/**
	 * Version option long name.
	 * @since 1.0.0
	 */
	static final String OPTION_VERSION = "version";

	/**
	 * Title.
	 * @since 1.4.0
	 */
	private static final String TITLE = Constants.NAME + " " + Constants.VERSION
			+ " (built with Maven " + MavenUtils.VERSION + " and Gradle " + GradleUtils.VERSION + ")";

	/**
	 * Description.
	 * @since 1.0.0
	 */
	private static final String DESCRIPTION = "Check for artifact updates of every "
			+ ToString.toString(BuildFileType.MAVEN.getFileName()) + ", "
			+ ToString.toString(BuildFileType.GRADLE_GROOVY.getFileName()) + " and "
			+ ToString.toString(BuildFileType.GRADLE_KOTLIN.getFileName()) + " build files in the given or current "
			+ "path recursively.";

	/**
	 * Command name.
	 * @since 1.0.0
	 */
	private static final String COMMAND_NAME = "mvnchk";

	/**
	 * Default value depending on whether ANSI should be enabled.
	 * @since 1.0.0
	 */
	private static final boolean DEFAULT_ANSI = false;

	/**
	 * Default path.
	 * @since 1.0.0
	 */
	private static final Path DEFAULT_PATH = Path.of(".");

	/**
	 * Default maximum depth.
	 * @since 1.1.0
	 */
	private static final int DEFAULT_MAX_DEPTH = Integer.MAX_VALUE;

	/**
	 * Options.
	 * @since 1.0.0
	 */
	private static final Options options = new Options();

	static {
		options.addOption(
				"d",
				OPTION_MAX_DEPTH,
				true,
				"Maximum depth of subdirectories to find build files (a non-negative integer)"
		);
		options.addOption(
				"f",
				OPTION_FILTER,
				true,
				"Filter build file artifacts with a \"groupId[:artifactId[:updateVersion]]\" expression ("
						+ ToString.toString(WildcardArtifactFilter.WILDCARD_SINGLE) + " and "
						+ ToString.toString(WildcardArtifactFilter.WILDCARD_ANY) + " wildcards are allowed)"
		);
		options.addOption(
				"h",
				OPTION_HELP,
				false,
				"Display help information"
		);
		options.addOption(
				"i",
				OPTION_IGNORE_SNAPSHOTS,
				false,
				"Ignore build file artifacts with a snapshot version"
		);
		options.addOption(
				null,
				OPTION_IGNORE_INHERITED,
				false,
				"Ignore build file artifacts with an inherited version"
		);
		options.addOption(
				"o",
				OPTION_INCLUDE_OUTPUT,
				false,
				"Include build files inside output directories"
		);
		options.addOption(
				"s",
				OPTION_SHORT,
				false,
				"Only show build files with at least one artifact update"
		);
		options.addOption(
				"v",
				OPTION_VERSION,
				false,
				"Display version information"
		);
	}

	/**
	 * Standard output {@link PrintStream}.
	 * @since 1.0.0
	 */
	private final PrintStream outputStream;

	/**
	 * Error output {@link PrintStream}.
	 * @since 1.0.0
	 */
	private final PrintStream errorStream;

	/**
	 * Constructor with a single {@link PrintStream} for standard and error outputs.
	 * @param printStream a {@link PrintStream}
	 * @throws NullPointerException if the {@link PrintStream} is {@code null}
	 * @since 1.0.0
	 */
	Application(final PrintStream printStream) {
		this(printStream, printStream, DEFAULT_ANSI);
	}

	/**
	 * Constructor with distinct {@link PrintStream}s for standard and error outputs.
	 * @param outputStream a standard output {@link PrintStream}
	 * @param errorStream an error output {@link PrintStream}
	 * @param ansi {@code true} if ANSI should be enabled
	 * @throws NullPointerException if the standard output {@link PrintStream} or the error output {@link PrintStream}
	 *         is {@code null}
	 * @since 1.0.0
	 */
	Application(final PrintStream outputStream, final PrintStream errorStream, final boolean ansi) {
		Ensure.notNull("outputStream", outputStream);
		Ensure.notNull("errorStream", errorStream);
		this.outputStream = outputStream;
		this.errorStream = errorStream;
		Ansi.setEnabled(ansi);
	}

	/**
	 * Run the command-line interface.
	 * @param args an array of arguments
	 * @throws NullPointerException if the array of arguments or any of them is {@code null}
	 * @since 1.0.0
	 */
	void run(final String... args) {
		Ensure.notNullAndNotNullElements("args", args);
		try {
			final var commandLine = new DefaultParser().parse(options, args, false);
			if (commandLine.hasOption(OPTION_VERSION) || commandLine.hasOption(OPTION_HELP)) {
				outputStream.println(TITLE);
				if (commandLine.hasOption(OPTION_HELP)) {
					outputStream.println();
					outputStream.println(DESCRIPTION);
					outputStream.println();
					try (var writer = new PrintWriter(outputStream)) {
						final var helpFormatter = HelpFormatter.builder()
								.setHelpAppendable(new TextHelpAppendable(writer))
								.setShowSince(false)
								.get();
						helpFormatter.printHelp(
								COMMAND_NAME + " [<glob>...]",
								null,
								options,
								null,
								true
						);
					}
				}
			} else {
				final var arguments = commandLine.getArgs();
				run(
						1 == arguments.length
								? Path.of(arguments[0])
								: DEFAULT_PATH,
						commandLine.hasOption(OPTION_MAX_DEPTH)
								? Integer.parseUnsignedInt(commandLine.getOptionValue(OPTION_MAX_DEPTH))
								: DEFAULT_MAX_DEPTH,
						commandLine.hasOption(OPTION_FILTER)
								? Set.of(commandLine.getOptionValues(OPTION_FILTER))
								: Set.of(),
						commandLine.hasOption(OPTION_IGNORE_SNAPSHOTS),
						commandLine.hasOption(OPTION_IGNORE_INHERITED),
						commandLine.hasOption(OPTION_INCLUDE_OUTPUT),
						commandLine.hasOption(OPTION_SHORT)
				);
			}
		} catch (final Exception e) {
			errorStream.println(Ansi.ansi().fgBrightRed().a(toString(e)).reset());
		}
	}

	/**
	 * Run the program.
	 * @param path a path
	 * @param ignoreSnapshots {@code true} if build file artifacts with a snapshot version should be ignored
	 * @param short0 {@code true} if only build files with at least one artifact update should be shown
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the path is {@code null}
	 * @deprecated since 1.1.0, use {@link #run(Path, int, boolean, boolean)} instead
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.1.0")
	void run(final Path path, final boolean ignoreSnapshots, final boolean short0) throws IOException {
		run(path, DEFAULT_MAX_DEPTH, ignoreSnapshots, short0);
	}

	/**
	 * Run the program.
	 * @param path a path
	 * @param maxDepth a maximum depth
	 * @param ignoreSnapshots {@code true} if build file artifacts with a snapshot version should be ignored
	 * @param short0 {@code true} if only build files with at least one artifact update should be shown
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the path is {@code null}
	 * @throws IllegalArgumentException if the maximum depth is lower than {@code 0}
	 * @deprecated since 1.5.0, use {@link #run(Path, int, boolean, boolean, boolean)} instead
	 * @since 1.1.0
	 */
	@Deprecated(since = "1.5.0")
	void run(
			final Path path,
			final int maxDepth,
			final boolean ignoreSnapshots,
			final boolean short0
	) throws IOException {
		run(path, maxDepth, false, ignoreSnapshots, short0);
	}

	/**
	 * Run the program.
	 * @param path a path
	 * @param maxDepth a maximum depth
	 * @param ignoreInherited {@code true} if build file artifacts with an inherited version should be ignored
	 * @param ignoreSnapshots {@code true} if build file artifacts with a snapshot version should be ignored
	 * @param short0 {@code true} if only build files with at least one artifact update should be shown
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the path is {@code null}
	 * @throws IllegalArgumentException if the maximum depth is lower than {@code 0}
	 * @deprecated since 1.7.0, use {@link #run(Path, int, Set, boolean, boolean, boolean, boolean)} instead
	 * @since 1.5.0
	 */
	@Deprecated(since = "1.7.0")
	void run(
			final Path path,
			final int maxDepth,
			final boolean ignoreInherited,
			final boolean ignoreSnapshots,
			final boolean short0
	) throws IOException {
		run(path, maxDepth, Set.of(), ignoreSnapshots, ignoreInherited, true, short0);
	}

	/**
	 * Run the program.
	 * @param path a path
	 * @param maxDepth a maximum depth
	 * @param filters a {@link Set} of filters
	 * @param ignoreSnapshots {@code true} if build file artifacts with a snapshot version should be ignored
	 * @param ignoreInherited {@code true} if build file artifacts with an inherited version should be ignored
	 * @param includeOutput {@code true} if build files inside output directories should be included
	 * @param short0 {@code true} if only build files with at least one artifact update should be shown
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the path, the {@link Set} of filters or any of them is {@code null}
	 * @throws IllegalArgumentException if the maximum depth is lower than {@code 0}
	 * @since 1.7.0
	 */
	void run(
			final Path path,
			final int maxDepth,
			final Set<String> filters,
			final boolean ignoreSnapshots,
			final boolean ignoreInherited,
			final boolean includeOutput,
			final boolean short0
	) throws IOException {
		Ensure.notNull("path", path);
		Ensure.greaterThanOrEqualTo("maxDepth", maxDepth, 0);
		Ensure.notNullAndNotNullElements("filters", filters);
		final var service = createService();
		var buildFiles = service.findBuildFiles(path, maxDepth);
		if (!includeOutput) {
			buildFiles = service.filterBuildFiles(buildFiles);
		}
		if (buildFiles.isEmpty()) {
			outputStream.println("No build file found");
			return;
		}
		outputStream.println(buildFiles.size() + " build file(s) found, checking for artifact updates");
		outputStream.println();
		var buildsCount = 0;
		var artifactsUpdatesCount = 0;
		for (final var buildFile : buildFiles) {
			final var file = buildFile.getFile();
			final List<ArtifactUpdateVersion> artifactUpdateVersions;
			try {
				final var build = service.findBuild(buildFile);
				artifactUpdateVersions = service.findArtifactUpdateVersions(
						build,
						filters,
						ignoreSnapshots,
						ignoreInherited
				);
			} catch (final BuildResolveException | ArtifactAvailableVersionsResolveException e) {
				outputStream.println(Ansi.ansi().fgBrightRed().a(toString(file)).reset());
				outputStream.println(Ansi.ansi().fgBrightRed().a(toString(e)).reset());
				outputStream.println();
				continue;
			}
			if (artifactUpdateVersions.isEmpty()) {
				if (!short0) {
					outputStream.println(Ansi.ansi().fgBrightGreen().a(toString(file)).reset());
					outputStream.println("No artifact update available");
					outputStream.println();
				}
			} else {
				outputStream.println(Ansi.ansi().fgBrightYellow().a(toString(file)).reset());
				for (final var artifactUpdateVersion : artifactUpdateVersions) {
					final var artifact = artifactUpdateVersion.getArtifact();
					final var updateVersion = artifactUpdateVersion.getUpdateVersion();
					outputStream.println(
							Ansi.ansi()
									.a("[")
									.fgBrightBlue()
									.a(toString(artifact.getType()))
									.reset()
									.a("] ")
									.a(artifact.isVersionInherited() ? "(" : Strings.EMPTY)
									.a(toString(artifact.getIdentifier()))
									.a(" ")
									.fgBrightYellow()
									.a(artifact.getOptionalVersion().orElseThrow())
									.reset()
									.a(" -> ")
									.fgBrightGreen()
									.a(updateVersion)
									.reset()
									.a(artifact.isVersionInherited() ? ")" : Strings.EMPTY)
					);
					++artifactsUpdatesCount;
				}
				outputStream.println(artifactUpdateVersions.size() + " artifact update(s) available");
				outputStream.println();
			}
			++buildsCount;
		}
		outputStream.println(
				buildsCount + "/" + buildFiles.size() + " build file(s) checked, " + (
						0 == artifactsUpdatesCount
								? "no artifact update available"
								: artifactsUpdatesCount + " artifact update(s) available"
				)
		);
		final var currentVersion = getCurrentVersion();
		if (null != currentVersion) {
			GithubUtils.retrieveOptionalLatestReleaseName(Constants.GITHUB_OWNER_NAME, Constants.GITHUB_REPOSITORY_NAME)
					.filter(latestVersion -> 0 > currentVersion.compareTo(latestVersion))
					.ifPresent(
							latestVersion -> outputStream.println(
									Ansi.ansi()
											.fgBrightYellow()
											.a("(A newer version is available to download on GitHub)")
											.reset()
							)
					);
		}
	}

	/**
	 * Create a service.
	 * @return the service
	 * @throws IOException might occur with input/output operations
	 * @since 1.0.0
	 */
	static Service createService() throws IOException {
		return new Service(new MavenSession());
	}

	/**
	 * Convert an {@link Exception} to a {@link String}.
	 * @param exception an {@link Exception}
	 * @return the {@link String}
	 * @throws NullPointerException if the {@link Exception} is {@code null}
	 * @since 1.0.0
	 */
	static String toString(final Exception exception) {
		Ensure.notNull("exception", exception);
		return Throwables.getOptionalRootCause(exception).orElse(exception).toString().stripTrailing();
	}

	/**
	 * Convert a file to a {@link String}.
	 * @param file a file
	 * @return the {@link String}
	 * @throws NullPointerException if the file is {@code null}
	 * @since 1.0.0
	 */
	static String toString(final Path file) {
		Ensure.notNull("file", file);
		return file.normalize().toString();
	}

	/**
	 * Convert an artifact type to a {@link String}.
	 * @param artifactType an artifact type
	 * @return the {@link String}
	 * @throws NullPointerException if the artifact type is {@code null}
	 * @since 1.0.0
	 */
	static String toString(final ArtifactType artifactType) {
		Ensure.notNull("artifactType", artifactType);
		return artifactType.toString().replace('_', ' ');
	}

	/**
	 * Convert an artifact identifier to a {@link String}.
	 * @param artifactIdentifier an artifact identifier
	 * @return the {@link String}
	 * @throws NullPointerException if the artifact identifier is {@code null}
	 * @since 1.0.0
	 */
	static String toString(final ArtifactIdentifier artifactIdentifier) {
		Ensure.notNull("artifactIdentifier", artifactIdentifier);
		return artifactIdentifier.getGroupId() + ":" + artifactIdentifier.getArtifactId();
	}

	/**
	 * Get the current version.
	 * @return the current version
	 * @since 1.4.0
	 */
	static String getCurrentVersion() {
		return Constants.VERSION;
	}

	/**
	 * Main entrance.
	 * @param args an array of arguments
	 * @since 1.0.0
	 */
	@ExcludeFromJacocoGeneratedReport
	public static void main(final String... args) {
		AnsiConsole.systemInstall();
		new Application(AnsiConsole.out(), AnsiConsole.err(), true).run(args);
		AnsiConsole.systemUninstall();
	}
}