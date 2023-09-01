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
package com.github.alexisjehan.mavencheck.core.component.session;

import com.github.alexisjehan.mavencheck.core.util.MavenUtils;
import org.apache.maven.settings.building.SettingsBuildingException;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@ExtendWith(MockitoExtension.class)
final class MavenSessionTest {

	@Mock
	private RepositorySystem mockedRepositorySystem;

	@Test
	void testConstructorInvalid() {
		try (var mockedStaticMavenUtils = Mockito.mockStatic(MavenUtils.class)) {
			mockedStaticMavenUtils.when(MavenUtils::makeSettings)
					.thenThrow(SettingsBuildingException.class);
			assertThatExceptionOfType(MavenSessionException.class)
					.isThrownBy(MavenSession::new)
					.withCauseInstanceOf(SettingsBuildingException.class);
		}
	}

	@Test
	void testResolve() {
		final var remoteRepositories = List.of(MavenUtils.createRemoteRepository("foo-id", "https://foo-host"));
		Mockito.when(mockedRepositorySystem.newResolutionRepositories(Mockito.any(), Mockito.any()))
				.thenReturn(remoteRepositories);
		try (var mockedStaticMavenUtils = Mockito.mockStatic(MavenUtils.class)) {
			mockedStaticMavenUtils.when(MavenUtils::makeRepositorySystem)
					.thenReturn(mockedRepositorySystem);
			final var mavenSession = new MavenSession();
			assertThat(mavenSession.resolve(List.of())).isSameAs(remoteRepositories);
		}
	}

	@Test
	void testResolveInvalid() {
		final var mavenSession = new MavenSession();
		assertThatNullPointerException().isThrownBy(() -> mavenSession.resolve(null));
		assertThatNullPointerException().isThrownBy(() -> mavenSession.resolve(Collections.singletonList(null)));
	}

	@Test
	void testRequest() throws VersionRangeResolutionException {
		final var versionRangeResult = new VersionRangeResult(new VersionRangeRequest());
		Mockito.when(mockedRepositorySystem.resolveVersionRange(Mockito.any(), Mockito.any()))
				.thenReturn(versionRangeResult);
		try (var mockedStaticMavenUtils = Mockito.mockStatic(MavenUtils.class)) {
			mockedStaticMavenUtils.when(MavenUtils::makeRepositorySystem)
					.thenReturn(mockedRepositorySystem);
			final var mavenSession = new MavenSession();
			assertThat(mavenSession.request(new VersionRangeRequest())).isSameAs(versionRangeResult);
		}
	}

	@Test
	void testRequestInvalid() {
		final var mavenSession = new MavenSession();
		assertThatNullPointerException().isThrownBy(() -> mavenSession.request(null));
	}

	@Test
	void testGetModelResolver() {
		final var mavenSession = new MavenSession();
		assertThat(mavenSession.getModelResolver()).isNotNull();
	}

	@Test
	void testGetModelCache() {
		final var mavenSession = new MavenSession();
		assertThat(mavenSession.getModelCache()).isNotNull();
	}
}