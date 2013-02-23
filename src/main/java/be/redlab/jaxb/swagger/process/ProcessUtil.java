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
package be.redlab.jaxb.swagger.process;

import javax.xml.bind.annotation.XmlElement;

import be.redlab.jaxb.swagger.DataTypeDeterminationUtil;
import be.redlab.jaxb.swagger.XJCHelper;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.wordnik.swagger.annotations.ApiProperty;

/**
 * @author redlab
 *
 */
public class ProcessUtil {
	private static final String NOTES = "notes";
	private static final String REQUIRED = "required";
	private static final String DATA_TYPE = "dataType";
	private static final String IS = "is";
	private static final String VALUE = "value";
	private static final String GET = "get";
	private static final ProcessUtil myself = new ProcessUtil();

	private ProcessUtil() {
	}

	public static ProcessUtil getInstance() {
		return myself;
	}

	/**
	 * @param mods
	 * @return
	 */
	public boolean validFieldMods(final int mods) {
		if ((mods & JMod.FINAL) != 0 || (mods & JMod.STATIC) != 0
				|| (mods & JMod.ABSTRACT) != 0 || (mods & JMod.NATIVE) != 0 || (mods & JMod.TRANSIENT) != 0
				|| (mods & JMod.VOLATILE) != 0)
			return false;
		return true;

	}

	/**
	 * @param abstractProcessStrategy TODO
	 * @param implClass
	 * @param jFieldVar
	 */
	public void addMethodAnnotationForField(final JDefinedClass implClass, final JFieldVar jFieldVar) {
		JMethod jm = getCorrespondingMethod(implClass, jFieldVar.name());
		if (null != jm) {
			addMethodAnnotation(implClass, jm, isRequired(jFieldVar), getDefault(jFieldVar));
		}
	}

	/**
	 * @param jFieldVar
	 * @return
	 */
	public String getDefault(final JFieldVar jFieldVar) {
		JAnnotationUse annotation = XJCHelper.getAnnotation(jFieldVar.annotations(), XmlElement.class);
		if (null != annotation) {
			return XJCHelper.getStringValueFromAnnotationMember(annotation, "defaultValue");
		}
		return null;
	}

	/**
	 * @param abstractProcessStrategy TODO
	 * @param jFieldVar
	 * @return
	 */
	public boolean isRequired(final JFieldVar jFieldVar) {
		return jFieldVar.type().isPrimitive()
				|| isRequiredByAnnotation(XJCHelper.getAnnotation(jFieldVar.annotations(), XmlElement.class));
	}

	/**
	 * @param annotation
	 * @return
	 */
	public boolean isRequiredByAnnotation(final JAnnotationUse annotation) {
		return null != annotation && "true".equalsIgnoreCase(XJCHelper.getStringValueFromAnnotationMember(annotation, REQUIRED));
	}

	/**
	 * @param implClass
	 * @param key
	 * @return
	 */
	public JMethod getCorrespondingMethod(final JDefinedClass implClass, final String key) {
		StringBuilder b = new StringBuilder(key.substring(0, 1).toUpperCase());
		if (key.length() > 1) {
			b.append(key.substring(1));
		}
		String get = GET + b.toString();
		String is = IS + b.toString();
		for (JMethod m : implClass.methods()) {
			if (get.equals(m.name()) || is.equals(m.name())) {
				return m;
			}
		}
		return null;
	}

	/**
	 * @param jm
	 * @param mods
	 * @return
	 */
	public boolean validMethodMods(final int mods) {
		if (((mods & JMod.PROTECTED) != 0 || (mods & JMod.PRIVATE) != 0 || (mods & JMod.FINAL) != 0 || (mods & JMod.STATIC) != 0
				|| (mods & JMod.ABSTRACT) != 0 || (mods & JMod.NATIVE) != 0 || (mods & JMod.TRANSIENT) != 0 || (mods & JMod.VOLATILE) != 0))
			return false;
		return true;
	}

	/**
	 * Add method level annotation {@link ApiProperty} if not already on the method
	 *
	 * @param o the ClassOutline
	 * @param m the method to add annotation on
	 * @param default
	 * @param required
	 */
	public void addMethodAnnotation(final JDefinedClass o, final JMethod m, final boolean required, final String defaultValue) {
		if (null == XJCHelper.getAnnotation(m.annotations(), ApiProperty.class)) {
			if (isValidMethod(m, GET)) {
				internalAddMethodAnnotation(o, m, GET, required, defaultValue);
			} else if (isValidMethod(m, IS)) {
				internalAddMethodAnnotation(o, m, IS, required, defaultValue);
			}
		}
	}

	/**
	 * @param o
	 * @param m
	 * @param prefix
	 */
	protected void internalAddMethodAnnotation(final JDefinedClass implClass, final JMethod m, final String prefix,
			final boolean required,
			final String defaultValue) {
		JAnnotationUse apiProperty = m.annotate(ApiProperty.class);
		String name = prepareNameFromMethod(m.name(), prefix);
		apiProperty.param(VALUE, name);
		String dataType = DataTypeDeterminationUtil.determineDataType(m.type());
		if (dataType != null) {
			apiProperty.param(DATA_TYPE, dataType);
		}
		if (required) {
			apiProperty.param(REQUIRED, true);
		}
		if (null != defaultValue) {
			apiProperty.param(NOTES, defaultValue);
		}
	}

	/**
	 * Checks that the given method is valid, meaning that it starts with the given prefix and the prefix is not the
	 * name of the method
	 *
	 * @param m the method
	 * @param prefix the prefix
	 * @return true if valid, false otherwise
	 */
	public boolean isValidMethod(final JMethod m, final String prefix) {
		return m.name().length() > prefix.length() && m.name().startsWith(prefix);
	}

	/**
	 * Create the name for in a {@link ApiProperty#value()}
	 *
	 * @param getterName the name of a getter
	 * @param prefix
	 * @return the name without get and with first character set to lowerCase
	 */
	public String prepareNameFromMethod(final String getterName, final String prefix) {
		String name = getterName.substring(prefix.length());
		StringBuilder b = new StringBuilder();
		b.append(Character.toLowerCase(name.charAt(0)));
		if (name.length() > 1) {
			b.append(name.substring(1));
		}
		return b.toString();
	}
}
