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

import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JAnnotationValue;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JMethod;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;
import com.wordnik.swagger.annotations.ApiClass;
import com.wordnik.swagger.annotations.ApiProperty;

/**
 * @author redlab
 *
 */
public class SwaggerAnnotationsJaxbPlugin extends Plugin {

	/**
	 *
	 */
	private static final String SWAGGERIFY = "swaggerify";

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sun.tools.xjc.Plugin#getOptionName()
	 */
	@Override
	public String getOptionName() {
		return SWAGGERIFY;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sun.tools.xjc.Plugin#getUsage()
	 */
	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not Implemented");

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sun.tools.xjc.Plugin#run(com.sun.tools.xjc.outline.Outline, com.sun.tools.xjc.Options,
	 * org.xml.sax.ErrorHandler)
	 */
	@Override
	public boolean run(final Outline outline, final Options opt, final ErrorHandler errorHandler) throws SAXException {
		Collection<? extends ClassOutline> classes = outline.getClasses();
		Model model = outline.getModel();
		for (ClassOutline o : classes) {
			if (o.implClass.isClass() && !o.implClass.isAbstract() && !o.implClass.isInterface()
					&& !o.implClass.isAnnotationTypeDeclaration()) {
				JAnnotationUse annotate2 = o.implClass.annotate(ApiClass.class);
				annotate2.param("value", o.ref.fullName());
				StringWriter w = new StringWriter();
				JFormatter f = new JFormatter(w);
				o.ref.javadoc().generate(f);
				annotate2.param("description", w.toString());

				for (JMethod m : o.implClass.methods()) {
					if (m.name().startsWith("get") && m.name().length() > 3) {
						/*
						 * String value() default "";
						 * String allowableValues() default "";endIndex
						 * String access() default "";
						 * String notes() default "";
						 * String dataType() default "";
						 * boolean required() default false;
						 */
						JAnnotationUse annotate = m.annotate(ApiProperty.class);
						String name = m.name().substring(3);
						annotate.param("value", name);
						annotate.param("dataType", m.type().fullName());
						Collection<JAnnotationUse> fieldAnnotations = o.implClass.fields()
								.get(name.substring(0, 1).toLowerCase() + name.substring(1)).annotations();
						for (JAnnotationUse jau : fieldAnnotations) {
							if (jau.getAnnotationClass().name().equals("XmlElement")) {
								Map<String, JAnnotationValue> members = jau.getAnnotationMembers();
								JAnnotationValue value = members.get("defaultValue");
								if (null != value) {
									StringWriter w2 = new StringWriter();
									f = new JFormatter(w2);
									value.generate(f);
									annotate.param("notes", w2.toString());
								}
								value = members.get("required");
								if (null != value) {
									annotate.param("required", true);
								} else {
									annotate.param("required", false);
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

}
