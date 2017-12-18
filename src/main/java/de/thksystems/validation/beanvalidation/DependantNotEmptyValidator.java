/*
 * tksCommons / mugwort
 * 
 * Author : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.validation.beanvalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.thksystems.exception.ServiceRuntimeException;
import de.thksystems.util.reflection.ReflectionUtils;

public class DependantNotEmptyValidator extends AbstractNotEmptyValidator implements ConstraintValidator<DependantNotEmpty, Object> {

	private String fieldname;

	private String dependantField;

	private String depandantValue;

	@Override
	public void initialize(DependantNotEmpty constraintAnnotation) {
		fieldname = constraintAnnotation.fieldname();
		dependantField = constraintAnnotation.dependantField();
		depandantValue = constraintAnnotation.dependantValue();
	}

	@Override
	public boolean isValid(Object container, ConstraintValidatorContext context) {
		try {
			Object currentConditionValue = ReflectionUtils.getFieldValue(container, container.getClass().getDeclaredField(dependantField));
			if ((currentConditionValue == null && depandantValue == null) || (currentConditionValue != null && currentConditionValue.equals(depandantValue))
					|| (currentConditionValue != null && currentConditionValue.toString().equals(depandantValue))) {
				Object fieldValue = ReflectionUtils.getFieldValue(container, container.getClass().getDeclaredField(fieldname));
				return isNotEmpty(fieldValue);
			}
			return true;
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}

}
