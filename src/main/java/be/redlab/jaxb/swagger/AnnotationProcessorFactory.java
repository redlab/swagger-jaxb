/*
 * Copyright 2013 Balder Van Camp
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

import com.sun.codemodel.JAnnotationUse;

/**
 * @author redlab
 *
 */
public class AnnotationProcessorFactory {

	/**
	 * @param jau
	 * @return
	 */
	public static Processor findProcessor(final JAnnotationUse jau) {
		if (jau.getAnnotationClass().name().equals("XmlElement")) {
			return new XmlElementProcessor(jau);
		}
		return new Processor() {
			public void process(final JAnnotationUse apiAnnotation) {
			}
		};
	}

}
