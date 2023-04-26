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

import org.apache.maven.settings.building.SettingsBuildingException;
import org.eclipse.aether.RepositoryEvent;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.metadata.DefaultMetadata;
import org.eclipse.aether.metadata.Metadata;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@Deprecated
final class ConsoleRepositoryListenerTest {

	private final ConsoleRepositoryListener consoleRepositoryListener = new ConsoleRepositoryListener();

	@Test
	void testArtifactDescriptorInvalid() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.artifactDescriptorInvalid(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.ARTIFACT_DESCRIPTOR_INVALID
						)
								.setArtifact(new DefaultArtifact("foo-group-id:foo-artifact-id:1.0.0"))
								.setException(new RuntimeException())
								.build()
				)
		);
	}

	@Test
	void testArtifactDescriptorInvalidInvalid() {
		assertThatNullPointerException()
				.isThrownBy(() -> consoleRepositoryListener.artifactDescriptorInvalid(null));
	}

	@Test
	void testArtifactDescriptorMissing() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.artifactDescriptorMissing(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.ARTIFACT_DESCRIPTOR_MISSING
						)
								.setArtifact(new DefaultArtifact("foo-group-id:foo-artifact-id:1.0.0"))
								.build()
				)
		);
	}

	@Test
	void testArtifactDescriptorMissingInvalid() {
		assertThatNullPointerException()
				.isThrownBy(() -> consoleRepositoryListener.artifactDescriptorMissing(null));
	}

	@Test
	void testArtifactResolving() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.artifactResolving(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.ARTIFACT_RESOLVING
						)
								.setArtifact(new DefaultArtifact("foo-group-id:foo-artifact-id:1.0.0"))
								.setRepository(MavenUtils.createRemoteRepository("foo-id", "https://foo-host"))
								.build()
				)
		);
	}

	@Test
	void testArtifactResolvingInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.artifactResolving(null));
	}

	@Test
	void testArtifactResolved() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.artifactResolved(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.ARTIFACT_RESOLVED
						)
								.setArtifact(new DefaultArtifact("foo-group-id:foo-artifact-id:1.0.0"))
								.setRepository(MavenUtils.createRemoteRepository("foo-id", "https://foo-host"))
								.build()
				)
		);
	}

	@Test
	void testArtifactResolvedInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.artifactResolved(null));
	}

	@Test
	void testArtifactDownloading() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.artifactDownloading(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.ARTIFACT_DOWNLOADING
						)
								.setArtifact(new DefaultArtifact("foo-group-id:foo-artifact-id:1.0.0"))
								.setRepository(MavenUtils.createRemoteRepository("foo-id", "https://foo-host"))
								.build()
				)
		);
	}

	@Test
	void testArtifactDownloadingInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.artifactDownloading(null));
	}

	@Test
	void testArtifactDownloaded() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.artifactDownloaded(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.ARTIFACT_DOWNLOADED
						)
								.setArtifact(new DefaultArtifact("foo-group-id:foo-artifact-id:1.0.0"))
								.setRepository(MavenUtils.createRemoteRepository("foo-id", "https://foo-host"))
								.build()
				)
		);
	}

	@Test
	void testArtifactDownloadedInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.artifactDownloaded(null));
	}

	@Test
	void testArtifactInstalling() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.artifactInstalling(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.ARTIFACT_INSTALLING
						)
								.setArtifact(new DefaultArtifact("foo-group-id:foo-artifact-id:1.0.0"))
								.setFile(new File("foo-file"))
								.build()
				)
		);
	}

	@Test
	void testArtifactInstallingInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.artifactInstalling(null));
	}

	@Test
	void testArtifactInstalled() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.artifactInstalled(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.ARTIFACT_INSTALLED
						)
								.setArtifact(new DefaultArtifact("foo-group-id:foo-artifact-id:1.0.0"))
								.setFile(new File("foo-file"))
								.build()
				)
		);
	}

	@Test
	void testArtifactInstalledInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.artifactInstalled(null));
	}

	@Test
	void testArtifactDeploying() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.artifactDeploying(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.ARTIFACT_DEPLOYING
						)
								.setArtifact(new DefaultArtifact("foo-group-id:foo-artifact-id:1.0.0"))
								.setRepository(MavenUtils.createRemoteRepository("foo-id", "https://foo-host"))
								.build()
				)
		);
	}

	@Test
	void testArtifactDeployingInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.artifactDeploying(null));
	}

	@Test
	void testArtifactDeployed() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.artifactDeployed(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.ARTIFACT_DEPLOYED
						)
								.setArtifact(new DefaultArtifact("foo-group-id:foo-artifact-id:1.0.0"))
								.setRepository(MavenUtils.createRemoteRepository("foo-id", "https://foo-host"))
								.build()
				)
		);
	}

	@Test
	void testArtifactDeployedInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.artifactDeployed(null));
	}

	@Test
	void testMetadataInvalid() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.metadataInvalid(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.METADATA_INVALID
						)
								.setMetadata(new DefaultMetadata("foo-metadata", Metadata.Nature.RELEASE))
								.setException(new RuntimeException())
								.build()
				)
		);
	}

	@Test
	void testMetadataInvalidInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.metadataInvalid(null));
	}

	@Test
	void testMetadataResolving() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.metadataResolving(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.METADATA_RESOLVING
						)
								.setMetadata(new DefaultMetadata("foo-metadata", Metadata.Nature.RELEASE))
								.setRepository(MavenUtils.createRemoteRepository("foo-id", "https://foo-host"))
								.build()
				)
		);
	}

	@Test
	void testMetadataResolvingInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.metadataResolving(null));
	}

	@Test
	void testMetadataResolved() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.metadataResolved(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.METADATA_RESOLVED
						)
								.setMetadata(new DefaultMetadata("foo-metadata", Metadata.Nature.RELEASE))
								.setRepository(MavenUtils.createRemoteRepository("foo-id", "https://foo-host"))
								.build()
				)
		);
	}

	@Test
	void testMetadataResolvedInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.metadataResolved(null));
	}

	@Test
	void testMetadataDownloading() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.metadataDownloading(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.METADATA_DOWNLOADING
						)
								.setMetadata(new DefaultMetadata("foo-metadata", Metadata.Nature.RELEASE))
								.setRepository(MavenUtils.createRemoteRepository("foo-id", "https://foo-host"))
								.build()
				)
		);
	}

	@Test
	void testMetadataDownloadingInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.metadataDownloading(null));
	}

	@Test
	void testMetadataDownloaded() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.metadataDownloaded(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.METADATA_DOWNLOADED
						)
								.setMetadata(new DefaultMetadata("foo-metadata", Metadata.Nature.RELEASE))
								.setRepository(MavenUtils.createRemoteRepository("foo-id", "https://foo-host"))
								.build()
				)
		);
	}

	@Test
	void testMetadataDownloadedInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.metadataDownloaded(null));
	}

	@Test
	void testMetadataInstalling() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.metadataInstalling(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.METADATA_INSTALLING
						)
								.setMetadata(new DefaultMetadata("foo-metadata", Metadata.Nature.RELEASE))
								.setFile(new File("foo-file"))
								.build()
				)
		);
	}

	@Test
	void testMetadataInstallingInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.metadataInstalling(null));
	}

	@Test
	void testMetadataInstalled() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.metadataInstalled(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.METADATA_INSTALLED
						)
								.setMetadata(new DefaultMetadata("foo-metadata", Metadata.Nature.RELEASE))
								.setFile(new File("foo-file"))
								.build()
				)
		);
	}

	@Test
	void testMetadataInstalledInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.metadataInstalled(null));
	}

	@Test
	void testMetadataDeploying() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.metadataDeploying(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.METADATA_DEPLOYING
						)
								.setMetadata(new DefaultMetadata("foo-metadata", Metadata.Nature.RELEASE))
								.setRepository(MavenUtils.createRemoteRepository("foo-id", "https://foo-host"))
								.build()
				)
		);
	}

	@Test
	void testMetadataDeployingInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.metadataDeploying(null));
	}

	@Test
	void testMetadataDeployed() throws SettingsBuildingException {
		final var settings = MavenUtils.makeSettings();
		assertThatNoException().isThrownBy(
				() -> consoleRepositoryListener.metadataDeployed(
						new RepositoryEvent.Builder(
								MavenUtils.makeRepositorySystemSession(
										settings,
										MavenUtils.makeDecryptedSettings(settings),
										MavenUtils.makeRepositorySystem()
								),
								RepositoryEvent.EventType.METADATA_DEPLOYED
						)
								.setMetadata(new DefaultMetadata("foo-metadata", Metadata.Nature.RELEASE))
								.setRepository(MavenUtils.createRemoteRepository("foo-id", "https://foo-host"))
								.build()
				)
		);
	}

	@Test
	void testMetadataDeployedInvalid() {
		assertThatNullPointerException().isThrownBy(() -> consoleRepositoryListener.metadataDeployed(null));
	}
}