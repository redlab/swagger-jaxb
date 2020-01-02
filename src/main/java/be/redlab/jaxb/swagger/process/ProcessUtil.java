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

package be.redlab.jaxb.swagger.process;

import com.sun.codemodel.*;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.outline.EnumConstantOutline;
import com.sun.tools.xjc.outline.EnumOutline;
import io.swagger.annotations.ApiModelProperty;

import java.util.Collection;
import java.util.List;

/**
 * @author redlab
 *
 */
public class ProcessUtil extends AbstractProcessUtil {
	private static final String NOTES = "notes";
	private static final String DATA_TYPE = "dataType";
	private static final String VALUE = "value";
	private static final ProcessUtil myself = new ProcessUtil();

	protected ProcessUtil() {
	}

	static ProcessUtil getInstance() {
		return myself;
	}

	@Override
	public void addAnnotationForField(JDefinedClass implClass, CClassInfo targetClass, JFieldVar jFieldVar, Collection<EnumOutline> enums) {
		addMethodAnnotationForField(implClass, targetClass, jFieldVar, enums);
	}


	void addMethodAnnotationForField(final JDefinedClass implClass, CClassInfo targetClass, final JFieldVar jFieldVar, final Collection<EnumOutline> enums) {
		JMethod jm = getCorrespondingMethod(implClass, jFieldVar.name());
		if (null != jm) {
			addMethodAnnotation(implClass, targetClass, jm, isRequired(jFieldVar), getDefault(jFieldVar), enums);
		}
	}

	@Override
	public void addAnnotationForMethod(JDefinedClass o, CClassInfo t, JMethod m, boolean required, String defaultValue, Collection<EnumOutline> enums) {
		addMethodAnnotation(o, t, m, required, defaultValue,enums);
	}

	/**
	 * Add method level annotation {@link ApiModelProperty} if not already on the method
	 * @param o the ClassOutline
	 * @param t the TargetClass
     * @param m the method to add annotation on
     */
	public void addMethodAnnotation(final JDefinedClass o, CClassInfo t, final JMethod m, final boolean required, final String defaultValue,
                                    final Collection<EnumOutline> enums) {
        if (isAnnotationNotPresent(m)) {
			if (isValidMethod(m, GET)) {
				internalAddMethodAnnotation(t, m, GET, required, defaultValue, enums);
			} else if (isValidMethod(m, IS)) {
				internalAddMethodAnnotation(t, m, IS, required, defaultValue, enums);
			}
		}
	}

	/**
     *
     * @param implClass
     * @param targetClass
     * @param m
     * @param prefix
     * @param required
     * @param defaultValue
     * @param enums
     */
	protected void internalAddMethodAnnotation(final JDefinedClass implClass, CClassInfo targetClass, final JMethod m, final String prefix,
                                               final boolean required,
                                               final String defaultValue, final Collection<EnumOutline> enums) {
		JAnnotationUse apiProperty = m.annotate(ApiModelProperty.class);
		String name = prepareNameFromMethod(m.name(), prefix);
        String description = getDescription(targetClass, name);
		apiProperty.param(VALUE, description);
		EnumOutline eo = getKnownEnum(m.type().fullName(), enums);
		if (null != eo) {
			addAllowableValues(eo, apiProperty);
		}
		if (required) {
			apiProperty.param(REQUIRED, true);
		}
		if (null != defaultValue) {
			apiProperty.param(NOTES, defaultValue);
		}
	}

	/**
	 * @param apiProperty
	 */
	private static void addAllowableValues(final EnumOutline eo, final JAnnotationUse apiProperty) {
		List<EnumConstantOutline> constants = eo.constants;
		StringBuilder b = new StringBuilder();
		int size = constants.size();
		int classNameLength = eo.clazz.fullName().length() + 1;
		for (int i = 0; i < size; i++) {
			b.append(constants.get(i).constRef.getName().substring(classNameLength));
			if (i < size - 1) {
				b.append(',');
			}
		}
		apiProperty.param("allowableValues", b.toString());
	}

	/**
	 * Checks that the given method is valid, meaning that it starts with the given prefix and the prefix is not the
	 * name of the method
	 *
	 * @param m the method
	 * @param prefix the prefix
	 * @return true if valid, false otherwise
	 */
	private boolean isValidMethod(final JMethod m, final String prefix) {
		return m.name().length() > prefix.length() && m.name().startsWith(prefix);
	}

	/**
	 * Create the name for in a {@link ApiModelProperty#value()}
	 *
	 * @param getterName the name of a getter
	 * @param prefix the prefix
	 * @return the name without get and with first character set to lowerCase
	 */
	String prepareNameFromMethod(final String getterName, final String prefix) {
		String name = getterName.substring(prefix.length());
		StringBuilder b = new StringBuilder();
		b.append(Character.toLowerCase(name.charAt(0)));
		if (name.length() > 1) {
			b.append(name.substring(1));
		}
		return b.toString();
	}

}
