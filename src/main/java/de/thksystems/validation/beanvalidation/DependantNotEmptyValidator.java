/*
 * tksCommons / mugwort
 *
 * Author : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.validation.beanvalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.reflect.FieldUtils;

import de.thksystems.exception.ServiceRuntimeException;

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
            Object currentConditionValue = FieldUtils.readDeclaredField(container, dependantField);
            if ((currentConditionValue == null && depandantValue == null) || (currentConditionValue != null && currentConditionValue.equals(depandantValue))
                    || (currentConditionValue != null && currentConditionValue.toString().equals(depandantValue))) {
                Object fieldValue = FieldUtils.readDeclaredField(container, fieldname);
                return isNotEmpty(fieldValue);
            }
            return true;
        } catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
            throw new ServiceRuntimeException(e.getMessage(), e);
        }
    }

}
