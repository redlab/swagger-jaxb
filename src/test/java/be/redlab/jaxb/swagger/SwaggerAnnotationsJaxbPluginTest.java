/*
 * Copyright 2012 Balder Van Camp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package be.redlab.jaxb.swagger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
/**
 * @author redlab
 *
 */
public class SwaggerAnnotationsJaxbPluginTest {

	private SwaggerAnnotationsJaxbPlugin plugin;

	@Before
	public void setup() {
		plugin = new SwaggerAnnotationsJaxbPlugin();

	}

	@Test
	public void optionName() {
		Assert.assertEquals("swaggerify", plugin.getOptionName());
	}
}
