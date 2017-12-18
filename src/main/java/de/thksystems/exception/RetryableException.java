/*
 * tksCommons / mugwort
 *
 * Author : Thomas Kuhlmann (ThK-Systems, http://www.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.exception;

public class RetryableException extends Exception implements Retryable {

    private static final long serialVersionUID = -7773527792833722664L;

    private final boolean retryable;

    public RetryableException(String errorMessage) {
        super(errorMessage);
        this.retryable = false;
    }

    public RetryableException(String errorMessage, boolean retryable) {
        super(errorMessage);
        this.retryable = retryable;
    }

    public RetryableException(Exception cause) {
        super(cause);
        this.retryable = false;
    }

    public RetryableException(Exception cause, boolean retryable) {
        super(cause);
        this.retryable = retryable;
    }

    public RetryableException(String message, Exception cause) {
        super(message, cause);
        this.retryable = false;
    }

    public RetryableException(String message, Exception cause, boolean retryable) {
        super(message, cause);
        this.retryable = retryable;
    }

    @Override
    public boolean isRetryable() {
        return retryable;
    }

}
