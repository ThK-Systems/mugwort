/*
 * tksCommons / mugwort
 *
 * Author : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.validation.beanvalidation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The annotated list must not have more than one elements, where the specified field (getter) has the specified value.
 */
@Documented
@Constraint(validatedBy = OccurenceValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Occurence {
    String message() default "Only a single occurence is allowed.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Name of getter method to access the field.
     */
    String getterName();

    /**
     * Value, that must exists not more than once.
     */
    String value();

    /**
     * Ignore case by comparing values as string.
     */
    boolean ignoreCase() default true;

    /**
     * Min occurence.
     */
    int min() default 1;

    /**
     * Max occurence. (0 for no max limit)
     */
    int max() default 1;
}
