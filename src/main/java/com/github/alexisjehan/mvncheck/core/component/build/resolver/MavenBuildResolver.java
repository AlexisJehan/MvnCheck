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
package com.github.alexisjehan.mvncheck.core.component.build.resolver;

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import com.github.alexisjehan.mvncheck.core.component.artifact.Artifact;
import com.github.alexisjehan.mvncheck.core.component.artifact.ArtifactIdentifier;
import com.github.alexisjehan.mvncheck.core.component.artifact.type.MavenArtifactType;
import com.github.alexisjehan.mvncheck.core.component.build.Build;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFile;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFileType;
import com.github.alexisjehan.mvncheck.core.component.repository.Repository;
import com.github.alexisjehan.mvncheck.core.component.repository.RepositoryType;
import com.github.alexisjehan.mvncheck.core.component.session.MavenSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Extension;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.ReportPlugin;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.model.building.PublicDefaultModelProblemCollector;
import org.apache.maven.model.interpolation.DefaultModelVersionProcessor;
import org.apache.maven.model.interpolation.StringVisitorModelInterpolator;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Class that describes a <i>Maven</i> resolver of the build for a file.
 * @since 1.0.0
 */
public final class MavenBuildResolver implements BuildResolver {

	/**
	 * {@link Set} of file types.
	 * @since 1.0.0
	 */
	private static final Set<BuildFileType> FILE_TYPES = Set.of(BuildFileType.MAVEN);

	/**
	 * Logger.
	 * @since 1.0.0
	 */
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * <i>Maven</i> session.
	 * @since 1.0.0
	 */
	private final MavenSession session;

