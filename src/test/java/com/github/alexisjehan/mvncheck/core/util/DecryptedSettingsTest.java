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
package com.github.alexisjehan.mvncheck.core.util;

import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Server;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class DecryptedSettingsTest {

	private static final List<Proxy> PROXIES = List.of(new Proxy());
	private static final List<Server> SERVERS = List.of(new Server());
	private static final List<Proxy> OTHER_PROXIES = List.of();
	private static final List<Server> OTHER_SERVERS = List.of();

	private final DecryptedSettings decryptedSettings = new DecryptedSettings(PROXIES, SERVERS);

	@Test
	void testConstructorImmutable() {
		final var proxies = new ArrayList<>(PROXIES);
		final var servers = new ArrayList<>(SERVERS);
		final var decryptedSettings = new DecryptedSettings(proxies, servers);
		proxies.clear();
		servers.clear();
		assertThat(decryptedSettings.getProxies()).isEqualTo(PROXIES);
		assertThat(decryptedSettings.getServers()).isEqualTo(SERVERS);
	}

	@Test
	void testConstructorInvalid() {
		assertThatNullPointerException()
				.isThrownBy(() -> new DecryptedSettings(null, SERVERS));
		assertThatNullPointerException()
				.isThrownBy(() -> new DecryptedSettings(Collections.singletonList(null), SERVERS));
		assertThatNullPointerException()
				.isThrownBy(() -> new DecryptedSettings(PROXIES, null));
		assertThatNullPointerException()
				.isThrownBy(() -> new DecryptedSettings(PROXIES, Collections.singletonList(null)));
	}

	@Test
	void testEqualsAndHashCodeAndToString() {
		assertThat(decryptedSettings.equals(decryptedSettings)).isTrue();
		assertThat(decryptedSettings).isNotEqualTo(new Object());
		assertThat(new DecryptedSettings(PROXIES, SERVERS)).satisfies(otherDecryptedSettings -> {
			assertThat(otherDecryptedSettings).isNotSameAs(decryptedSettings);
			assertThat(otherDecryptedSettings).isEqualTo(decryptedSettings);
			assertThat(otherDecryptedSettings).hasSameHashCodeAs(decryptedSettings);
			assertThat(otherDecryptedSettings).hasToString(decryptedSettings.toString());
		});
		assertThat(new DecryptedSettings(OTHER_PROXIES, SERVERS)).satisfies(otherDecryptedSettings -> {
			assertThat(otherDecryptedSettings).isNotSameAs(decryptedSettings);
			assertThat(otherDecryptedSettings).isNotEqualTo(decryptedSettings);
			assertThat(otherDecryptedSettings).doesNotHaveSameHashCodeAs(decryptedSettings);
			assertThat(otherDecryptedSettings).doesNotHaveToString(decryptedSettings.toString());
		});
		assertThat(new DecryptedSettings(PROXIES, OTHER_SERVERS)).satisfies(otherDecryptedSettings -> {
			assertThat(otherDecryptedSettings).isNotSameAs(decryptedSettings);
			assertThat(otherDecryptedSettings).isNotEqualTo(decryptedSettings);
			assertThat(otherDecryptedSettings).doesNotHaveSameHashCodeAs(decryptedSettings);
			assertThat(otherDecryptedSettings).doesNotHaveToString(decryptedSettings.toString());
		});
	}

	@Test
	void testGetProxies() {
		assertThat(decryptedSettings.getProxies()).isEqualTo(PROXIES);
	}

	@Test
	void testGetProxiesImmutable() {
		final var proxies = decryptedSettings.getProxies();
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(proxies::clear);
	}

	@Test
	void testGetServers() {
		assertThat(decryptedSettings.getServers()).isEqualTo(SERVERS);
	}

	@Test
	void testGetServersImmutable() {
		final var servers = decryptedSettings.getServers();
		assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(servers::clear);
	}
}