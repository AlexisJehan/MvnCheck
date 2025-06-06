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
package com.github.alexisjehan.mvncheck;

import java.lang.invoke.MethodHandles;

/**
 * Class that holds constants.
 * @since 1.4.0
 */
final class Constants {

	/**
	 * Name.
	 * @since 1.4.0
	 */
	static final String NAME = MethodHandles.lookup().lookupClass().getPackage().getImplementationTitle();

	/**
	 * Version.
	 * @since 1.4.0
	 */
	static final String VERSION = MethodHandles.lookup().lookupClass().getPackage().getImplementationVersion();

	/**
	 * <i>GitHub</i> owner name.
	 * @since 1.4.0
	 */
	static final String GITHUB_OWNER_NAME = "AlexisJehan";

	/**
	 * <i>GitHub</i> repository name.
	 * @since 1.4.0
	 */
	static final String GITHUB_REPOSITORY_NAME = "MvnCheck";

	/**
	 * Constructor.
	 * @since 1.4.0
	 */
	private Constants() {}
}