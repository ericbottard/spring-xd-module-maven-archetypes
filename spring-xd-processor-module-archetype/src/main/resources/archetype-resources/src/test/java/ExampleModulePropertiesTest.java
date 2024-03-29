/*
 * Copyright 2014 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package $package;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.xd.module.ModuleType.*;

import org.hamcrest.Matcher;
import org.junit.Test;

import org.springframework.xd.module.ModuleDefinition;
import org.springframework.xd.module.ModuleDefinitions;
import org.springframework.xd.module.options.DefaultModuleOptionsMetadataResolver;
import org.springframework.xd.module.options.ModuleOption;
import org.springframework.xd.module.options.ModuleOptionsMetadata;
import org.springframework.xd.module.options.ModuleOptionsMetadataResolver;


/**
 * Tests expected module properties are defined.
 */
public class ExampleModulePropertiesTest {

	private String moduleName = "$artifactId";

	@Test
	public void testModuleProperties() {
		ModuleOptionsMetadataResolver moduleOptionsMetadataResolver = new DefaultModuleOptionsMetadataResolver();
		String resource =
				"classpath:/module/processor/" + moduleName + "/";
		ModuleDefinition definition = ModuleDefinitions.simple(moduleName, processor, resource);
		ModuleOptionsMetadata metadata = moduleOptionsMetadataResolver.resolve(definition);

		assertThat(
				metadata,
				containsInAnyOrder(moduleOptionNamed("prefix"), moduleOptionNamed("suffix"), moduleOptionNamed("prefixOnly")));
	}

	public static Matcher<ModuleOption> moduleOptionNamed(String name) {
		return hasProperty("name", equalTo(name));
	}
}
