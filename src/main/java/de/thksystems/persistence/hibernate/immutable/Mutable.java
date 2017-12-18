/*
 * tksCommons / mugwort
 * 
 * Author : Thomas Kuhlmann (ThK-Systems, http://oss.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.persistence.hibernate.immutable;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the field as mutable.
 * <p>
 * Only useful for entities marked as {@link Immutable}. In other entities it is ignored!!
 * <p>
 * <b>Use it with caution and never use it for business fields, just for technical fields!!!</b>
 * 
 * @author Thomas Kuhlmann (kuhl025), arvato D1CS, extern <thomas.kuhlmann.extern@bertelsmann.de>
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mutable {

	/**
	 * The value can be set only once from <code>null</code> to some non-null value.
	 */
	boolean finall() default false;

}
