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

import be.redlab.jaxb.swagger.XJCHelper;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.outline.EnumOutline;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author redlab
 *
 */
public final class PropertyProcessStrategy extends AbstractProcessStrategy {

	public PropertyProcessStrategy(AbstractProcessUtil processUtil) {
		super(processUtil);
	}

	public PropertyProcessStrategy() {
		super();
	}

	@Override
	public void doProcess(final JDefinedClass implClass, final CClassInfo targetClass, final Collection<JMethod> methods, final Map<String, JFieldVar> fields,
			final Collection<EnumOutline> enums) {
		for (JMethod jm : methods) {
			int mods = jm.mods().getValue();
			if (processUtil.validMethodMods(mods) && null == XJCHelper.getAnnotation(jm.annotations(), XmlTransient.class)) {
				JAnnotationUse annotation = XJCHelper.getAnnotation(jm.annotations(), XmlElement.class);
				processUtil.addAnnotationForMethod(implClass, targetClass, jm, processUtil.isRequiredByAnnotation(annotation), null, enums);
			}
		}
		for (Entry<String, JFieldVar> e : fields.entrySet()) {
			int mods = e.getValue().mods().getValue();
			if (processUtil.validFieldMods(mods) && null != XJCHelper.getAnnotation(e.getValue().annotations(), XmlElement.class)) {
				processUtil.addAnnotationForField(implClass, targetClass, e.getValue(), enums);
			}
		}
	}
}