	/**
	 * Constructor.
	 * @param session a <i>Maven</i> session
	 * @throws NullPointerException if the <i>Maven</i> session is {@code null}
	 * @since 1.0.0
	 */
	public MavenBuildResolver(final MavenSession session) {
		Ensure.notNull("session", session);
		this.session = session;
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the file is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public Build resolve(final BuildFile file) {
		Ensure.notNull("file", file);
		logger.info("Resolving the {} build", () -> ToString.toString(file));
		final var request = new DefaultModelBuildingRequest()
				.setPomFile(file.getFile().toFile())
				.setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL)
				.setProcessPlugins(false)
				.setSystemProperties(System.getProperties())
				.setModelResolver(session.getModelResolver())
				.setModelCache(session.getModelCache());
		final ModelBuildingResult result;
		try {
			result = new DefaultModelBuilderFactory()
					.newInstance()
					.build(request);
		} catch (final ModelBuildingException e) {
			throw new BuildResolveException(e);
		}
		final var effectiveModel = result.getEffectiveModel();
		final var modelInterpolator = new StringVisitorModelInterpolator()
				.setVersionPropertiesProcessor(new DefaultModelVersionProcessor());
		final var rawModel = modelInterpolator.interpolateModel(
				result.getRawModel(),
				null,
				request,
				new PublicDefaultModelProblemCollector(result)
		);

		logger.trace("Extracting effective repositories");
		final var effectiveRepositories = extractRepositories(effectiveModel);
		logger.debug("Extracted effective repositories:");
		effectiveRepositories.forEach(
				effectiveRepository -> logger.debug(
						"- {}",
						() -> ToString.toString(effectiveRepository)
				)
		);

		logger.trace("Extracting effective artifacts");
		final var effectiveArtifacts = extractArtifacts(effectiveModel);
		logger.debug("Extracted effective artifacts:");
		effectiveArtifacts.forEach(
				effectiveArtifact -> logger.debug(
						"- {}",
						() -> ToString.toString(effectiveArtifact)
				)
		);

		logger.trace("Extracting raw artifacts");
		final var rawArtifacts = extractArtifacts(rawModel);
		logger.debug("Extracted raw artifacts:");
		rawArtifacts.forEach(
				rawArtifact -> logger.debug(
						"- {}",
						() -> ToString.toString(rawArtifact)
				)
		);

		logger.trace("Inheriting artifacts");
		final var inheritedArtifacts = inherit(rawArtifacts, effectiveArtifacts);
		logger.debug("Inherited artifacts:");
		inheritedArtifacts.forEach(
				inheritedArtifact -> logger.debug(
						"- {}",
						() -> ToString.toString(inheritedArtifact)
				)
		);

		return new Build(file, effectiveRepositories, inheritedArtifacts);
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
	 * Extract a {@link List} of repositories from a <i>Maven</i> model.
	 * @param model a <i>Maven</i> model
	 * @return the {@link List} of repositories
	 * @since 1.0.0
	 */
	private static List<Repository> extractRepositories(final Model model) {
		return Stream.concat(
						model.getRepositories()
								.stream()
								.map(
										repository ->
												toRepository(RepositoryType.NORMAL, repository)
								),
						model.getPluginRepositories()
								.stream()
								.map(
										pluginRepository ->
												toRepository(RepositoryType.PLUGIN, pluginRepository)
								)
				)
				.toList();
	}

	/**
	 * Extract a {@link List} of artifacts from a <i>Maven</i> model.
	 * @param model a <i>Maven</i> model
	 * @return the {@link List} of artifacts
	 * @since 1.0.0
	 */
	private static List<Artifact<MavenArtifactType>> extractArtifacts(final Model model) {
		final var artifacts = new ArrayList<Artifact<MavenArtifactType>>();
		final var parent = model.getParent();
		if (null != parent) {
			artifacts.add(toArtifact(parent));
		}
		final var dependencyManagement = model.getDependencyManagement();
		if (null != dependencyManagement) {
			for (final var dependency : dependencyManagement.getDependencies()) {
				artifacts.add(toArtifact(MavenArtifactType.DEPENDENCY_MANAGEMENT_DEPENDENCY, dependency));
			}
		}
		for (final var dependency : model.getDependencies()) {
			artifacts.add(toArtifact(MavenArtifactType.DEPENDENCY, dependency));
		}
		final var build = model.getBuild();
		if (null != build) {
			for (final var extension : build.getExtensions()) {
				artifacts.add(toArtifact(extension));
			}
			final var buildPluginManagement = build.getPluginManagement();
			if (null != buildPluginManagement) {
				for (final var plugin : buildPluginManagement.getPlugins()) {
					artifacts.add(toArtifact(MavenArtifactType.BUILD_PLUGIN_MANAGEMENT_PLUGIN, plugin));
					for (final var dependency : plugin.getDependencies()) {
						artifacts.add(
								toArtifact(
										MavenArtifactType.BUILD_PLUGIN_MANAGEMENT_PLUGIN_DEPENDENCY,
										dependency
								)
						);
					}
				}
			}
			for (final var plugin : build.getPlugins()) {
				artifacts.add(toArtifact(MavenArtifactType.BUILD_PLUGIN, plugin));
				for (final var dependency : plugin.getDependencies()) {
					artifacts.add(toArtifact(MavenArtifactType.BUILD_PLUGIN_DEPENDENCY, dependency));
				}
			}
		}
		final var reporting = model.getReporting();
		if (null != reporting) {
			for (final var plugin : reporting.getPlugins()) {
				artifacts.add(toArtifact(MavenArtifactType.REPORTING_PLUGIN, plugin));
			}
		}
		for (final var profile : model.getProfiles()) {
			final var profileDependencyManagement = profile.getDependencyManagement();
			if (null != profileDependencyManagement) {
				for (final var dependency : profileDependencyManagement.getDependencies()) {
					artifacts.add(toArtifact(MavenArtifactType.PROFILE_DEPENDENCY_MANAGEMENT_DEPENDENCY, dependency));
				}
			}
			for (final var dependency : profile.getDependencies()) {
				artifacts.add(toArtifact(MavenArtifactType.PROFILE_DEPENDENCY, dependency));
			}
			final var profileBuild = profile.getBuild();
			if (null != profileBuild) {
				final var profileBuildPluginManagement = profileBuild.getPluginManagement();
				if (null != profileBuildPluginManagement) {
					for (final var plugin : profileBuildPluginManagement.getPlugins()) {
						artifacts.add(toArtifact(MavenArtifactType.PROFILE_BUILD_PLUGIN_MANAGEMENT_PLUGIN, plugin));
						for (final var dependency : plugin.getDependencies()) {
							artifacts.add(
									toArtifact(
											MavenArtifactType.PROFILE_BUILD_PLUGIN_MANAGEMENT_PLUGIN_DEPENDENCY,
											dependency
									)
							);
						}
					}
				}
				for (final var plugin : profileBuild.getPlugins()) {
					artifacts.add(toArtifact(MavenArtifactType.PROFILE_BUILD_PLUGIN, plugin));
					for (final var dependency : plugin.getDependencies()) {
						artifacts.add(toArtifact(MavenArtifactType.PROFILE_BUILD_PLUGIN_DEPENDENCY, dependency));
					}
				}
			}
			final var profileReporting = profile.getReporting();
			if (null != profileReporting) {
				for (final var plugin : profileReporting.getPlugins()) {
					artifacts.add(toArtifact(MavenArtifactType.PROFILE_REPORTING_PLUGIN, plugin));
				}
			}
		}
		return artifacts;
	}

	/**
	 * Inherit a {@link List} of raw artifacts using a {@link List} of effective artifacts.
	 * @param rawArtifacts a {@link List} of raw artifacts
	 * @param effectiveArtifacts a {@link List} of effective artifacts
	 * @return the {@link List} of artifacts
	 * @since 1.0.0
	 */
	private static List<Artifact<MavenArtifactType>> inherit(
			final List<Artifact<MavenArtifactType>> rawArtifacts,
			final List<Artifact<MavenArtifactType>> effectiveArtifacts
	) {
		return rawArtifacts.stream()
				.map(rawArtifact -> {
					final var rawArtifactType = rawArtifact.getType();
					final var rawArtifactIdentifier = rawArtifact.getIdentifier();
					final var rawArtifactOptionalVersion = rawArtifact.getOptionalVersion();
					return effectiveArtifacts.stream()
							.filter(
									effectiveArtifact ->
											rawArtifactType == effectiveArtifact.getType()
													&& rawArtifactIdentifier.equals(effectiveArtifact.getIdentifier())
							)
							.sorted(
									Comparator.comparing(
											effectiveArtifact ->
													rawArtifactOptionalVersion.equals(
															effectiveArtifact.getOptionalVersion()
													),
											Comparator.reverseOrder()
									)
							)
							.map(
									effectiveArtifact ->
											effectiveArtifact.withVersionInherited(rawArtifactOptionalVersion.isEmpty())
							)
							.findAny()
							.orElse(rawArtifact);
				})
				.toList();
	}

	/**
	 * Create a repository from a type and a <i>Maven</i> repository.
	 * @param type a type
	 * @param repository a <i>Maven</i> repository
	 * @return the repository
	 * @since 1.0.0
	 */
	private static Repository toRepository(
			final RepositoryType type,
			final org.apache.maven.model.Repository repository
	) {
		return new Repository(type, repository.getId(), repository.getUrl());
	}

	/**
	 * Create an artifact from a <i>Maven</i> parent.
	 * @param parent a <i>Maven</i> parent
	 * @return the artifact
	 * @since 1.0.0
	 */
	private static Artifact<MavenArtifactType> toArtifact(final Parent parent) {
		return new Artifact<>(
				MavenArtifactType.PARENT,
				new ArtifactIdentifier(parent.getGroupId(), parent.getArtifactId()),
				parent.getVersion()
		);
	}

	/**
	 * Create an artifact from a type and a <i>Maven</i> dependency.
	 * @param type a type
	 * @param dependency a <i>Maven</i> dependency
	 * @return the artifact
	 * @since 1.0.0
	 */
	private static Artifact<MavenArtifactType> toArtifact(final MavenArtifactType type, final Dependency dependency) {
		return new Artifact<>(
				type,
				new ArtifactIdentifier(dependency.getGroupId(), dependency.getArtifactId()),
				dependency.getVersion()
		);
	}

	/**
	 * Create an artifact from a <i>Maven</i> extension.
	 * @param extension a <i>Maven</i> extension
	 * @return the artifact
	 * @since 1.0.0
	 */
	private static Artifact<MavenArtifactType> toArtifact(final Extension extension) {
		return new Artifact<>(
				MavenArtifactType.BUILD_EXTENSION,
				new ArtifactIdentifier(extension.getGroupId(), extension.getArtifactId()),
				extension.getVersion()
		);
	}

	/**
	 * Create an artifact from a type and a <i>Maven</i> plugin.
	 * @param type a type
	 * @param plugin a <i>Maven</i> plugin
	 * @return the artifact
	 * @since 1.0.0
	 */
	private static Artifact<MavenArtifactType> toArtifact(final MavenArtifactType type, final Plugin plugin) {
		return new Artifact<>(
				type,
				new ArtifactIdentifier(plugin.getGroupId(), plugin.getArtifactId()),
				plugin.getVersion()
		);
	}

	/**
	 * Create an artifact from a type and a <i>Maven</i> report plugin.
	 * @param type a type
	 * @param reportPlugin a <i>Maven</i> report plugin
	 * @return the artifact
	 * @since 1.0.0
	 */
	private static Artifact<MavenArtifactType> toArtifact(
			final MavenArtifactType type,
			final ReportPlugin reportPlugin
	) {
		return new Artifact<>(
				type,
				new ArtifactIdentifier(reportPlugin.getGroupId(), reportPlugin.getArtifactId()),
				reportPlugin.getVersion()
		);
	}
}