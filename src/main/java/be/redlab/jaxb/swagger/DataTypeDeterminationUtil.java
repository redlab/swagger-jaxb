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

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import com.sun.codemodel.JType;

/**
 * Helper that determines what type of Swagger Type description should be used for which Java type.
 *
 * @author redlab
 *
 */
public class DataTypeDeterminationUtil {

	/**
	 * Supported Swagger Type Description
	 * <hr />
	 * <ul>
	 * <li>byte</li>
	 * <li>boolean</li>
	 * <li>int</li>
	 * <li>long</li>
	 * <li>float</li>
	 * <li>double</li>
	 * <li>string</li>
	 * <li>Date a ISO-8601 Date, which is t in a String (1970-01-01T00:00:00.000+0000)</li>
	 * </ul>
	 * Currently supports all primitives and most of their wrapper classes, arrays. enums are returned as string.
	 * @param apiProperty TODO
	 */
	public static String setDataType(final JType jType) {
		String newName = jType.name();
		String fullName = jType.fullName();
		if (jType.isArray()) {
			newName = "Array";
		} else if (jType.isPrimitive()) {
			if (fullName.equals("short")) {
				newName = "int";
			} else {
				newName = fullName;
			}
		} else {
			// TODO change to switch once we only support java 1.7
			if (fullName.equals(String.class.getName())) {
				newName = "string";
			} else if (fullName.equals(Integer.class.getName())) {
				newName = "int";
			} else if (fullName.equals(BigInteger.class.getName()) || fullName.equals(Long.class.getName())) {
				newName = "long";
			} else if (fullName.equals(Double.class.getName())) {
				newName = "double";
			} else if (fullName.equals(Byte.class.getName())) {
				newName = "byte";
			} else if (fullName.equals(Float.class.getName())) {
				newName = "float";
			} else if (fullName.equals(Short.class.getName())) {
				newName = "int";
			} else if (fullName.equals(Boolean.class.getName())) {
				newName = "boolean";
			} else if (fullName.equals(Date.class.getName())) {
				newName = "long";
			} else
				try {
					if (fullName.contains("<")) {
						StringBuilder untypedName = new StringBuilder();
						for (char c : fullName.toCharArray()) {
							if (c == '<') {
								break;
							}
							untypedName.append(c);
						}
						fullName = untypedName.toString();
					}
					Class<?> forName = Class.forName(fullName);
					if (XMLGregorianCalendar.class.isAssignableFrom(forName)) {
						newName = "date";
					} else if (List.class.isAssignableFrom(forName)) {
						newName = "List";
					} else if (Set.class.isAssignableFrom(forName)) {
						newName = "Set";
					}
				} catch (ClassNotFoundException e) {
				}
		}
		return newName;
	}



}
