package be.redlab.jaxb.swagger.process;

import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;

import be.redlab.jaxb.swagger.XJCHelper;
import com.sun.codemodel.JAnnotatable;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.EnumOutline;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSParticle;
import io.swagger.annotations.ApiModelProperty;

/**
 * Abstract class to exclude (possibly) common logic from {@link ProcessUtil}
 *
 * @author mark.petrenyi
 */
public abstract class AbstractProcessUtil {

    protected static final String REQUIRED = "required";
    protected static final String IS = "is";
    protected static final String GET = "get";

    /**
     * Annotate methods based on the xsd
     * @param o
     * @param t
     * @param m
     * @param required
     * @param defaultValue
     * @param enums
     */
    public abstract void addAnnotationForMethod(final JDefinedClass o, CClassInfo t, final JMethod m, final boolean required, final String defaultValue,
                                                final Collection<EnumOutline> enums);

    /**
     * Annotate fields based on the xsd
     * @param implClass
     * @param targetClass
     * @param jFieldVar
     * @param enums
     */
    public abstract void addAnnotationForField(final JDefinedClass implClass, CClassInfo targetClass, final JFieldVar jFieldVar, final Collection<EnumOutline> enums);

    /**
     * @param mods
     * @return
     */
    public boolean validFieldMods(final int mods) {
        if ((mods & JMod.FINAL) != 0 || (mods & JMod.STATIC) != 0
            || (mods & JMod.ABSTRACT) != 0 || (mods & JMod.NATIVE) != 0 || (mods & JMod.TRANSIENT) != 0
            || (mods & JMod.VOLATILE) != 0) {
            return false;
        }
        return true;

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
        return null != annotation &&
               "true".equalsIgnoreCase(XJCHelper.getStringValueFromAnnotationMember(annotation, REQUIRED));
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
     * Returns the {@link JFieldVar} of {@literal implClass} based on {@literal getter} param.
     * Getter is expected to start with get or is and followed by the field name with uppercase first letter
     *
     * @param implClass
     * @param getter    name for getter method
     * @return {@link JFieldVar}
     */
    public JFieldVar getCorrespondingField(final JDefinedClass implClass, final String getter) {
        String trimmedMethod = trimMethod(getter);
        if (trimmedMethod != null && trimmedMethod.length() > 0) {
            StringBuilder b = new StringBuilder(trimmedMethod.substring(0, 1).toLowerCase());
            if (trimmedMethod.length() > 1) {
                b.append(trimmedMethod.substring(1));
            }
            String fieldName = b.toString();
            for (JFieldVar fieldVar : implClass.fields().values()) {
                if (fieldName.equals(fieldVar.name())) {
                    return fieldVar;
                }
            }

        }
        return null;
    }

    private String trimMethod(String method) {
        if (method.length() > GET.length() && method.startsWith(GET)) {
            return method.substring(GET.length());
        } else if (method.length() > IS.length() && method.startsWith(IS)) {
            return method.substring(IS.length());
        }
        return null;
    }

    /**
     * @param mods
     * @return
     */
    public boolean validMethodMods(int mods) {
        if (((mods & JMod.PROTECTED) != 0 || (mods & JMod.PRIVATE) != 0 || (mods & JMod.FINAL) != 0 ||
             (mods & JMod.STATIC) != 0
             || (mods & JMod.ABSTRACT) != 0 || (mods & JMod.NATIVE) != 0 || (mods & JMod.TRANSIENT) != 0 ||
             (mods & JMod.VOLATILE) != 0)) {
            return false;
        }
        return true;
    }

    public boolean isAnnotationNotPresent(JAnnotatable a) {
        return null == XJCHelper.getAnnotation(a.annotations(), ApiModelProperty.class);
    }

    /**
     * Extract value from {@code <xs:annotation><xs:documentation>} for property if exists.
     *
     * @param targetClass  the TargetClass
     * @param propertyName property name
     * @return value from {@code <xs:annotation><xs:documentation>} or <code>null</code> if
     * {@code <xs:annotation><xs:documentation>} does not exists.
     */
    protected String getDescription(CClassInfo targetClass, String propertyName) {
        CPropertyInfo property = targetClass.getProperty(propertyName);
        String description = propertyName;
        XSComponent schemaComponent = property.getSchemaComponent();
        if (schemaComponent instanceof XSParticle) {
            XSAnnotation annotation = ((XSParticle) schemaComponent).getTerm().getAnnotation();
            if (annotation != null) {
                Object annotationObj = annotation.getAnnotation();
                if (annotationObj instanceof BindInfo && ((BindInfo) annotationObj).getDocumentation() != null) {
                    description = ((BindInfo) annotationObj).getDocumentation();
                }
            }
        }
        return description;
    }

    /**
     * @param clazz
     * @param enums
     * @return
     */
    public EnumOutline getKnownEnum(final String clazz, final Collection<EnumOutline> enums) {
        for (EnumOutline eo : enums) {
            if (eo.clazz.fullName().equals(clazz)) {
                return eo;
            }
        }
        return null;
    }
}
