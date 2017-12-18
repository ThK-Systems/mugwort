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
 * Marks the entity as immutable, denies all changes and log it, if someone tries to change anything. <br>
 * (Unfortunately it it not possible to throw an exception in that case, because of the hibernate api.)
 * 
 * @see ImmutableCheckInterceptor
 * @author Thomas Kuhlmann (kuhl025), arvato D1CS, extern <thomas.kuhlmann.extern@bertelsmann.de>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Immutable {

}
