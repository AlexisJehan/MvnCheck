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

import com.github.alexisjehan.javanilla.misc.quality.Ensure;

/**
 * <p>Unchecked {@link Exception} related to <i>Maven</i> sessions.</p>
 * <p><b>Note</b>: This class is {@link java.io.Serializable}.</p>
 * @since 1.0.0
 */
public final class MavenSessionException extends RuntimeException {

	/**
	 * <p>Serial version unique identifier.</p>
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = -2024028478245530224L;

	/**
	 * <p>Constructor.</p>
	 * @param cause a cause
	 * @throws NullPointerException if the cause is {@code null}
	 * @since 1.0.0
	 */
	MavenSessionException(final Throwable cause) {
		super(Ensure.notNull("cause", cause));
	}
}