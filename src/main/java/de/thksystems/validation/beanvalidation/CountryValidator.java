/*
 * tksCommons / mugwort
 * 
 * Author : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.validation.beanvalidation;

import java.util.function.Predicate;

import de.thksystems.util.text.LocaleUtils;

public class CountryValidator extends AbstractValueValidator<Country> {

	@Override
	protected Predicate<Object> getPredicate() {
		return o -> LocaleUtils.isValidCountryCode(o.toString());
	}
}
