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

package org.springframework.boot.actuate.autoconfigure.web.server.undertow;

import java.util.function.Function;

import org.springframework.boot.actuate.autoconfigure.web.server.AccessLogCustomizer;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory;

/**
 * {@link AccessLogCustomizer} for Undertow.
 *
 * @param <T> the type of factory that can be customized
 * @author Andy Wilkinson
 */
class UndertowAccessLogCustomizer<T extends ConfigurableUndertowWebServerFactory> extends AccessLogCustomizer<T> {

	private final Function<T, String> accessLogPrefixExtractor;

	UndertowAccessLogCustomizer(ManagementServerProperties properties, Function<T, String> accessLogPrefixExtractor) {
		super(properties.getUndertow().getAccesslog().getPrefix());
		this.accessLogPrefixExtractor = accessLogPrefixExtractor;
	}

	@Override
	public void customize(T factory) {
		factory.setAccessLogPrefix(customizePrefix(this.accessLogPrefixExtractor.apply(factory)));
	}

}
