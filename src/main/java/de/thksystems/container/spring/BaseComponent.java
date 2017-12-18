package de.thksystems.container.spring;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.thksystems.exception.ServiceRuntimeException;

public class BaseComponent {

	final static Logger LOG = LoggerFactory.getLogger(BaseComponent.class);

	/**
	 * Throws given exception as {@link ServiceRuntimeException}.
	 */
	protected <T> T throwAsServiceRuntimeExceptionAndLog(Throwable cause, String message) {
		String msg = message + ": " + cause.toString();
		LOG.error(msg, cause);
		throw new ServiceRuntimeException(msg, cause);
	}

	/**
	 * Returns <code>true</code>, if we are running in the context of a junit-test.
	 * 
	 * @see "Source: http://stackoverflow.com/a/12717377/1869090"
	 */
	protected boolean isJUnitTest() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		List<StackTraceElement> list = Arrays.asList(stackTrace);
		for (StackTraceElement element : list) {
			if (element.getClassName().contains(".junit.")) {
				return true;
			}
		}
		return false;
	}

}