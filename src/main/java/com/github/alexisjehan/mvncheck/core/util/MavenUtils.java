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
package com.github.alexisjehan.mvncheck.core.util;

import com.github.alexisjehan.javanilla.lang.Strings;
import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.rtinfo.internal.DefaultRuntimeInformation;
import org.apache.maven.settings.Repository;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.DefaultSettingsBuilderFactory;
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingException;
import org.apache.maven.settings.crypto.DefaultSettingsDecrypter;
import org.apache.maven.settings.crypto.DefaultSettingsDecryptionRequest;
import org.eclipse.aether.DefaultRepositoryCache;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.impl.RemoteRepositoryManager;
import org.eclipse.aether.internal.impl.DefaultChecksumPolicyProvider;
import org.eclipse.aether.internal.impl.DefaultRemoteRepositoryManager;
import org.eclipse.aether.internal.impl.DefaultUpdatePolicyAnalyzer;
import org.eclipse.aether.repository.AuthenticationSelector;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.MirrorSelector;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.repository.ProxySelector;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.spi.locator.ServiceLocator;
import org.eclipse.aether.supplier.RepositorySystemSupplier;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.util.repository.ConservativeAuthenticationSelector;
import org.eclipse.aether.util.repository.ConservativeProxySelector;
import org.eclipse.aether.util.repository.DefaultAuthenticationSelector;
import org.eclipse.aether.util.repository.DefaultMirrorSelector;
import org.eclipse.aether.util.repository.DefaultProxySelector;
import org.sonatype.plexus.components.cipher.DefaultPlexusCipher;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Utility class that helps to deal with <i>Maven</i>.
 * @since 1.0.0
 */
public final class MavenUtils {

	/**
	 * <i>Maven</i> version.
	 * @since 1.0.0
	 */
	public static final String VERSION = new DefaultRuntimeInformation().getMavenVersion();

	/**
	 * Pattern to extract the <i>Maven</i> home from the {@code PATH} environment variable.
	 * @since 1.0.0
	 */
	private static final Pattern PATH_PATTERN = Pattern.compile(
			"^(.*[/\\\\]+apache-maven-\\d+\\.\\d+\\.\\d+)[/\\\\]+bin[/\\\\]*$"
	);

	/**
	 * User {@code repository} directory.
	 * @since 1.0.0
	 */
	private static final Path USER_REPOSITORY_DIRECTORY = SystemUtils.getUserHomeDirectory().resolve(
			Path.of(".m2", "repository")
	);

	/**
	 * User {@code settings.xml} file.
	 * @since 1.0.0
	 */
	private static final Path USER_SETTINGS_FILE = SystemUtils.getUserHomeDirectory().resolve(
			Path.of(".m2", "settings.xml")
	);

	/**
	 * User {@code settings-security.xml} file.
	 * @since 1.0.0
	 */
	private static final Path USER_SETTINGS_SECURITY_FILE = SystemUtils.getUserHomeDirectory().resolve(
			Path.of(".m2", "settings-security.xml")
	);

	/**
	 * Logger.
	 * @since 1.0.0
	 */
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Repository system supplier.
	 * @since 1.7.0
	 */
	private static final RepositorySystemSupplier repositorySystemSupplier = new RepositorySystemSupplier();

	/**
	 * Constructor.
	 * @since 1.0.0
	 */
	private MavenUtils() {}

	/**
	 * Retrieve an {@link Optional} of the <i>Maven</i> home.
	 * @return the {@link Optional} of the <i>Maven</i> home
	 * @since 1.0.0
	 */
	public static Optional<String> retrieveOptionalGlobalHome() {
		return Strings.split(File.pathSeparatorChar, SystemUtils.getPathEnvironmentVariable())
				.stream()
				.map(segment -> {
					final var matcher = PATH_PATTERN.matcher(segment);
					if (matcher.find()) {
						return Optional.of(matcher.group(1));
					}
					return Optional.<String>empty();
				})
				.flatMap(Optional::stream)
				.findAny();
	}

