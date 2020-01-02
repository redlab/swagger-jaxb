package be.redlab.jaxb.swagger;

import javax.xml.bind.annotation.XmlAccessType;

import be.redlab.jaxb.swagger.process.FieldProcessStrategy;
import be.redlab.jaxb.swagger.process.NoProcessStrategy;
import be.redlab.jaxb.swagger.process.PropertyProcessStrategy;
import be.redlab.jaxb.swagger.process.PublicMemberProcessStrategy;

/**
 * Factory class to create and reuse ProcessStrategies
 * @author mark.petrenyi
 */
public class SwaggerProcessStrategyFactory {

    private static FieldProcessStrategy fieldProcessor;
    private static NoProcessStrategy noProcessor;
    private static PropertyProcessStrategy propProcessor;
    private static PublicMemberProcessStrategy publicMemberProcessor;

    private SwaggerProcessStrategyFactory() {
        super();
    }

    public static ProcessStrategy getProcessStrategy(final XmlAccessType access) {
        switch (access) {
            case FIELD:
                return null == fieldProcessor ? fieldProcessor = new FieldProcessStrategy() : fieldProcessor;
            case NONE:
                return null == noProcessor ? noProcessor = new NoProcessStrategy() : noProcessor;
            case PROPERTY:
                return null == propProcessor ? propProcessor = new PropertyProcessStrategy() : propProcessor;
            case PUBLIC_MEMBER:
                return null == publicMemberProcessor ? publicMemberProcessor = new PublicMemberProcessStrategy() :
                       publicMemberProcessor;
            default:
                throw new UnsupportedOperationException(String.format("%s not supported as ProcessStrategy", access));
        }
    }
}
