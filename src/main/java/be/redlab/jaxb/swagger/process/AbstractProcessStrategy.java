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

import be.redlab.jaxb.swagger.ProcessStrategy;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.outline.EnumOutline;

import java.util.Collection;
import java.util.Map;

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
	public final void process(final JDefinedClass implClass, CClassInfo targetClass, final Collection<EnumOutline> enums) {
		Collection<JMethod> methods = implClass.methods();
		Map<String, JFieldVar> fields = implClass.fields();
		doProcess(implClass, targetClass, methods, fields, enums);
	}

	/**
	 * @param implClass
	 * @param targetClass
	 * @param methods
	 * @param fields
	 */
	public abstract void doProcess(JDefinedClass implClass, CClassInfo targetClass, Collection<JMethod> methods, Map<String, JFieldVar> fields,
			Collection<EnumOutline> enums);


}