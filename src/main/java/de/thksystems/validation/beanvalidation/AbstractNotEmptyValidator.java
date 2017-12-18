/*
 * tksCommons / mugwort
 *
 * Author : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.validation.beanvalidation;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractNotEmptyValidator {

    protected boolean isNotEmpty(Object fieldValue) {
        if (fieldValue == null) {
            return false;
        }
        // Check for empty String
        if (fieldValue instanceof CharSequence) {
            return StringUtils.isNotEmpty((String) fieldValue);
        }
        // Check for empty Map
        if (fieldValue instanceof Map<?, ?>) {
            return !((Map<?, ?>) fieldValue).isEmpty();
        }
        // Check for empty collection
        if (fieldValue instanceof Collection<?>) {
            return !((Collection<?>) fieldValue).isEmpty();
        }
        // Check for empty array
        if (fieldValue.getClass().isArray()) {
            return Array.getLength(fieldValue) > 0;
        }
        // Any other object that is not null
        return true;
    }

}
