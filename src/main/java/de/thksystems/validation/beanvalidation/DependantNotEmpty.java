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
 * The value of the field 'fieldname' must not be empty, if the value of the file 'dependantField' is equal to the 'dependantValue'.
 */
@Documented
@Constraint(validatedBy = DependantNotEmptyValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface DependantNotEmpty {
	String message() default "At least one field must have a non-empty value.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/** Name of field, must be not empty. */
	String fieldname();

	/** Name of field to check for dependantValue. */
	String dependantField();

	/** If dependantField has dependantValue, the value of the fieldname must not be empty. */
	String dependantValue();
}
