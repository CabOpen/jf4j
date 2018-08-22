/**
 *
 */
package org.jf4j.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
/**
 * Annotation to specify that a declared numeric Java field should map to a JSON string.
 * <p>
 * By default declared Java numeric fields are stored as JSON numbers. F However, if instead the JSON content uses a
 * JSON String to store the value, one needs to use the {@link JsonString} annotation.
 */
public @interface JsonString {

}
