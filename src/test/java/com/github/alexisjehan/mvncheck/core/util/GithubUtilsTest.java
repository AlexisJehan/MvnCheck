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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kohsuke.github.GHRateLimit;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@ExtendWith(MockitoExtension.class)
final class GithubUtilsTest {

	@Mock
	private GitHub mockedGithub;

	@Mock
	private GHRateLimit mockedRateLimit;

	@Mock
	private GHRepository mockedRepository;

	@Mock
	private GHRelease mockedRelease;

	@Test
	void testRetrieveOptionalLatestReleaseName() throws IOException {
		Mockito.when(mockedGithub.getRateLimit())
				.thenReturn(mockedRateLimit);
		Mockito.when(mockedGithub.getRepository(Mockito.notNull()))
				.thenReturn(mockedRepository);
		Mockito.when(mockedRateLimit.getRemaining())
				.thenReturn(10);
		Mockito.when(mockedRepository.getLatestRelease())
				.thenReturn(mockedRelease);
		Mockito.when(mockedRelease.getName())
				.thenReturn("foo-release-name");
		try (var mockedStaticGithub = Mockito.mockStatic(GitHub.class)) {
			mockedStaticGithub.when(GitHub::connectAnonymously)
					.thenReturn(mockedGithub);
			assertThat(
					GithubUtils.retrieveOptionalLatestReleaseName(
							"foo-owner-name",
							"foo-repository-name"
					)
			).contains("foo-release-name");

			// Repository with no release
			Mockito.when(mockedRepository.getLatestRelease())
					.thenReturn(null);
			assertThat(
					GithubUtils.retrieveOptionalLatestReleaseName(
							"foo-owner-name",
							"foo-repository-name"
					)
			).isEmpty();

			// Non-existent repository
			Mockito.when(mockedGithub.getRepository(Mockito.notNull()))
					.thenThrow(IOException.class);
			assertThat(
					GithubUtils.retrieveOptionalLatestReleaseName(
							"foo-owner-name",
							"foo-repository-name"
					)
			).isEmpty();

			// API rate limit reach
			Mockito.when(mockedRateLimit.getRemaining())
					.thenReturn(0);
			assertThat(
					GithubUtils.retrieveOptionalLatestReleaseName(
							"foo-owner-name",
							"foo-repository-name"
					)
			).isEmpty();
		}
	}

	@Test
	void testRetrieveOptionalLatestReleaseNameInvalid() {
		assertThatNullPointerException().isThrownBy(
				() -> GithubUtils.retrieveOptionalLatestReleaseName(null, "foo-repository-name")
		);
		assertThatIllegalArgumentException().isThrownBy(
				() -> GithubUtils.retrieveOptionalLatestReleaseName(Strings.EMPTY, "foo-repository-name")
		);
		assertThatNullPointerException().isThrownBy(
				() -> GithubUtils.retrieveOptionalLatestReleaseName("foo-owner-name", null)
		);
		assertThatIllegalArgumentException().isThrownBy(
				() -> GithubUtils.retrieveOptionalLatestReleaseName("foo-owner-name", Strings.EMPTY)
		);
	}
}