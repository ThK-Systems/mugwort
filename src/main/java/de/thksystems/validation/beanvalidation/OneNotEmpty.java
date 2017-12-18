/*
 * tksCommons / mugwort
 * 
 * Author : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.validation.beanvalidation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * At least one of the given fields must be non-empty (for strings, collections and arrays) or non-null (for anything else).
 */
@Documented
@Constraint(validatedBy = OneNotEmptyValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface OneNotEmpty {
	String message() default "At least one field must have a non-empty value.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/** Name of the fields. */
	String[] fieldnames();
}
