package de.thksystems.validation.beanvalidation;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public abstract class AbstractValueValidator<A extends Annotation> implements ConstraintValidator<A, Object> {

	@Override
	public void initialize(A constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		// Null is always valid (Use an additional @NotNull to avoid)
		if (value == null) {
			return true;
		}
		// Check for collection of objects (as strings)
		if (value instanceof Collection<?>) {
			return ((Collection<?>) value).stream().allMatch(getPredicate());
		}
		// Check for array of objects (as strings)
		if (value.getClass().isArray()) {
			return Arrays.stream((Object[]) value).allMatch(getPredicate());
		}
		// Check as string
		return getPredicate().test(value.toString());
	}

	protected abstract Predicate<Object> getPredicate();

}
