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

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.Equals;
import com.github.alexisjehan.javanilla.misc.quality.HashCode;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import com.github.alexisjehan.javanilla.misc.tuple.Pair;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Server;

import java.util.List;

/**
 * <p>Class that describes <i>Maven</i> decrypted settings.</p>
 * <p><b>Note</b>: This class implements its own {@link #equals(Object)}, {@link #hashCode()} and {@link #toString()}
 * methods.</p>
 * @since 1.0.0
 */
final class DecryptedSettings {

	/**
	 * <p>{@link List} of proxies.</p>
	 * @since 1.0.0
	 */
	private final List<Proxy> proxies;

	/**
	 * <p>{@link List} of servers.</p>
	 * @since 1.0.0
	 */
	private final List<Server> servers;

	/**
	 * <p>Constructor.</p>
	 * @param proxies a {@link List} of proxies
	 * @param servers a {@link List} of servers
	 * @throws NullPointerException if the {@link List} of proxies, any of them, the {@link List} of servers or any of
	 *         them is {@code null}
	 * @since 1.0.0
	 */
	DecryptedSettings(final List<Proxy> proxies, final List<Server> servers) {
		Ensure.notNullAndNotNullElements("proxies", proxies);
		Ensure.notNullAndNotNullElements("servers", servers);
		this.proxies = List.copyOf(proxies);
		this.servers = List.copyOf(servers);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof DecryptedSettings)) {
			return false;
		}
		final var other = (DecryptedSettings) object;
		return Equals.equals(proxies, other.proxies)
				&& Equals.equals(servers, other.servers);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public int hashCode() {
		return HashCode.of(
				HashCode.hashCode(proxies),
				HashCode.hashCode(servers)
		);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0
	 */
	@Override
	public String toString() {
		return ToString.of(
				this,
				Pair.of("proxies", ToString.toString(proxies)),
				Pair.of("servers", ToString.toString(servers))
		);
	}

	/**
	 * <p>Get the {@link List} of proxies.</p>
	 * @return the {@link List} of proxies
	 * @since 1.0.0
	 */
	List<Proxy> getProxies() {
		return proxies;
	}

	/**
	 * <p>Get the {@link List} of servers.</p>
	 * @return the {@link List} of servers
	 * @since 1.0.0
	 */
	List<Server> getServers() {
		return servers;
	}
}