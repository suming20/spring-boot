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

package org.springframework.boot.actuate.web.mappings.servlet;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import jakarta.servlet.ServletException;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.core.StandardWrapper;

import org.springframework.boot.tomcat.TomcatWebServer;
import org.springframework.boot.undertow.servlet.UndertowServletWebServer;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;

/**
 * {@code DispatcherServletHandlerMappings} provides access to a {@link DispatcherServlet
 * DispatcherServlet's} handler mappings, triggering initialization of the dispatcher
 * servlet if necessary.
 *
 * @author Andy Wilkinson
 */
final class DispatcherServletHandlerMappings {

	private static final boolean TOMCAT_WEB_SERVER_PRESENT = ClassUtils.isPresent(
			"org.springframework.boot.tomcat.TomcatWebServer", DispatcherServletHandlerMappings.class.getClassLoader());

	private static final boolean UNDERTOW_WEB_SERVER_PRESENT = ClassUtils.isPresent(
			"org.springframework.boot.undertow.UndertowWebServer",
			DispatcherServletHandlerMappings.class.getClassLoader());

	private final String name;

	private final DispatcherServlet dispatcherServlet;

	private final WebApplicationContext applicationContext;

	DispatcherServletHandlerMappings(String name, DispatcherServlet dispatcherServlet,
			WebApplicationContext applicationContext) {
		this.name = name;
		this.dispatcherServlet = dispatcherServlet;
		this.applicationContext = applicationContext;
	}

	List<HandlerMapping> getHandlerMappings() {
		List<HandlerMapping> handlerMappings = this.dispatcherServlet.getHandlerMappings();
		if (handlerMappings == null) {
			initializeDispatcherServletIfPossible();
			handlerMappings = this.dispatcherServlet.getHandlerMappings();
		}
		return (handlerMappings != null) ? handlerMappings : Collections.emptyList();
	}

	private void initializeDispatcherServletIfPossible() {
		if (!(this.applicationContext instanceof ServletWebServerApplicationContext webServerApplicationContext)) {
			return;
		}
		WebServer webServer = webServerApplicationContext.getWebServer();
		if (UNDERTOW_WEB_SERVER_PRESENT && webServer instanceof UndertowServletWebServer undertowServletWebServer) {
			new UndertowServletInitializer(undertowServletWebServer).initializeServlet(this.name);
		}
		else if (TOMCAT_WEB_SERVER_PRESENT && webServer instanceof TomcatWebServer tomcatWebServer) {
			new TomcatServletInitializer(tomcatWebServer).initializeServlet(this.name);
		}
	}

	String getName() {
		return this.name;
	}

	private static final class TomcatServletInitializer {

		private final TomcatWebServer webServer;

		private TomcatServletInitializer(TomcatWebServer webServer) {
			this.webServer = webServer;
		}

		void initializeServlet(String name) {
			findContext().ifPresent((context) -> initializeServlet(context, name));
		}

		private Optional<Context> findContext() {
			return Stream.of(this.webServer.getTomcat().getHost().findChildren())
				.filter(Context.class::isInstance)
				.map(Context.class::cast)
				.findFirst();
		}

		private void initializeServlet(Context context, String name) {
			Container child = context.findChild(name);
			if (child instanceof StandardWrapper wrapper) {
				try {
					wrapper.deallocate(wrapper.allocate());
				}
				catch (ServletException ex) {
					// Continue
				}
			}
		}

	}

	private static final class UndertowServletInitializer {

		private final UndertowServletWebServer webServer;

		private UndertowServletInitializer(UndertowServletWebServer webServer) {
			this.webServer = webServer;
		}

		void initializeServlet(String name) {
			try {
				this.webServer.getDeploymentManager().getDeployment().getServlets().getManagedServlet(name).forceInit();
			}
			catch (ServletException ex) {
				// Continue
			}
		}

	}

}