	/**
	 * Retrieve an {@link Optional} of the global {@code settings.xml} file.
	 * @return the {@link Optional} of the global {@code settings.xml} file
	 * @since 1.0.0
	 */
	public static Optional<Path> retrieveOptionalGlobalSettingsFile() {
		return retrieveOptionalGlobalHome()
				.map(globalHome -> Path.of(globalHome, "conf", "settings.xml"));
	}

	/**
	 * Create a remote repository from an identifier and a URL.
	 * @param id an identifier
	 * @param url a URL
	 * @return the remote repository
	 * @throws NullPointerException if the identifier or the URL is {@code null}
	 * @throws IllegalArgumentException if the identifier or the URL is empty
	 * @since 1.0.0
	 */
	public static RemoteRepository createRemoteRepository(final String id, final String url) {
		Ensure.notNullAndNotEmpty("id", id);
		Ensure.notNullAndNotEmpty("url", url);
		final var repository = new Repository();
		repository.setId(id);
		repository.setUrl(url);
		return toRemoteRepository(repository);
	}

	/**
	 * Convert a repository to a remote repository.
	 * @param repository a repository
	 * @return the remote repository
	 * @throws NullPointerException if the repository is {@code null}
	 * @since 1.0.0
	 */
	static RemoteRepository toRemoteRepository(final Repository repository) {
		Ensure.notNull("repository", repository);
		final var builder = new RemoteRepository.Builder(
				repository.getId(),
				repository.getLayout(),
				repository.getUrl()
		);
		final var releasesPolicy = repository.getReleases();
		if (null != releasesPolicy) {
			builder.setReleasePolicy(
					new RepositoryPolicy(
							releasesPolicy.isEnabled(),
							releasesPolicy.getUpdatePolicy(),
							releasesPolicy.getChecksumPolicy()
					)
			);
		}
		final var snapshotsPolicy = repository.getSnapshots();
		if (null != snapshotsPolicy) {
			builder.setSnapshotPolicy(
					new RepositoryPolicy(
							snapshotsPolicy.isEnabled(),
							snapshotsPolicy.getUpdatePolicy(),
							snapshotsPolicy.getChecksumPolicy()
					)
			);
		}
		return builder.build();
	}

	/**
	 * Make a service locator.
	 * @return the service locator
	 * @deprecated since 1.1.0, don't use anymore
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.1.0")
	public static ServiceLocator makeServiceLocator() {
		return MavenRepositorySystemUtils.newServiceLocator();
	}

	/**
	 * Make a repository system.
	 * @param serviceLocator a service locator
	 * @return the repository system
	 * @throws NullPointerException if the service locator is {@code null}
	 * @deprecated since 1.1.0, use {@link #makeRepositorySystem()} instead
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.1.0")
	public static RepositorySystem makeRepositorySystem(final ServiceLocator serviceLocator) {
		Ensure.notNull("serviceLocator", serviceLocator);
		return makeRepositorySystem();
	}

	/**
	 * Make a repository system.
	 * @return the repository system
	 * @since 1.1.0
	 */
	public static RepositorySystem makeRepositorySystem() {
		return repositorySystemSupplier.get();
	}

	/**
	 * Make a remote repository manager.
	 * @param serviceLocator a service locator
	 * @return the remote repository manager
	 * @throws NullPointerException if the service locator is {@code null}
	 * @deprecated since 1.1.0, use {@link #makeRemoteRepositoryManager()} instead
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.1.0")
	public static RemoteRepositoryManager makeRemoteRepositoryManager(final ServiceLocator serviceLocator) {
		Ensure.notNull("serviceLocator", serviceLocator);
		return makeRemoteRepositoryManager();
	}

	/**
	 * Make a remote repository manager.
	 * @return the remote repository manager
	 * @since 1.1.0
	 */
	public static RemoteRepositoryManager makeRemoteRepositoryManager() {
		return new DefaultRemoteRepositoryManager(
				new DefaultUpdatePolicyAnalyzer(),
				new DefaultChecksumPolicyProvider()
		);
	}

	/**
	 * Make settings using default global and user {@code settings.xml} files.
	 * @return settings
	 * @throws SettingsBuildingException might occur while resolving settings
	 * @since 1.0.0
	 */
	public static Settings makeSettings() throws SettingsBuildingException {
		return makeSettings(retrieveOptionalGlobalSettingsFile().orElse(null), USER_SETTINGS_FILE);
	}

