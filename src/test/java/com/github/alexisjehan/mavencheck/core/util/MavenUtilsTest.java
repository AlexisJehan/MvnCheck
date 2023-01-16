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
package com.github.alexisjehan.mavencheck.core.util;

import com.github.alexisjehan.javanilla.lang.Strings;
import org.apache.maven.settings.Repository;
import org.apache.maven.settings.RepositoryPolicy;
import org.apache.maven.settings.building.SettingsBuildingException;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class MavenUtilsTest {

	@Test
	void testRetrieveOptionalGlobalHome() {
		try (final var mockedSystemUtils = Mockito.mockStatic(SystemUtils.class)) {
			mockedSystemUtils.when(SystemUtils::getPathEnvironmentVariable)
					.thenReturn(
							String.join(
									File.pathSeparator,
									"foo",
									"bar"
							)
					);
			mockedSystemUtils.when(SystemUtils::getUserHomeDirectory)
					.thenCallRealMethod();
			assertThat(MavenUtils.retrieveOptionalGlobalHome()).isEmpty();
		}
		try (final var mockedSystemUtils = Mockito.mockStatic(SystemUtils.class)) {
			mockedSystemUtils.when(SystemUtils::getPathEnvironmentVariable)
					.thenReturn(
							String.join(
									File.pathSeparator,
									"foo",
									File.separator + "apache-maven-1.0.0" + File.separator + "bin",
									"bar"
							)
					);
			mockedSystemUtils.when(SystemUtils::getUserHomeDirectory)
					.thenCallRealMethod();
			assertThat(MavenUtils.retrieveOptionalGlobalHome()).contains(File.separator + "apache-maven-1.0.0");
		}
	}

	@Test
	void testRetrieveOptionalGlobalSettingsFile() {
		try (final var mockedSystemUtils = Mockito.mockStatic(SystemUtils.class)) {
			mockedSystemUtils.when(SystemUtils::getPathEnvironmentVariable)
					.thenReturn(
							String.join(
									File.pathSeparator,
									"foo",
									"bar"
							)
					);
			mockedSystemUtils.when(SystemUtils::getUserHomeDirectory)
					.thenCallRealMethod();
			assertThat(MavenUtils.retrieveOptionalGlobalSettingsFile())
					.isEmpty();
		}
		try (final var mockedSystemUtils = Mockito.mockStatic(SystemUtils.class)) {
			mockedSystemUtils.when(SystemUtils::getPathEnvironmentVariable)
					.thenReturn(
							String.join(
									File.pathSeparator,
									"foo",
									File.separator + "apache-maven-1.0.0" + File.separator + "bin",
									"bar"
							)
					);
			mockedSystemUtils.when(SystemUtils::getUserHomeDirectory)
					.thenCallRealMethod();
			assertThat(MavenUtils.retrieveOptionalGlobalSettingsFile())
					.contains(
							Path.of(
									File.separator + "apache-maven-1.0.0",
									"conf",
									"settings.xml"
							)
					);
		}
	}

	@Test
	void testCreateRemoteRepository() {
		final var remoteRepository = MavenUtils.createRemoteRepository("foo-id", "foo-url");
		assertThat(remoteRepository.getId()).isEqualTo("foo-id");
		assertThat(remoteRepository.getUrl()).isEqualTo("foo-url");
	}

	@Test
	void testCreateRemoteRepositoryInvalid() {
		assertThatNullPointerException()
				.isThrownBy(() -> MavenUtils.createRemoteRepository(null, "https://foo-host"));
		assertThatIllegalArgumentException()
				.isThrownBy(() -> MavenUtils.createRemoteRepository(Strings.EMPTY, "https://foo-host"));
		assertThatNullPointerException()
				.isThrownBy(() -> MavenUtils.createRemoteRepository("foo-id", null));
		assertThatIllegalArgumentException()
				.isThrownBy(() -> MavenUtils.createRemoteRepository("foo-id", Strings.EMPTY));
	}

	@Test
	void testToRemoteRepository() {
		final var repository = new Repository();
		repository.setId("foo-id");
		repository.setLayout("foo-layout");
		repository.setUrl("https://foo-host");
		final var releasesPolicy = new RepositoryPolicy();
		releasesPolicy.setEnabled(true);
		releasesPolicy.setUpdatePolicy("foo-update-releases-policy");
		releasesPolicy.setChecksumPolicy("foo-checksum-releases-policy");
		repository.setReleases(releasesPolicy);
		final var snapshotsPolicy = new RepositoryPolicy();
		snapshotsPolicy.setEnabled(false);
		snapshotsPolicy.setUpdatePolicy("foo-update-snapshots-policy");
		snapshotsPolicy.setChecksumPolicy("foo-checksum-snapshots-policy");
		repository.setSnapshots(snapshotsPolicy);
		final var remoteRepository = MavenUtils.toRemoteRepository(repository);
		assertThat(remoteRepository.getId())
				.isEqualTo("foo-id");
		assertThat(remoteRepository.getContentType())
				.isEqualTo("foo-layout");
		assertThat(remoteRepository.getUrl())
				.isEqualTo("https://foo-host");
		assertThat(remoteRepository.getPolicy(false).isEnabled())
				.isTrue();
		assertThat(remoteRepository.getPolicy(false).getUpdatePolicy())
				.isEqualTo("foo-update-releases-policy");
		assertThat(remoteRepository.getPolicy(false).getChecksumPolicy())
				.isEqualTo("foo-checksum-releases-policy");
		assertThat(remoteRepository.getPolicy(true).isEnabled())
				.isFalse();
		assertThat(remoteRepository.getPolicy(true).getUpdatePolicy())
				.isEqualTo("foo-update-snapshots-policy");
		assertThat(remoteRepository.getPolicy(true).getChecksumPolicy())
				.isEqualTo("foo-checksum-snapshots-policy");
	}

	@Test
	void testToRemoteRepositoryInvalid() {
		assertThatNullPointerException().isThrownBy(() -> MavenUtils.toRemoteRepository(null));
	}

	@Test
	void testMakeServiceLocator() {
		assertThat(MavenUtils.makeServiceLocator()).isNotNull();
	}

	@Test
	void testMakeRepositorySystem() {
		assertThat(MavenUtils.makeRepositorySystem(MavenUtils.makeServiceLocator())).isNotNull();
	}

	@Test
	void testMakeRepositorySystemInvalid() {
		assertThatNullPointerException().isThrownBy(() -> MavenUtils.makeRepositorySystem(null));
	}

	@Test
	void testMakeRemoteRepositoryManager() {
		assertThat(MavenUtils.makeRemoteRepositoryManager(MavenUtils.makeServiceLocator())).isNotNull();
	}

	@Test
	void testMakeRemoteRepositoryManagerInvalid() {
		assertThatNullPointerException().isThrownBy(() -> MavenUtils.makeRemoteRepositoryManager(null));
	}

	@Test
	void testMakeSettings() throws SettingsBuildingException {
		assertThat(MavenUtils.makeSettings()).isNotNull();
		assertThat(MavenUtils.makeSettings(null, null)).isNotNull();
		assertThat(Path.of("src", "test", "resources", "settings_foo.xml")).satisfies(
				settingsFile -> assertThat(MavenUtils.makeSettings(settingsFile, settingsFile)).isNotNull()
		);
		assertThat(Path.of("src", "test", "resources", "settings_warning.xml")).satisfies(
				settingsFile -> assertThat(MavenUtils.makeSettings(settingsFile, settingsFile)).isNotNull()
		);
		assertThat(Path.of("src", "test", "resources", "settings_not-found.xml")).satisfies(
				settingsFile -> assertThat(MavenUtils.makeSettings(settingsFile, settingsFile)).isNotNull()
		);
	}

	@Test
	void testMakeSettingsInvalid() {
		final var settingsFile = Path.of("src", "test", "resources", "settings_error.xml");
		assertThatExceptionOfType(SettingsBuildingException.class)
				.isThrownBy(() -> MavenUtils.makeSettings(settingsFile, settingsFile));
	}

	@Test
	void testMakeDecryptedSettings() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings(
				null,
				Path.of("src", "test", "resources", "settings_foo.xml")
		);
		assertThat(
				MavenUtils.makeDecryptedSettings(settings)
		).satisfies(decryptedSettings -> {
			assertThat(decryptedSettings.getProxies())
					.anySatisfy(proxy -> assertThat(proxy.getPassword()).isNotEqualTo("foo-password"));
			assertThat(decryptedSettings.getServers())
					.anySatisfy(server -> assertThat(server.getPassword()).isNotEqualTo("foo-password"));
		});
		assertThat(
				MavenUtils.makeDecryptedSettings(settings, null)
		).satisfies(decryptedSettings -> {
			assertThat(decryptedSettings.getProxies())
					.anySatisfy(proxy -> assertThat(proxy.getPassword()).isNotEqualTo("foo-password"));
			assertThat(decryptedSettings.getServers())
					.anySatisfy(server -> assertThat(server.getPassword()).isNotEqualTo("foo-password"));
		});
		assertThat(
				MavenUtils.makeDecryptedSettings(
						settings,
						Path.of("src", "test", "resources", "settings-security.xml")
				)
		).satisfies(decryptedSettings -> {
			assertThat(decryptedSettings.getProxies())
					.anySatisfy(proxy -> assertThat(proxy.getPassword()).isEqualTo("foo-password"));
			assertThat(decryptedSettings.getServers())
					.anySatisfy(server -> assertThat(server.getPassword()).isEqualTo("foo-password"));
		});
		assertThat(
				MavenUtils.makeDecryptedSettings(
						settings,
						Path.of("src", "test", "resources", "settings-security_not-found.xml")
				)
		).satisfies(decryptedSettings -> {
			assertThat(decryptedSettings.getProxies())
					.anySatisfy(proxy -> assertThat(proxy.getPassword()).isNotEqualTo("foo-password"));
			assertThat(decryptedSettings.getServers())
					.anySatisfy(server -> assertThat(server.getPassword()).isNotEqualTo("foo-password"));
		});
	}

	@Test
	void testMakeDecryptedSettingsInvalid() {
		assertThatNullPointerException().isThrownBy(() -> MavenUtils.makeDecryptedSettings(null));
	}

	@Test
	void testMakeLocalRepository() throws SettingsBuildingException {
		final var localRepository = new LocalRepository("foo-directory");
		assertThat(
				MavenUtils.makeLocalRepository(
						MavenUtils.makeSettings(null, null)
				)
		).isNotEqualTo(localRepository);
		assertThat(
				MavenUtils.makeLocalRepository(
						MavenUtils.makeSettings(
								null,
								Path.of("src", "test", "resources", "settings_foo.xml")
						)
				)
		).isEqualTo(localRepository);
	}

	@Test
	void testMakeLocalRepositoryInvalid() {
		assertThatNullPointerException().isThrownBy(() -> MavenUtils.makeLocalRepository(null));
	}

	@Test
	void testMakeProxySelector() throws SettingsBuildingException {
		final var proxy = new Proxy(
				"http",
				"foo-host",
				8080,
				new AuthenticationBuilder()
						.addUsername("foo-username")
						.addPassword("foo-password")
						.build()
		);
		assertThat(
				MavenUtils.makeProxySelector(
						MavenUtils.makeDecryptedSettings(
								MavenUtils.makeSettings(null, null),
								null
						)
				)
		).satisfies(proxySelector -> {
			assertThat(proxySelector.getProxy(MavenUtils.createRemoteRepository("foo-id", "https://foo-host")))
					.isNull();
			assertThat(proxySelector.getProxy(MavenUtils.createRemoteRepository("bar-id", "https://bar-host")))
					.isNull();
		});
		assertThat(
				MavenUtils.makeProxySelector(
						MavenUtils.makeDecryptedSettings(
								MavenUtils.makeSettings(
										null,
										Path.of("src", "test", "resources", "settings_foo.xml")
								),
								Path.of("src", "test", "resources", "settings-security.xml")
						)
				)
		).satisfies(proxySelector -> {
			assertThat(proxySelector.getProxy(MavenUtils.createRemoteRepository("foo-id", "https://foo-host")))
					.isEqualTo(proxy);
			assertThat(proxySelector.getProxy(MavenUtils.createRemoteRepository("bar-id", "https://bar-host")))
					.isNull();
		});
	}

	@Test
	void testMakeProxySelectorInvalid() {
		assertThatNullPointerException().isThrownBy(() -> MavenUtils.makeProxySelector(null));
	}

	@Test
	void testMakeAuthenticationSelector() throws SettingsBuildingException {
		final var authentication = new AuthenticationBuilder()
				.addUsername("foo-username")
				.addPassword("foo-password")
				.build();
		assertThat(
				MavenUtils.makeAuthenticationSelector(
						MavenUtils.makeDecryptedSettings(
								MavenUtils.makeSettings(null, null),
								null
						)
				)
		).satisfies(authenticationSelector -> {
			assertThat(
					authenticationSelector.getAuthentication(
							MavenUtils.createRemoteRepository("foo-id", "https://foo-host")
					)
			).isNull();
			assertThat(
					authenticationSelector.getAuthentication(
							MavenUtils.createRemoteRepository("bar-id", "https://bar-host")
					)
			).isNull();
		});
		assertThat(
				MavenUtils.makeAuthenticationSelector(
						MavenUtils.makeDecryptedSettings(
								MavenUtils.makeSettings(
										null,
										Path.of("src", "test", "resources", "settings_foo.xml")
								),
								Path.of("src", "test", "resources", "settings-security.xml")
						)
				)
		).satisfies(authenticationSelector -> {
			assertThat(
					authenticationSelector.getAuthentication(
							MavenUtils.createRemoteRepository("foo-id", "https://foo-host")
					)
			).isEqualTo(authentication);
			assertThat(
					authenticationSelector.getAuthentication(
							MavenUtils.createRemoteRepository("bar-id", "https://bar-host")
					)
			).isNull();
		});
	}

	@Test
	void testMakeAuthenticationSelectorInvalid() {
		assertThatNullPointerException().isThrownBy(() -> MavenUtils.makeAuthenticationSelector(null));
	}

	@Test
	void testMakeMirrorSelector() throws SettingsBuildingException {
		final var remoteRepositoryBuilder = new RemoteRepository.Builder(
				"default",
				"default",
				"https://foo-mirror-host"
		);
		assertThat(
				MavenUtils.makeMirrorSelector(
						MavenUtils.makeSettings(null, null)
				)
		).satisfies(mirrorSelector -> {
			assertThat(MavenUtils.createRemoteRepository("foo-id", "https://foo-host")).satisfies(
					remoteRepository -> assertThat(mirrorSelector.getMirror(remoteRepository)).isNull()
			);
			assertThat(MavenUtils.createRemoteRepository("bar-id", "https://bar-host")).satisfies(
					remoteRepository -> assertThat(mirrorSelector.getMirror(remoteRepository)).isNull()
			);
		});
		assertThat(
				MavenUtils.makeMirrorSelector(
						MavenUtils.makeSettings(
								null,
								Path.of("src", "test", "resources", "settings_foo.xml")
						)
				)
		).satisfies(mirrorSelector -> {
			assertThat(MavenUtils.createRemoteRepository("foo-id", "https://foo-host")).satisfies(
					remoteRepository -> assertThat(mirrorSelector.getMirror(remoteRepository)).isEqualTo(
							remoteRepositoryBuilder.setMirroredRepositories(List.of(remoteRepository)).build()
					)
			);
			assertThat(MavenUtils.createRemoteRepository("bar-id", "https://bar-host")).satisfies(
					remoteRepository -> assertThat(mirrorSelector.getMirror(remoteRepository)).isNull()
			);
		});
	}

	@Test
	void testMakeMirrorSelectorInvalid() {
		assertThatNullPointerException().isThrownBy(() -> MavenUtils.makeMirrorSelector(null));
	}

	@Test
	void testMakeRepositorySystemSession() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings(
				null,
				Path.of("src", "test", "resources", "settings_foo.xml")
		);
		final var decryptedSettings = MavenUtils.makeDecryptedSettings(
				settings,
				Path.of("src", "test", "resources", "settings-security.xml")
		);
		final var repositorySystem = MavenUtils.makeRepositorySystem(MavenUtils.makeServiceLocator());
		assertThat(MavenUtils.makeRepositorySystemSession(settings, decryptedSettings, repositorySystem)).isNotNull();
	}

	@Test
	void testMakeRepositorySystemSessionInvalid() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings(
				null,
				Path.of("src", "test", "resources", "settings_foo.xml")
		);
		final var decryptedSettings = MavenUtils.makeDecryptedSettings(
				settings,
				Path.of("src", "test", "resources", "settings-security.xml")
		);
		final var repositorySystem = MavenUtils.makeRepositorySystem(MavenUtils.makeServiceLocator());
		assertThatNullPointerException().isThrownBy(
				() -> MavenUtils.makeRepositorySystemSession(null, decryptedSettings, repositorySystem)
		);
		assertThatNullPointerException().isThrownBy(
				() -> MavenUtils.makeRepositorySystemSession(settings, null, repositorySystem)
		);
		assertThatNullPointerException().isThrownBy(
				() -> MavenUtils.makeRepositorySystemSession(settings, decryptedSettings, null)
		);
	}

	@Test
	void testMakeRemoteRepositories() throws SettingsBuildingException {
		assertThat(
				MavenUtils.makeRemoteRepositories(
						MavenUtils.makeSettings(null, null)
				)
		).isEmpty();
		assertThat(
				MavenUtils.makeRemoteRepositories(
						MavenUtils.makeSettings(
								null,
								Path.of("src", "test", "resources", "settings_foo.xml")
						)
				)
		).containsExactly(
				MavenUtils.createRemoteRepository("foo-id", "https://foo-host"),
				MavenUtils.createRemoteRepository("foo-plugin-id", "https://foo-plugin-host")
		);
	}

	@Test
	void testMakeRemoteRepositoriesInvalid() {
		assertThatNullPointerException().isThrownBy(() -> MavenUtils.makeRemoteRepositories(null));
	}

	@Test
	void testGetVersion() {
		assertThat(MavenUtils.getVersion()).matches("^\\d+\\.\\d+\\.\\d+$");
	}
}