/*
 * tksCommons / mugwort
 * 
 * Author : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.validation.beanvalidation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The annotated {@link String} (or an arbitrary scalar object as its {@link String} representation) must be a valid currency code (ISO 4217 ALPHA). If the
 * annotated value is a {@link Collection} or an array, every element (in its {@link String} representation) must be a valid currency code.
 */
@Documented
@Constraint(validatedBy = CurrencyValidator.class)
@Target({ METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface Currency {
	String message() default "Invalid currency code.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
