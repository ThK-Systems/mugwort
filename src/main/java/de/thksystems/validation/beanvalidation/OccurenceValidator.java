/*
 * tksCommons / mugwort
 *
 * Author : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.validation.beanvalidation;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.ReflectionUtils;

public class OccurenceValidator implements ConstraintValidator<Occurence, Collection<? extends Object>> {

    private String getterName;

    private String getterValue;

    private boolean ignoreCase;

    private int min;

    private int max;

    @Override
    public void initialize(Occurence constraintAnnotation) {
        getterName = constraintAnnotation.getterName();
        getterValue = constraintAnnotation.value();
        ignoreCase = constraintAnnotation.ignoreCase();
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();

    }

    @Override
    public boolean isValid(Collection<? extends Object> list, ConstraintValidatorContext context) {
        if (list.isEmpty()) {
            return true;
        }
        Class<? extends Object> elemClass = list.iterator().next().getClass();
        Method elemMethod = ReflectionUtils.findMethod(elemClass, getterName);
        if (elemMethod == null) {
            return false;
        }
        int valueCount = 0;
        for (Object elemObj : list) {
            Object elemFieldValue = ReflectionUtils.invokeMethod(elemMethod, elemObj);
            if (elemFieldValue != null) {
                if (ignoreCase) {
                    if (elemFieldValue.toString().equalsIgnoreCase(getterValue)) {
                        valueCount++;
                    }
                } else {
                    if (elemFieldValue.toString().equals(getterValue)) {
                        valueCount++;
                    }
                }
            }
        }
        return valueCount >= min && (valueCount <= max || max == 0);
    }
}
