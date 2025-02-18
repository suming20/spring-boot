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

package org.springframework.boot.actuate.autoconfigure.web.server.tomcat;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for a Tomcat-based management server.
 *
 * @author Moritz Halbritter
 * @since 4.0.0
 */
@ConfigurationProperties("management.server.tomcat")
public class TomcatManagementServerProperties {

	private final Accesslog accesslog = new Accesslog();

	public Accesslog getAccesslog() {
		return this.accesslog;
	}

	public static class Accesslog {

		/**
		 * Management log file name prefix.
		 */
		private String prefix = "management_";

		public String getPrefix() {
			return this.prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}

	}

}
