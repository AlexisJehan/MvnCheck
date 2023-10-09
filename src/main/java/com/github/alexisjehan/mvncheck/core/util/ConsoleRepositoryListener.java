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

import com.github.alexisjehan.javanilla.misc.quality.Ensure;
import com.github.alexisjehan.javanilla.misc.quality.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.aether.RepositoryEvent;
import org.eclipse.aether.RepositoryListener;

import java.lang.invoke.MethodHandles;

/**
 * <p>Class that describes a <i>Maven</i> console repository listener.</p>
 * @deprecated since 1.3.0, don't use anymore
 * @since 1.0.0
 */
@Deprecated(since = "1.3.0")
final class ConsoleRepositoryListener implements RepositoryListener {

	/**
	 * <p>Logger.</p>
	 * @since 1.0.0
	 */
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void artifactDescriptorInvalid(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.warn(
				"Invalid {} artifact descriptor: {}",
				() -> ToString.toString(event.getArtifact()),
				() -> ToString.toString(event.getException())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void artifactDescriptorMissing(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.warn(
				"Missing {} artifact descriptor",
				() -> ToString.toString(event.getArtifact())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void artifactResolving(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.trace(
				"Resolving {} artifact from {}",
				() -> ToString.toString(event.getArtifact()),
				() -> ToString.toString(event.getRepository())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void artifactResolved(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.debug(
				"Resolved {} artifact from {}",
				() -> ToString.toString(event.getArtifact()),
				() -> ToString.toString(event.getRepository())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void artifactDownloading(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.trace(
				"Downloading {} artifact from {}",
				() -> ToString.toString(event.getArtifact()),
				() -> ToString.toString(event.getRepository())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void artifactDownloaded(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.debug(
				"Downloaded {} artifact from {}",
				() -> ToString.toString(event.getArtifact()),
				() -> ToString.toString(event.getRepository())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void artifactInstalling(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.trace(
				"Installing {} artifact to {}",
				() -> ToString.toString(event.getArtifact()),
				() -> ToString.toString(event.getFile())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void artifactInstalled(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.debug(
				"Installed {} artifact to {}",
				() -> ToString.toString(event.getArtifact()),
				() -> ToString.toString(event.getFile())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void artifactDeploying(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.trace(
				"Deploying {} artifact to {}",
				() -> ToString.toString(event.getArtifact()),
				() -> ToString.toString(event.getRepository())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void artifactDeployed(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.debug(
				"Deployed {} artifact to {}",
				() -> ToString.toString(event.getArtifact()),
				() -> ToString.toString(event.getRepository())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void metadataInvalid(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.warn(
				"Invalid {} metadata: {}",
				() -> ToString.toString(event.getMetadata()),
				() -> ToString.toString(event.getException())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void metadataResolving(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.trace(
				"Resolving {} metadata from {}",
				() -> ToString.toString(event.getMetadata()),
				() -> ToString.toString(event.getRepository())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void metadataResolved(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.debug(
				"Resolved {} metadata from {}",
				() -> ToString.toString(event.getMetadata()),
				() -> ToString.toString(event.getRepository())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void metadataDownloading(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.trace(
				"Downloading {} metadata from {}",
				() -> ToString.toString(event.getMetadata()),
				() -> ToString.toString(event.getRepository())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void metadataDownloaded(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.debug(
				"Downloaded {} metadata from {}",
				() -> ToString.toString(event.getMetadata()),
				() -> ToString.toString(event.getRepository())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void metadataInstalling(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.trace(
				"Installing {} metadata to {}",
				() -> ToString.toString(event.getMetadata()),
				() -> ToString.toString(event.getFile())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void metadataInstalled(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.debug(
				"Installed {} metadata to {}",
				() -> ToString.toString(event.getMetadata()),
				() -> ToString.toString(event.getFile())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void metadataDeploying(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.trace(
				"Deploying {} metadata to {}",
				() -> ToString.toString(event.getMetadata()),
				() -> ToString.toString(event.getRepository())
		);
	}

	/**
	 * {@inheritDoc}
	 * @throws NullPointerException if the event is {@code null}
	 * @since 1.0.0
	 */
	@Override
	public void metadataDeployed(final RepositoryEvent event) {
		Ensure.notNull("event", event);
		logger.debug(
				"Deployed {} metadata to {}",
				() -> ToString.toString(event.getMetadata()),
				() -> ToString.toString(event.getRepository())
		);
	}
}