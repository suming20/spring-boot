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

package org.springframework.boot.build.toolchain;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.testing.Test;
import org.gradle.jvm.toolchain.JavaLanguageVersion;
import org.gradle.jvm.toolchain.JavaToolchainService;

/**
 * {@link Plugin} for customizing Gradle's toolchain support.
 *
 * @author Christoph Dreis
 * @author Andy Wilkinson
 */
public class ToolchainPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		configureToolchain(project);
	}

	private void configureToolchain(Project project) {
		ToolchainExtension toolchain = project.getExtensions().create("toolchain", ToolchainExtension.class, project);
		JavaLanguageVersion toolchainVersion = toolchain.getJavaVersion();
		if (toolchainVersion != null) {
			project.afterEvaluate((evaluated) -> configure(evaluated, toolchain));
		}
	}

	private void configure(Project project, ToolchainExtension toolchain) {
		if (!isJavaVersionSupported(toolchain, toolchain.getJavaVersion())) {
			disableToolchainTasks(project);
		}
		else {
			configureTestToolchain(project, toolchain.getJavaVersion());
		}
	}

	private boolean isJavaVersionSupported(ToolchainExtension toolchain, JavaLanguageVersion toolchainVersion) {
		return toolchain.getMaximumCompatibleJavaVersion()
			.map((version) -> version.canCompileOrRun(toolchainVersion))
			.getOrElse(true);
	}

	private void disableToolchainTasks(Project project) {
		project.getTasks().withType(Test.class, (task) -> task.setEnabled(false));
	}

	private void configureTestToolchain(Project project, JavaLanguageVersion toolchainVersion) {
		JavaToolchainService javaToolchains = project.getExtensions().getByType(JavaToolchainService.class);
		project.getTasks()
			.withType(Test.class, (test) -> test.getJavaLauncher()
				.set(javaToolchains.launcherFor((spec) -> spec.getLanguageVersion().set(toolchainVersion))));
	}

}
