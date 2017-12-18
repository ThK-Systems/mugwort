/*
 * tksCommons / mugwort
 *
 * Author : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */

package de.thksystems.exception;

/**
 * Sometimes an exception has a cause, which makes it possible to retry the exceptional request a little bit later again, e.g. a network problem.
 */
public interface Retryable {

    boolean isRetryable();

}