	/**
	 * Make settings using custom global and user {@code settings.xml} files.
	 * @param globalSettingsFile a global {@code settings.xml} file or {@code null}
	 * @param userSettingsFile a user {@code settings.xml} file or {@code null}
	 * @return settings
	 * @throws SettingsBuildingException might occur while resolving settings
	 * @since 1.0.0
	 */
	static Settings makeSettings(
			final Path globalSettingsFile,
			final Path userSettingsFile
	) throws SettingsBuildingException {
		final var result = new DefaultSettingsBuilderFactory()
				.newInstance()
				.build(
						new DefaultSettingsBuildingRequest()
								.setGlobalSettingsFile(
										null != globalSettingsFile
												? globalSettingsFile.toFile()
												: null
								)
								.setUserSettingsFile(
										null != userSettingsFile
												? userSettingsFile.toFile()
												: null
								)
								.setSystemProperties(System.getProperties())
				);
		for (final var exception : result.getProblems()) {
			logger.warn(exception::getMessage);
		}
		return result.getEffectiveSettings();
	}

	/**
	 * Make decrypted settings using the default user {@code settings-security.xml} file.
	 * @param settings settings
	 * @return decrypted settings
	 * @throws NullPointerException if settings are {@code null}
	 * @since 1.0.0
	 */
	public static DecryptedSettings makeDecryptedSettings(final Settings settings) {
		return makeDecryptedSettings(settings, USER_SETTINGS_SECURITY_FILE);
	}

	/**
	 * Make decrypted settings using a custom user {@code settings-security.xml} file.
	 * @param settings settings
	 * @param userSettingsSecurityFile a user {@code settings-security.xml} file or {@code null}
	 * @return decrypted settings
	 * @throws NullPointerException if settings are {@code null}
	 * @since 1.0.0
	 */
	static DecryptedSettings makeDecryptedSettings(
			final Settings settings,
			final Path userSettingsSecurityFile
	) {
		Ensure.notNull("settings", settings);
		final var result = new DefaultSettingsDecrypter(
				new DefaultSecDispatcher(
						new DefaultPlexusCipher(),
						Map.of(),
						null != userSettingsSecurityFile
								? userSettingsSecurityFile.toString()
								: null
				)
		).decrypt(new DefaultSettingsDecryptionRequest(settings));
		for (final var exception : result.getProblems()) {
			logger.warn(exception::getMessage);
		}
		return new DecryptedSettings(result.getProxies(), result.getServers());
	}

	/**
	 * Make a local repository.
	 * @param settings settings
	 * @return the local repository
	 * @throws NullPointerException if settings are {@code null}
	 * @since 1.0.0
	 */
	static LocalRepository makeLocalRepository(final Settings settings) {
		Ensure.notNull("settings", settings);
		final var settingsLocalRepository = settings.getLocalRepository();
		if (null != settingsLocalRepository) {
			return new LocalRepository(settingsLocalRepository);
		}
		return new LocalRepository(USER_REPOSITORY_DIRECTORY.toFile());
	}

	/**
	 * Make a proxy selector.
	 * @param decryptedSettings decrypted settings
	 * @return the proxy selector
	 * @throws NullPointerException if decrypted settings are {@code null}
	 * @since 1.0.0
	 */
	static ProxySelector makeProxySelector(final DecryptedSettings decryptedSettings) {
		Ensure.notNull("decryptedSettings", decryptedSettings);
		final var proxySelector = new DefaultProxySelector();
		for (final var proxy : decryptedSettings.getProxies()) {
			proxySelector.add(
					new Proxy(
							proxy.getProtocol(),
							proxy.getHost(),
							proxy.getPort(),
							new AuthenticationBuilder()
									.addUsername(proxy.getUsername())
									.addPassword(proxy.getPassword())
									.build()
					),
					proxy.getNonProxyHosts()
			);
		}
		return new ConservativeProxySelector(proxySelector);
	}

