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

public class OneNotEmptyValidator extends AbstractNotEmptyValidator implements ConstraintValidator<OneNotEmpty, Object> {

    private String[] fieldnames;

    @Override
    public void initialize(OneNotEmpty constraintAnnotation) {
        fieldnames = constraintAnnotation.fieldnames();
    }

    @Override
    public boolean isValid(Object container, ConstraintValidatorContext context) {
        try {
            for (String fieldname : fieldnames) {
                Object fieldValue = ReflectionUtils.getFieldValue(container, container.getClass().getDeclaredField(fieldname));
                if (isNotEmpty(fieldValue)) {
                    return true;
                }
            }
            return false;
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new ServiceRuntimeException(e.getMessage(), e);
        }
    }
}
