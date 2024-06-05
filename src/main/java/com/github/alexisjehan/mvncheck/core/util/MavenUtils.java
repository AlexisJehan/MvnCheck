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
package com.github.alexisjehan.mvncheck.core.util;

import com.github.alexisjehan.javanilla.lang.Strings;
import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.google.inject.Guice;
import com.google.inject.Injector;
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
import org.eclipse.aether.repository.AuthenticationSelector;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.MirrorSelector;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.repository.ProxySelector;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.spi.locator.ServiceLocator;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.util.repository.ConservativeAuthenticationSelector;
import org.eclipse.aether.util.repository.ConservativeProxySelector;
import org.eclipse.aether.util.repository.DefaultAuthenticationSelector;
import org.eclipse.aether.util.repository.DefaultMirrorSelector;
import org.eclipse.aether.util.repository.DefaultProxySelector;
import org.eclipse.sisu.launch.Main;
import org.eclipse.sisu.space.BeanScanning;
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
 * <p>Utility class that helps dealing with <i>Maven</i>.</p>
 * @since 1.0.0
 */
public final class MavenUtils {

	/**
	 * <p><i>Maven</i> version.</p>
	 * @since 1.0.0
	 */
	public static final String VERSION = new DefaultRuntimeInformation().getMavenVersion();

	/**
	 * <p>Pattern to extract the <i>Maven</i> home from the {@code PATH} environment variable.</p>
	 * @since 1.0.0
	 */
	private static final Pattern PATH_PATTERN = Pattern.compile(
			"^(.*[/\\\\]+apache-maven-\\d+\\.\\d+\\.\\d+)[/\\\\]+bin[/\\\\]*$"
	);

	/**
	 * <p>User {@code repository} directory.</p>
	 * @since 1.0.0
	 */
	private static final Path USER_REPOSITORY_DIRECTORY = SystemUtils.getUserHomeDirectory().resolve(
			Path.of(".m2", "repository")
	);

	/**
	 * <p>User {@code settings.xml} file.</p>
	 * @since 1.0.0
	 */
	private static final Path USER_SETTINGS_FILE = SystemUtils.getUserHomeDirectory().resolve(
			Path.of(".m2", "settings.xml")
	);

	/**
	 * <p>User {@code settings-security.xml} file.</p>
	 * @since 1.0.0
	 */
	private static final Path USER_SETTINGS_SECURITY_FILE = SystemUtils.getUserHomeDirectory().resolve(
			Path.of(".m2", "settings-security.xml")
	);

	/**
	 * <p>Logger.</p>
	 * @since 1.0.0
	 */
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * <p>Dependency injector.</p>
	 * @since 1.1.0
	 */
	private static final Injector injector = Guice.createInjector(Main.wire(BeanScanning.INDEX));

	/**
	 * <p>Constructor.</p>
	 * @since 1.0.0
	 */
	private MavenUtils() {}

	/**
	 * <p>Retrieve an {@link Optional} of the <i>Maven</i> home.</p>
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
	 * <p>Retrieve an {@link Optional} of the global {@code settings.xml} file.</p>
	 * @return the {@link Optional} of the global {@code settings.xml} file
	 * @since 1.0.0
	 */
	public static Optional<Path> retrieveOptionalGlobalSettingsFile() {
		return retrieveOptionalGlobalHome()
				.map(globalHome -> Path.of(globalHome, "conf", "settings.xml"));
	}

	/**
	 * <p>Create a remote repository from an identifier and an URL.</p>
	 * @param id an identifier
	 * @param url an URL
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
	 * <p>Convert a repository to a remote repository.</p>
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
	 * <p>Make a service locator.</p>
	 * @return the service locator
	 * @deprecated since 1.1.0, don't use anymore
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.1.0")
	public static ServiceLocator makeServiceLocator() {
		return MavenRepositorySystemUtils.newServiceLocator();
	}

	/**
	 * <p>Make a repository system.</p>
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
	 * <p>Make a repository system.</p>
	 * @return the repository system
	 * @since 1.1.0
	 */
	public static RepositorySystem makeRepositorySystem() {
		return injector.getInstance(RepositorySystem.class);
	}

	/**
	 * <p>Make a remote repository manager.</p>
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
	 * <p>Make a remote repository manager.</p>
	 * @return the remote repository manager
	 * @since 1.1.0
	 */
	public static RemoteRepositoryManager makeRemoteRepositoryManager() {
		return injector.getInstance(RemoteRepositoryManager.class);
	}

	/**
	 * <p>Make settings using default global and user {@code settings.xml} files.</p>
	 * @return settings
	 * @throws SettingsBuildingException might occur while resolving settings
	 * @since 1.0.0
	 */
	public static Settings makeSettings() throws SettingsBuildingException {
		return makeSettings(retrieveOptionalGlobalSettingsFile().orElse(null), USER_SETTINGS_FILE);
	}

	/**
	 * <p>Make settings using custom global and user {@code settings.xml} files.</p>
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
	 * <p>Make decrypted settings using the default user {@code settings-security.xml} file.</p>
	 * @param settings settings
	 * @return decrypted settings
	 * @throws NullPointerException if settings are {@code null}
	 * @since 1.0.0
	 */
	public static DecryptedSettings makeDecryptedSettings(final Settings settings) {
		return makeDecryptedSettings(settings, USER_SETTINGS_SECURITY_FILE);
	}

	/**
	 * <p>Make decrypted settings using a custom user {@code settings-security.xml} file.</p>
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
	 * <p>Make a local repository.</p>
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
	 * <p>Make a proxy selector.</p>
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
	 * <p>Make an authentication selector.</p>
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
	 * <p>Make a mirror selector.</p>
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
	 * <p>Make a repository system session.</p>
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
	 * <p>Make a {@link List} of remote repositories.</p>
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
	 * <p>Get the <i>Maven</i> version.</p>
	 * @return the <i>Maven</i> version
	 * @deprecated since 1.4.0, use {@link #VERSION} instead
	 * @since 1.0.0
	 */
	@Deprecated(since = "1.4.0")
	public static String getVersion() {
		return VERSION;
	}
}