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
package com.github.alexisjehan.mvncheck.core.component.build.resolver;

import com.github.alexisjehan.mvncheck.core.component.build.Build;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFile;
import com.github.alexisjehan.mvncheck.core.component.build.file.BuildFileType;

import java.util.Set;

/**
 * Interface that describes a resolver of the build for a file.
 * @since 1.0.0
 */
public interface BuildResolver {

	/**
	 * Resolve the build for a file.
	 * @param file a file
	 * @return the build
	 * @throws BuildResolveException might occur while resolving the build
	 * @since 1.0.0
	 */
	Build resolve(BuildFile file);

	/**
	 * Get the {@link Set} of file types.
	 * @return the {@link Set} of file types
	 * @since 1.0.0
	 */
	Set<BuildFileType> getFileTypes();
}