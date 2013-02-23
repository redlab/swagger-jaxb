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

import java.io.StringWriter;
import java.util.Map;
import java.util.logging.Logger;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JAnnotationValue;
import com.sun.codemodel.JFormatter;
import com.sun.xml.txw2.annotation.XmlElement;
import com.wordnik.swagger.annotations.ApiProperty;

/**
 * Processes {@link XmlElement} annotations
 *
 * @author redlab
 *
 */
public class XmlElementProcessor implements Processor {
	private static final Logger LOG = Logger.getLogger(XmlElementProcessor.class.getName());
	private final JAnnotationUse jau;

	/**
	 * The {@link ApiProperty} annotation
	 *
	 * @param jau
	 */
	public XmlElementProcessor(final JAnnotationUse jau) {
		this.jau = jau;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see be.redlab.jaxb.swagger.Processor#process(com.sun.codemodel.JAnnotationUse)
	 */
	public void process(final JAnnotationUse apiAnnotation, final boolean isPrimitive) {
		Map<String, JAnnotationValue> members = jau.getAnnotationMembers();
		JAnnotationValue value = members.get("required");
		if (isPrimitive || null != value) {
			StringWriter w2 = new StringWriter();
			JFormatter f = new JFormatter(w2);
			value.generate(f);
			LOG.finest("processed to " + w2.toString());
			apiAnnotation.param("required", true);
		} else {
			apiAnnotation.param("required", false);
		}
		value = members.get("defaultValue");
		if (null != value) {
			StringWriter w2 = new StringWriter();
			JFormatter f = new JFormatter(w2);
			value.generate(f);
			apiAnnotation.param("notes", w2.toString());
		}
	}

}
