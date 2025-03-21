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

package org.springframework.boot.autoconfigure.web.server.reactive.netty;

import reactor.netty.http.server.HttpServer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.reactor.netty.ReactorNettyConfigurations.ReactorResourceFactoryConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.server.reactive.ReactiveWebServerConfiguration;
import org.springframework.boot.web.server.reactive.ReactiveWebServerFactory;
import org.springframework.boot.web.server.reactive.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.reactive.netty.NettyRouteProvider;
import org.springframework.boot.web.server.reactive.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.client.ReactorResourceFactory;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for a Netty-based reactive web
 * server.
 *
 * @author Andy Wilkinson
 * @since 4.0.0
 */
@AutoConfiguration
@ConditionalOnClass({ ReactiveHttpInputMessage.class, HttpServer.class })
@Import({ ReactiveWebServerConfiguration.class, ReactorResourceFactoryConfiguration.class })
public class NettyReactiveWebServerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(ReactiveWebServerFactory.class)
	NettyReactiveWebServerFactory nettyReactiveWebServerFactory(ReactorResourceFactory resourceFactory,
			ObjectProvider<NettyRouteProvider> routes, ObjectProvider<NettyServerCustomizer> serverCustomizers) {
		NettyReactiveWebServerFactory serverFactory = new NettyReactiveWebServerFactory();
		serverFactory.setResourceFactory(resourceFactory);
		routes.orderedStream().forEach(serverFactory::addRouteProviders);
		serverFactory.getServerCustomizers().addAll(serverCustomizers.orderedStream().toList());
		return serverFactory;
	}

	@Bean
	NettyReactiveWebServerFactoryCustomizer nettyWebServerFactoryCustomizer(Environment environment,
			ServerProperties serverProperties) {
		return new NettyReactiveWebServerFactoryCustomizer(environment, serverProperties);
	}

}
