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
package com.github.alexisjehan.mvncheck.core.component.filter.version.factory;

import com.github.alexisjehan.javanilla.lang.Strings;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

final class ReleaseVersionFilterFactoryTest {

	private final ReleaseVersionFilterFactory releaseVersionFilterFactory = new ReleaseVersionFilterFactory();

	@Test
	void testCreateInvalid() {
		assertThatNullPointerException().isThrownBy(() -> releaseVersionFilterFactory.create(null));
		assertThatIllegalArgumentException().isThrownBy(() -> releaseVersionFilterFactory.create(Strings.EMPTY));
	}

	@Test
	void testCreateAccept() {
		final var releaseVersionFilter = releaseVersionFilterFactory.create("1.0.0");

		// No qualifier
		assertThat(releaseVersionFilter.accept("1.2.3")).isTrue();

		// Any qualifier
		assertThat(releaseVersionFilter.accept("1.2.3foo")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3foo123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3foo.123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3foo-123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3FOO")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3FOO123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3FOO.123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3FOO-123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3.foo")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3.foo123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3.foo.123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3.foo-123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3.FOO")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3.FOO123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3.FOO.123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3.FOO-123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3-foo")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3-foo123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3-foo.123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3-foo-123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3-FOO")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3-FOO123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3-FOO.123")).isTrue();
		assertThat(releaseVersionFilter.accept("1.2.3-FOO-123")).isTrue();

		// Alpha qualifier
		assertThat(releaseVersionFilter.accept("1.2.3alpha")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3alpha123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3alpha.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3alpha-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3ALPHA")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3ALPHA123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3ALPHA.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3ALPHA-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.alpha")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.alpha123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.alpha.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.alpha-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.ALPHA")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.ALPHA123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.ALPHA.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.ALPHA-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-alpha")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-alpha123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-alpha.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-alpha-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-ALPHA")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-ALPHA123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-ALPHA.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-ALPHA-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3a")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3a123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3a.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3a-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3A")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3A123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3A.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3A-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.a")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.a123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.a.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.a-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.A")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.A123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.A.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.A-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-a")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-a123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-a.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-a-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-A")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-A123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-A.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-A-123")).isFalse();

		// Beta qualifier
		assertThat(releaseVersionFilter.accept("1.2.3beta")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3beta123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3beta.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3beta-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3BETA")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3BETA123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3BETA.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3BETA-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.beta")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.beta123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.beta.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.beta-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.BETA")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.BETA123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.BETA.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.BETA-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-beta")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-beta123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-beta.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-beta-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-BETA")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-BETA123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-BETA.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-BETA-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3b")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3b123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3b.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3b-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3B")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3B123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3B.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3B-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.b")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.b123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.b.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.b-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.B")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.B123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.B.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.B-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-b")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-b123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-b.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-b-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-B")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-B123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-B.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-B-123")).isFalse();

		// Milestone qualifier
		assertThat(releaseVersionFilter.accept("1.2.3milestone")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3milestone123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3milestone.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3milestone-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3MILESTONE")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3MILESTONE123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3MILESTONE.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3MILESTONE-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.milestone")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.milestone123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.milestone.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.milestone-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.MILESTONE")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.MILESTONE123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.MILESTONE.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.MILESTONE-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-milestone")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-milestone123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-milestone.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-milestone-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-MILESTONE")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-MILESTONE123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-MILESTONE.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-MILESTONE-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3m")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3m123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3m.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3m-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3M")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3M123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3M.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3M-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.m")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.m123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.m.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.m-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.M")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.M123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.M.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.M-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-m")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-m123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-m.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-m-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-M")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-M123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-M.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-M-123")).isFalse();

		// Release candidate qualifier
		assertThat(releaseVersionFilter.accept("1.2.3rc")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3rc123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3rc.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3rc-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3RC")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3RC123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3RC.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3RC-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.rc")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.rc123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.rc.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.rc-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.RC")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.RC123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.RC.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.RC-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-rc")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-rc123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-rc.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-rc-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-RC")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-RC123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-RC.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-RC-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3cr")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3cr123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3cr.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3cr-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3CR")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3CR123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3CR.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3CR-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.cr")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.cr123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.cr.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.cr-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.CR")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.CR123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.CR.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.CR-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-cr")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-cr123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-cr.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-cr-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-CR")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-CR123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-CR.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-CR-123")).isFalse();

		// Snapshot qualifier
		assertThat(releaseVersionFilter.accept("1.2.3snapshot")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3snapshot123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3snapshot.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3snapshot-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3SNAPSHOT")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3SNAPSHOT123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3SNAPSHOT.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3SNAPSHOT-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.snapshot")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.snapshot123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.snapshot.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.snapshot-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.SNAPSHOT")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.SNAPSHOT123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.SNAPSHOT.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3.SNAPSHOT-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-snapshot")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-snapshot123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-snapshot.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-snapshot-123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-SNAPSHOT")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-SNAPSHOT123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-SNAPSHOT.123")).isFalse();
		assertThat(releaseVersionFilter.accept("1.2.3-SNAPSHOT-123")).isFalse();
	}

	@Test
	void testCreateAcceptInvalid() {
		final var releaseVersionFilter = releaseVersionFilterFactory.create("1.0.0");
		assertThatNullPointerException().isThrownBy(() -> releaseVersionFilter.accept(null));
		assertThatIllegalArgumentException().isThrownBy(() -> releaseVersionFilter.accept(Strings.EMPTY));
	}
}