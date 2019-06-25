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

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JAnnotationValue;
import com.sun.codemodel.JFormatter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.StringWriter;
import java.util.Collection;

/**
 * @author redlab
 */
public final class XJCHelper {
    private static final String VALUE = "value";

    private XJCHelper() {
    }

    /**
     * Returns a Stringyfied version of the value of an annotation member
     *
     * @param a      the annotation
     * @param member the member to fetch
     * @return the String value of the member or null of not found
     */
    public static String getStringValueFromAnnotationMember(final JAnnotationUse a, final String member) {
        JAnnotationValue jAnnotationValue = a.getAnnotationMembers().get(member);
        if (null != jAnnotationValue) {
            StringWriter w = new StringWriter();
            JFormatter f = new JFormatter(w);
            jAnnotationValue.generate(f);
            return w.toString();
        }
        return null;
    }

    /**
     * Searches for the given class in the JAnnotationUse collection
     *
     * @param annotations collection of annotations to search in
     * @param annotation  the annotation class to search for
     * @return the annotation or null if not found
     */
    public static JAnnotationUse getAnnotation(final Collection<JAnnotationUse> annotations, final Class<?> annotation) {
        String name = annotation.getName();
        for (JAnnotationUse a : annotations) {
            String fullName = a.getAnnotationClass().fullName();
            if (fullName.equals(name)) {
                return a;
            }
        }
        return null;
    }

    /**
     * @param annotations the annotations to search in for {@link XmlAccessorType}
     * @return {@link XmlAccessType} if valid is found, null otherwise
     */
    public static XmlAccessType getAccessType(final Collection<JAnnotationUse> annotations) {
        JAnnotationUse a = getAnnotation(annotations, XmlAccessorType.class);
        String value = null;
        if (a != null) {
            value = XJCHelper.getStringValueFromAnnotationMember(a, VALUE);

            if ("javax.xml.bind.annotation.XmlAccessType.FIELD".equals(value)) {
                return XmlAccessType.FIELD;
            } else if ("javax.xml.bind.annotation.XmlAccessType.PROPERTY".equals(value)) {
                return XmlAccessType.PROPERTY;
            } else if ("javax.xml.bind.annotation.XmlAccessType.PUBLIC_MEMBER".equals(value)) {
                return XmlAccessType.PUBLIC_MEMBER;
            } else if ("javax.xml.bind.annotation.XmlAccessType.NONE".equals(value)) {
                return XmlAccessType.NONE;
            }
        }
        return null;
    }
}
