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
package com.github.alexisjehan.mavencheck;

import com.github.alexisjehan.javanilla.lang.Throwables;
import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import com.github.alexisjehan.mavencheck.core.Service;
import com.github.alexisjehan.mavencheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mavencheck.core.component.artifact.type.ArtifactType;
import com.github.alexisjehan.mavencheck.core.component.build.Build;
import com.github.alexisjehan.mavencheck.core.component.build.file.BuildFileType;
import com.github.alexisjehan.mavencheck.core.component.build.resolver.BuildResolveException;
import com.github.alexisjehan.mavencheck.core.component.session.MavenSession;
import com.github.alexisjehan.mavencheck.core.util.GradleUtils;
import com.github.alexisjehan.mavencheck.core.util.MavenUtils;
import internal.ExcludeFromJacocoGeneratedReport;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;

/**
 * <p>Class that describes the application.</p>
 * @since 1.0.0
 */
public final class Application {

	/**
	 * <p>Name.</p>
	 * @since 1.0.0
	 */
	private static final String NAME = MethodHandles.lookup().lookupClass().getPackage().getImplementationTitle();

	/**
	 * <p>Version.</p>
	 * @since 1.0.0
	 */
	private static final String VERSION = MethodHandles.lookup().lookupClass().getPackage().getImplementationVersion()
			+ " (built with Maven " + MavenUtils.getVersion() + " and Gradle " + GradleUtils.getVersion() + ")";

	/**
	 * <p>Description.</p>
	 * @since 1.0.0
	 */
	private static final String DESCRIPTION = "Check for artifact updates of every "
			+ ToString.toString(BuildFileType.MAVEN.getFileName()) + ", "
			+ ToString.toString(BuildFileType.GRADLE_GROOVY.getFileName()) + " and "
			+ ToString.toString(BuildFileType.GRADLE_KOTLIN.getFileName()) + " build files in the given or current "
			+ "path recursively.";

	/**
	 * <p>Command name.</p>
	 * @since 1.0.0
	 */
	private static final String COMMAND_NAME = "mvnchk";

	/**
	 * <p>Help option long name.</p>
	 * @since 1.0.0
	 */
	private static final String OPTION_HELP = "help";

	/**
	 * <p>Ignore snapshots option long name.</p>
	 * @since 1.0.0
	 */
	private static final String OPTION_IGNORE_SNAPSHOTS = "ignore-snapshots";

	/**
	 * <p>Short option long name.</p>
	 * @since 1.0.0
	 */
	private static final String OPTION_SHORT = "short";

	/**
	 * <p>Version option long name.</p>
	 * @since 1.0.0
	 */
	private static final String OPTION_VERSION = "version";

	/**
	 * <p>Default value depending on whether ANSI should be enabled or not.</p>
	 * @since 1.0.0
	 */
	private static final boolean DEFAULT_ANSI = false;

	/**
	 * <p>Default path, current directory.</p>
	 * @since 1.0.0
	 */
	private static final Path DEFAULT_PATH = Path.of(".");

	/**
	 * <p>Options.</p>
	 * @since 1.0.0
	 */
	private static final Options options = new Options();

	static {
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
	 * <p>Standard output {@link PrintStream}.</p>
	 * @since 1.0.0
	 */
	private final PrintStream outputStream;

	/**
	 * <p>Error output {@link PrintStream}.</p>
	 * @since 1.0.0
	 */
	private final PrintStream errorStream;

	/**
	 * <p>Constructor with a single {@link PrintStream} for standard and error outputs.</p>
	 * @param printStream a {@link PrintStream}
	 * @throws NullPointerException if the {@link PrintStream} is {@code null}
	 * @since 1.0.0
	 */
	Application(final PrintStream printStream) {
		this(printStream, printStream, DEFAULT_ANSI);
	}

	/**
	 * <p>Constructor with distinct {@link PrintStream}s for standard and error outputs.</p>
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
	 * <p>Run the command-line interface.</p>
	 * @param args an array of arguments
	 * @throws NullPointerException if the array of arguments or any of them is {@code null}
	 * @since 1.0.0
	 */
	void run(final String... args) {
		Ensure.notNullAndNotNullElements("args", args);
		try {
			final var commandLine = new DefaultParser().parse(options, args, false);
			if (commandLine.hasOption(OPTION_VERSION) || commandLine.hasOption(OPTION_HELP)) {
				outputStream.println(NAME + " " + VERSION);
				if (commandLine.hasOption(OPTION_HELP)) {
					outputStream.println();
					outputStream.println(DESCRIPTION);
					outputStream.println();
					try (final var writer = new PrintWriter(outputStream)) {
						final var helpFormatter = new HelpFormatter();
						helpFormatter.printHelp(
								writer,
								helpFormatter.getWidth(),
								COMMAND_NAME + " [<path>]",
								null,
								options,
								helpFormatter.getLeftPadding(),
								helpFormatter.getDescPadding(),
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
						commandLine.hasOption(OPTION_IGNORE_SNAPSHOTS),
						commandLine.hasOption(OPTION_SHORT)
				);
			}
		} catch (final Exception e) {
			errorStream.println(Ansi.ansi().fgBrightRed().a(toString(e)).reset());
		}
	}

	/**
	 * <p>Run the program.</p>
	 * @param path a path
	 * @param ignoreSnapshots {@code true} if build file artifacts with a snapshot version should be ignored
	 * @param short0 {@code true} if only build files with at least one artifact update should be shown
	 * @throws IOException might occur with input/output operations
	 * @throws NullPointerException if the path is {@code null}
	 * @since 1.0.0
	 */
	void run(final Path path, final boolean ignoreSnapshots, final boolean short0) throws IOException {
		Ensure.notNull("path", path);
		final var service = createService();
		final var buildFiles = service.findBuildFiles(path);
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
			final Build build;
			try {
				build = service.findBuild(buildFile);
			} catch (final BuildResolveException e) {
				outputStream.println(Ansi.ansi().fgBrightRed().a(toString(file)).reset());
				outputStream.println(Ansi.ansi().fgBrightRed().a(toString(e)).reset());
				outputStream.println();
				continue;
			}
			final var artifactUpdateVersions = service.findArtifactUpdateVersions(build, ignoreSnapshots);
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
									.fgBrightBlue().a(toString(artifact.getType())).reset()
									.a("] ")
									.a(toString(artifact.getIdentifier()))
									.a(" ")
									.fgBrightYellow().a(artifact.getOptionalVersion().orElseThrow()).reset()
									.a(" -> ")
									.fgBrightGreen().a(updateVersion).reset()
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
	}

	/**
	 * <p>Create a service.</p>
	 * @return the service
	 * @throws IOException might occur with input/output operations
	 * @since 1.0.0
	 */
	static Service createService() throws IOException {
		return new Service(new MavenSession());
	}

	/**
	 * <p>Convert an {@link Exception} to a {@link String}.</p>
	 * @param exception an {@link Exception}
	 * @return the {@link String}
	 * @throws NullPointerException if the {@link Exception} is {@code null}
	 * @since 1.0.0
	 */
	static String toString(final Exception exception) {
		Ensure.notNull("exception", exception);
		return Throwables.getOptionalRootCause(exception).orElse(exception).getMessage().trim();
	}

	/**
	 * <p>Convert a file to a {@link String}.</p>
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
	 * <p>Convert an artifact type to a {@link String}.</p>
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
	 * <p>Convert an artifact identifier to a {@link String}.</p>
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
	 * <p>Main entrance.</p>
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