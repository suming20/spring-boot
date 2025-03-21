/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.web.server.servlet.undertow;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.web.server.undertow.UndertowServerProperties;
import org.springframework.boot.web.server.servlet.undertow.UndertowServletWebServerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link UndertowServletWebServerFactoryCustomizer}
 *
 * @author Andy Wilkinson
 */
class UndertowServletWebServerFactoryCustomizerTests {

	@Test
	void eagerFilterInitCanBeDisabled() {
		UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory(0);
		assertThat(factory.isEagerFilterInit()).isTrue();
		UndertowServerProperties undertowProperties = new UndertowServerProperties();
		undertowProperties.setEagerFilterInit(false);
		new UndertowServletWebServerFactoryCustomizer(undertowProperties).customize(factory);
		assertThat(factory.isEagerFilterInit()).isFalse();
	}

	@Test
	void preservePathOnForwardCanBeEnabled() {
		UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory(0);
		assertThat(factory.isPreservePathOnForward()).isFalse();
		UndertowServerProperties undertowProperties = new UndertowServerProperties();
		undertowProperties.setPreservePathOnForward(true);
		new UndertowServletWebServerFactoryCustomizer(undertowProperties).customize(factory);
		assertThat(factory.isPreservePathOnForward()).isTrue();
	}

}
