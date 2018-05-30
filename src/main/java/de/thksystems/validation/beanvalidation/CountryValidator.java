/*
 * tksCommons / mugwort
 *
 * Author : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.validation.beanvalidation;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import de.thksystems.util.text.LocaleUtils;

public class CountryValidator extends AbstractValueValidator<Country> {

    private List<String> additionalValidCountryCodes;

    @Override
    public void initialize(Country constraintAnnotation) {
        super.initialize(constraintAnnotation);
        additionalValidCountryCodes = Arrays.asList(StringUtils.split(constraintAnnotation.additionalValidCountryCodes(), ","));
    }

    @Override
    protected Predicate<Object> getPredicate() {
        return o -> LocaleUtils.isValidCountryCode(o.toString()) || additionalValidCountryCodes.contains(o.toString());
    }
}
