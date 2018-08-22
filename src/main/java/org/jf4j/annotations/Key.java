package org.jf4j.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to specify that a field is a data key, optionally providing the data key name to use.
 * <p>
 * If the data key name is not specified, the default is the Java field's name. For example:
 * </p>
 *
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Key {

    /**
     * Override the data key name of the field or {@code "##default"} to use the Java field's name.
     */
    String value() default "##default";
}
