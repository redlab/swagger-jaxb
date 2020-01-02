/*
 *  Copyright 2017 Balder Van Camp
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package be.redlab.jaxb.swagger;

import be.redlab.jaxb.swagger.process.FieldProcessStrategy;
import be.redlab.jaxb.swagger.process.NoProcessStrategy;
import be.redlab.jaxb.swagger.process.PropertyProcessStrategy;
import be.redlab.jaxb.swagger.process.PublicMemberProcessStrategy;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.EnumOutline;
import com.sun.tools.xjc.outline.Outline;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
import com.sun.xml.xsom.XSAnnotation;
import io.swagger.annotations.ApiModel;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.bind.annotation.XmlAccessType;
import java.util.Collection;

/**
 * The SwaggerAnnotationsJaxbPlugin adds Swaggers {@link io.swagger.annotations.ApiModel} and {@link io.swagger.annotations.ApiModelProperty} to JAXB Generated classes.
 * Currently only working with Field Accessor annotations.
 *
 *
 * @author redlab
 *
 */
public class SwaggerAnnotationsJaxbPlugin extends Plugin {


	private static final String DESCRIPTION_CLASS = " description generated by jaxb-swagger, hence no class description yet.";
	private static final String WARNING_SKIPPING = "Skipping %s as it is not an implementation or class";
	private static final String DESCRIPTION = "description";
	private static final String SWAGGERIFY = "swaggerify";
	private static final String USAGE = "Add this plugin to the JAXB classes generator classpath and provide the argument '-swaggerify'.";
	private static final String VALUE = "value";
	/**
	 * The optio name to activate swagger annotations.
	 *
	 * @return swaggerify
	 */
	@Override
	public String getOptionName() {
		return SWAGGERIFY;

	}

	/**
	 * A usage description
	 */
	@Override
	public String getUsage() {
		return USAGE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sun.tools.xjc.Plugin#run(com.sun.tools.xjc.outline.Outline, com.sun.tools.xjc.Options,
	 * org.xml.sax.ErrorHandler)
	 *
	 * Api Annotations Info
	 * String value() default "";
	 * String allowableValues() default "";endIndex
	 * String access() default "";
	 * String notes() default "";
	 * String dataType() default "";
	 * boolean required() default false;
	 */
	/**
	 * The run method called by XJC.
	 */
	@Override
	public boolean run(final Outline outline, final Options opt, final ErrorHandler errorHandler) throws SAXException {
		Collection<? extends ClassOutline> classes = outline.getClasses();
		Collection<EnumOutline> enums = outline.getEnums();
		for (ClassOutline o : classes) {
			JDefinedClass implClass = o.implClass;
			if (null != implClass && implClass.isClass() && !implClass.isAbstract() && !implClass.isInterface()
					&& !implClass.isAnnotationTypeDeclaration()) {
				Collection<JAnnotationUse> annotations = implClass.annotations();
				if (!annotations.isEmpty()) {
					XmlAccessType access;
					access = XJCHelper.getAccessType(annotations);
					if (null != access) {
						addClassAnnotation(o);
						getProcessStrategy(access).process(implClass, o.target, enums);
					}
				}
			} else {
				errorHandler.warning(new SAXParseException(String.format(WARNING_SKIPPING, o), null));
			}
		}
		return true;
	}

	/**
	 * Add the class level annotation, {@link io.swagger.annotations.ApiModel}
	 *
	 * @param o the ClassOutline
	 */
    private void addClassAnnotation(final ClassOutline o) {
		JAnnotationUse apiClass = o.implClass.annotate(ApiModel.class);
		String value = o.target.isElement() ? o.target.getElementName().getLocalPart() : o.ref.name();
		apiClass.param(VALUE, value);
		String documentation = getDocumentation(o);
		apiClass.param(DESCRIPTION, (documentation != null) ? documentation : o.ref.fullName() + DESCRIPTION_CLASS);
	}

	/**
	 * Extract value from {@code <xs:annotation><xs:documentation>} if exists.
	 *
	 * @param o the ClassOutline
	 * @return value from {@code <xs:annotation><xs:documentation>} or <code>null</code> if
	 * {@code <xs:annotation><xs:documentation>} does not exists.
	 */
	protected String getDocumentation(final ClassOutline o) {
		XSAnnotation annotation = o.target.getSchemaComponent().getAnnotation();
		if (annotation != null) {
			if (annotation.getAnnotation() instanceof BindInfo) {
				return ((BindInfo) annotation.getAnnotation()).getDocumentation();
			}
		}
		return null;
	}

	protected ProcessStrategy getProcessStrategy(final XmlAccessType access) {
		return SwaggerProcessStrategyFactory.getProcessStrategy(access);
	}

}
