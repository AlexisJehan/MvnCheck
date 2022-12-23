/*
 * MIT License
 *
 * Copyright (c) 2022 Alexis Jehan
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
package com.github.alexisjehan.mavencheck.core.component.artifact.type;

import com.github.alexisjehan.javanilla.misc.CaseStyle;

import java.util.Set;

/**
 * <p>Enumeration of <i>Gradle</i> artifact types.</p>
 * <p><b>Note</b>: Based on {@code org.gradle:gradle-tooling-api:7.6}.</p>
 * @see <a href="https://docs.gradle.org/current/userguide/java_plugin.html#sec:java_plugin_and_dependency_management">https://docs.gradle.org/current/userguide/java_plugin.html#sec:java_plugin_and_dependency_management</a>
 * @see <a href="https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_configurations_graph">https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_configurations_graph</a>
 * @since 1.0.0
 */
public enum GradleArtifactType implements ArtifactType {

	/**
	 * <p>Annotation processors and their dependency artifacts.</p>
	 * <p><b>Note</b>: Added since <i>Gradle 4.6</i>.</p>
	 * @since 1.0.0
	 */
	ANNOTATION_PROCESSOR,

	/**
	 * <p>API dependency artifacts.</p>
	 * @since 1.0.0
	 */
	API,

	/**
	 * <p>Dependency artifacts.</p>
	 * <p><b>Note</b>: Deprecated since <i>Gradle 4.7</i>.</p>
	 * <p><b>Note</b>: Removed since <i>Gradle 7.0</i> (Superseded by {@link IMPLEMENTATION}).</p>
	 * @since 1.0.0
	 */
	COMPILE(Property.DEPRECATED),

	/**
	 * <p>Compile classpath artifacts.</p>
	 * @since 1.0.0
	 */
	COMPILE_CLASSPATH(Property.CLASSPATH),

	/**
	 * <p>Compile only dependency artifacts.</p>
	 * @since 1.0.0
	 */
	COMPILE_ONLY,

	/**
	 * <p>Compile only API dependency artifacts.</p>
	 * <p><b>Note</b>: Added since <i>Gradle 6.7</i>.</p>
	 * @since 1.0.0
	 */
	COMPILE_ONLY_API,

	/**
	 * <p>Implementation only dependency artifacts.</p>
	 * @since 1.0.0
	 */
	IMPLEMENTATION,

	/**
	 * <p>Runtime dependency artifacts.</p>
	 * <p><b>Note</b>: Deprecated since <i>Gradle 4.7</i>.</p>
	 * <p><b>Note</b>: Removed since <i>Gradle 7.0</i> (Superseded by {@link RUNTIME_ONLY}).</p>
	 * @since 1.0.0
	 */
	RUNTIME(Property.DEPRECATED),

	/**
	 * <p>Runtime classpath artifacts.</p>
	 * @since 1.0.0
	 */
	RUNTIME_CLASSPATH(Property.CLASSPATH),

	/**
	 * <p>Runtime only dependency artifacts.</p>
	 * @since 1.0.0
	 */
	RUNTIME_ONLY,

	/**
	 * <p>Test annotation processors and their dependency artifacts.</p>
	 * <p><b>Note</b>: Added since <i>Gradle 4.6</i>.</p>
	 * @since 1.0.0
	 */
	TEST_ANNOTATION_PROCESSOR,

	/**
	 * <p>Test dependency artifacts.</p>
	 * <p><b>Note</b>: Deprecated since <i>Gradle 4.7</i>.</p>
	 * <p><b>Note</b>: Removed since <i>Gradle 7.0</i> (Superseded by {@link TEST_IMPLEMENTATION}).</p>
	 * @since 1.0.0
	 */
	TEST_COMPILE(Property.DEPRECATED),

	/**
	 * <p>Test compile classpath artifacts.</p>
	 * @since 1.0.0
	 */
	TEST_COMPILE_CLASSPATH(Property.CLASSPATH),

	/**
	 * <p>Test compile only dependency artifacts.</p>
	 * @since 1.0.0
	 */
	TEST_COMPILE_ONLY,

	/**
	 * <p>Test implementation only dependency artifacts.</p>
	 * @since 1.0.0
	 */
	TEST_IMPLEMENTATION,

	/**
	 * <p>Test runtime dependency artifacts.</p>
	 * <p><b>Note</b>: Deprecated since <i>Gradle 4.7</i>.</p>
	 * <p><b>Note</b>: Removed since <i>Gradle 7.0</i> (Superseded by {@link TEST_RUNTIME_ONLY}).</p>
	 * @since 1.0.0
	 */
	TEST_RUNTIME(Property.DEPRECATED),

	/**
	 * <p>Test runtime classpath artifacts.</p>
	 * @since 1.0.0
	 */
	TEST_RUNTIME_CLASSPATH(Property.CLASSPATH),

	/**
	 * <p>Test runtime only dependency artifacts.</p>
	 * @since 1.0.0
	 */
	TEST_RUNTIME_ONLY;

	/**
	 * <p>Enumeration of properties.</p>
	 * @since 1.0.0
	 */
	private enum Property {

		/**
		 * <p>Flagged as a classpath artifact type.</p>
		 * @since 1.0.0
		 */
		CLASSPATH,

		/**
		 * <p>Flagged as a deprecated artifact type.</p>
		 * @since 1.0.0
		 */
		DEPRECATED
	}

	/**
	 * <p>{@link Set} of properties.</p>
	 * @since 1.0.0
	 */
	private final Set<Property> properties;

	/**
	 * <p>Constructor.</p>
	 * @param properties an array of properties
	 * @since 1.0.0
	 */
	GradleArtifactType(final Property... properties) {
		this.properties = Set.of(properties);
	}

	/**
	 * <p>Return the name printed by the <i>Gradle</i> {@code :dependencies} task.</p>
	 * @return the name
	 * @since 1.0.0
	 */
	public final String dependenciesTaskName() {
		return CaseStyle.CAMEL.of(name(), CaseStyle.MACRO);
	}

	/**
	 * <p>Get if this is a classpath artifact type.</p>
	 * @return {@code true} if this is a classpath artifact type
	 * @since 1.0.0
	 */
	public final boolean isClasspath() {
		return properties.contains(Property.CLASSPATH);
	}

	/**
	 * <p>Get if this is a deprecated artifact type.</p>
	 * @return {@code true} if this is a deprecated artifact type
	 * @since 1.0.0
	 */
	public final boolean isDeprecated() {
		return properties.contains(Property.DEPRECATED);
	}
}