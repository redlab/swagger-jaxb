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

import java.util.Collection;
import java.util.Map;

import be.redlab.jaxb.swagger.ProcessStrategy;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;

public abstract class AbstractProcessStrategy implements ProcessStrategy {


	protected final ProcessUtil processUtil;

	/**
	 *
	 */
	public AbstractProcessStrategy() {
		this.processUtil = ProcessUtil.getInstance();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * be.redlab.jaxb.swagger.ProcessStrategy#process(com.sun.codemodel.JDefinedClass)
	 */
	public final void process(final JDefinedClass implClass) {
		Collection<JMethod> methods = implClass.methods();
		Map<String, JFieldVar> fields = implClass.fields();
		doProcess(implClass, methods, fields);
	}

	/**
	 * @param implClass
	 * @param fields
	 * @param methods
	 */
	public abstract void doProcess(JDefinedClass implClass, Collection<JMethod> methods, Map<String, JFieldVar> fields);


}