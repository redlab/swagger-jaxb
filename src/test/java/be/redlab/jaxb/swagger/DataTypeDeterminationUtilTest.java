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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;

import com.sun.codemodel.JType;

/**
 * @author redlab
 *
 */
public class DataTypeDeterminationUtilTest {
	@Test
	public void typeCheckPrimitiveInt() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.TRUE);
		when(jType.fullName()).thenReturn("int");
		Assert.assertEquals("int", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckPrimitiveLong() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.TRUE);
		when(jType.fullName()).thenReturn("long");
		Assert.assertEquals("long", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckPrimitiveDouble() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.TRUE);
		when(jType.fullName()).thenReturn("double");
		Assert.assertEquals("double", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckPrimitiveFloat() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.TRUE);
		when(jType.fullName()).thenReturn("float");
		Assert.assertEquals("float", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckPrimitiveByte() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.TRUE);
		when(jType.fullName()).thenReturn("byte");
		Assert.assertEquals("byte", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckPrimitiveShort() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.TRUE);
		when(jType.fullName()).thenReturn("short");
		Assert.assertEquals("int", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckArray() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.FALSE);
		when(jType.isArray()).thenReturn(Boolean.TRUE);
		when(jType.fullName()).thenReturn("java.lang.String[]");
		Assert.assertEquals("Array", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckString() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.FALSE);
		when(jType.isArray()).thenReturn(Boolean.FALSE);
		when(jType.fullName()).thenReturn("java.lang.String");
		Assert.assertEquals("string", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckInteger() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.FALSE);
		when(jType.isArray()).thenReturn(Boolean.FALSE);
		when(jType.fullName()).thenReturn("java.lang.Integer");
		Assert.assertEquals("int", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckLong() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.FALSE);
		when(jType.isArray()).thenReturn(Boolean.FALSE);
		when(jType.fullName()).thenReturn("java.lang.Long");
		Assert.assertEquals("long", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckDouble() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.FALSE);
		when(jType.isArray()).thenReturn(Boolean.FALSE);
		when(jType.fullName()).thenReturn("java.lang.Double");
		Assert.assertEquals("double", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckSHort() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.FALSE);
		when(jType.isArray()).thenReturn(Boolean.FALSE);
		when(jType.fullName()).thenReturn("java.lang.Short");
		Assert.assertEquals("int", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckByte() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.FALSE);
		when(jType.isArray()).thenReturn(Boolean.FALSE);
		when(jType.fullName()).thenReturn("java.lang.Byte");
		Assert.assertEquals("byte", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckFloat() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.FALSE);
		when(jType.isArray()).thenReturn(Boolean.FALSE);
		when(jType.fullName()).thenReturn("java.lang.Float");
		Assert.assertEquals("float", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckTypedList() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.FALSE);
		when(jType.isArray()).thenReturn(Boolean.FALSE);
		when(jType.fullName()).thenReturn("java.util.List<Integer>");
		Assert.assertEquals("List", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckUnTypedList() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.FALSE);
		when(jType.isArray()).thenReturn(Boolean.FALSE);
		when(jType.fullName()).thenReturn("java.util.List");
		Assert.assertEquals("List", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCheckTypedArrayList() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.FALSE);
		when(jType.isArray()).thenReturn(Boolean.FALSE);
		when(jType.fullName()).thenReturn("java.util.ArrayList<Integer>");
		Assert.assertEquals("List", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeCalendar() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.FALSE);
		when(jType.isArray()).thenReturn(Boolean.FALSE);
		when(jType.fullName()).thenReturn("javax.xml.datatype.XMLGregorianCalendar");
		Assert.assertEquals("date", DataTypeDeterminationUtil.setDataType(jType));
	}

	@Test
	public void typeAnObject() {
		JType jType = mock(JType.class);
		when(jType.isPrimitive()).thenReturn(Boolean.FALSE);
		when(jType.isArray()).thenReturn(Boolean.FALSE);
		when(jType.fullName()).thenReturn("be.redlab.jaxb.swagger.TestEnum");
		when(jType.name()).thenReturn("TestEnum");
		Assert.assertEquals("TestEnum", DataTypeDeterminationUtil.setDataType(jType));
	}
}