	/**
	 * Make an authentication selector.
	 * @param decryptedSettings decrypted settings
	 * @return the authentication selector
	 * @throws NullPointerException if decrypted settings are {@code null}
	 * @since 1.0.0
	 */
	static AuthenticationSelector makeAuthenticationSelector(final DecryptedSettings decryptedSettings) {
		Ensure.notNull("decryptedSettings", decryptedSettings);
		final var authenticationSelector = new DefaultAuthenticationSelector();
		for (final var server : decryptedSettings.getServers()) {
			authenticationSelector.add(
					server.getId(),
					new AuthenticationBuilder()
							.addUsername(server.getUsername())
							.addPassword(server.getPassword())
							.addPrivateKey(server.getPrivateKey(), server.getPassphrase())
							.build()
			);
		}
		return new ConservativeAuthenticationSelector(authenticationSelector);
	}

	/**
	 * Make a mirror selector.
	 * @param settings settings
	 * @return the mirror selector
	 * @throws NullPointerException if settings are {@code null}
	 * @since 1.0.0
	 */
	static MirrorSelector makeMirrorSelector(final Settings settings) {
		Ensure.notNull("settings", settings);
		final var mirrorSelector = new DefaultMirrorSelector();
		for (final var mirror : settings.getMirrors()) {
			mirrorSelector.add(
					mirror.getId(),
					mirror.getUrl(),
					mirror.getLayout(),
					false,
					mirror.isBlocked(),
					mirror.getMirrorOf(),
					mirror.getMirrorOfLayouts()
			);
		}
		return mirrorSelector;
	}

	/**
	 * Make a repository system session.
	 * @param settings settings
	 * @param decryptedSettings decrypted settings
	 * @param repositorySystem a repository system
	 * @return the repository system session
	 * @throws NullPointerException if settings, decrypted settings or the repository system are {@code null}
	 * @since 1.0.0
	 */
	public static RepositorySystemSession makeRepositorySystemSession(
			final Settings settings,
			final DecryptedSettings decryptedSettings,
			final RepositorySystem repositorySystem
	) {
		Ensure.notNull("settings", settings);
		Ensure.notNull("decryptedSettings", decryptedSettings);
		Ensure.notNull("repositorySystem", repositorySystem);
		final var repositorySystemSession = MavenRepositorySystemUtils.newSession();
		return repositorySystemSession
				.setOffline(settings.isOffline())
				.setLocalRepositoryManager(
						repositorySystem.newLocalRepositoryManager(
								repositorySystemSession,
								makeLocalRepository(settings)
						)
				)
				.setProxySelector(makeProxySelector(decryptedSettings))
				.setAuthenticationSelector(makeAuthenticationSelector(decryptedSettings))
				.setMirrorSelector(makeMirrorSelector(settings))
				.setCache(new DefaultRepositoryCache())
				.setSystemProperties(System.getProperties());
	}

	/**
	 * Make a {@link List} of remote repositories.
	 * @param settings settings
	 * @return the {@link List} of remote repositories
	 * @throws NullPointerException if settings are {@code null}
	 * @since 1.0.0
	 */
	public static List<RemoteRepository> makeRemoteRepositories(final Settings settings) {
		Ensure.notNull("settings", settings);
		final var remoteRepositories = new ArrayList<RemoteRepository>();
		final var activeProfileIds = settings.getActiveProfiles();
		for (final var entry : settings.getProfilesAsMap().entrySet()) {
			final var id = entry.getKey();
			final var profile = entry.getValue();
			final var profileActivation = profile.getActivation();
			if (activeProfileIds.contains(id) || null != profileActivation && profileActivation.isActiveByDefault()) {
				for (final var repository : profile.getRepositories()) {
					remoteRepositories.add(toRemoteRepository(repository));
				}
				for (final var pluginRepository : profile.getPluginRepositories()) {
					remoteRepositories.add(toRemoteRepository(pluginRepository));
				}
			}
		}
		return remoteRepositories;
	}

	/**
	 * Get the <i>Maven</i> version.
	 * @return the <i>Maven</i> version
	 * @deprecated since 1.4.0, use {@link #VERSION} instead
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.4.0")
	public static String getVersion() {
		return VERSION;
	}
}