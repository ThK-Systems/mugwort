/*
 * tksCommons / mugwort
 *
 * Author : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.exception;

public class ServiceRuntimeException extends RuntimeException implements Retryable {

    private static final long serialVersionUID = -5411254397078580026L;

    private final boolean retryable;

    public ServiceRuntimeException(String message) {
        super(message);
        this.retryable = false;
    }

    public ServiceRuntimeException(Throwable cause) {
        super(cause);
        this.retryable = false;
    }

    public ServiceRuntimeException(String message, Throwable cause) {
        super(message, cause);
        this.retryable = false;
    }

    public ServiceRuntimeException(String message, boolean retryable) {
        super(message);
        this.retryable = retryable;
    }

    public ServiceRuntimeException(Throwable cause, boolean retryable) {
        super(cause);
        this.retryable = retryable;
    }

    public ServiceRuntimeException(String message, Throwable cause, boolean retryable) {
        super(message, cause);
        this.retryable = retryable;
    }

    @Override
    public boolean isRetryable() {
        return retryable;
    }

}
