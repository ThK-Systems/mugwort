/*
 * tksCommons / mugwort
 * 
 * Author : Thomas Kuhlmann (ThK-Systems, http://oss.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.persistence.hibernate.immutable;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

/**
 * Checks the immutablity of entities.
 * <p>
 * <b>The interceptor must be registered!!</b>
 * 
 * @author Thomas Kuhlmann (kuhl025), arvato D1CS, extern <thomas.kuhlmann.extern@bertelsmann.de>
 */
public class ImmutableCheckInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = -298527553545916632L;

	private static final Logger LOG = LoggerFactory.getLogger(ImmutableCheckInterceptor.class);

	/**
	 * Checks the immutablity of entities.
	 * 
	 * @see org.hibernate.EmptyInterceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[],
	 *      org.hibernate.type.Type[])
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		boolean modified = false;
		// Check for Immutable annoation
		if (AnnotationUtils.findAnnotation(entity.getClass(), Immutable.class) != null) {
			// For every property
			for (int i = 0; i < propertyNames.length; i++) {
				// Check for modification
				if (!checkForModification(currentState[i], previousState[i])) {
					// Get modified field
					Field field = ReflectionUtils.findField(entity.getClass(), propertyNames[i]);
					// Check if modification is allowed or illegal
					if (!checkModificationAllowed(field, previousState[i])) {
						// Throwing an (even runtime-) exception is not allowed here and leaves persistence in a very inconsistent state.
						String msg = String.format("ILLEGAL MODIFICATION ATTEMPT: %s[%s].%s: %s -> %s", entity.getClass(), id, propertyNames[i],
								previousState[i], currentState[i]);
						LOG.error(msg);
						// Revert modification
						currentState[i] = previousState[i];
						modified = true;
					}
				}

			}
		}
		return modified;
	}

	private boolean checkForModification(Object currentState, Object previousState) {
		if (currentState == null && previousState == null) {
			return true;
		}
		if (currentState instanceof Date) {
			return ((Date) currentState).getTime() == ((Date) previousState).getTime();
		}

		return currentState != null && currentState.equals(previousState);
	}

	/**
	 * Returns <code>true</code>, if modification is allowed.
	 */
	private boolean checkModificationAllowed(Field field, Object previous) {
		Mutable mutableAnnotation = findMutableAnnotation(field);
		// No Mutable annotation -> Illegal modification
		if (mutableAnnotation == null) {
			return false;
		}
		// If finall, previous value must be null, otherwise it is a illegal modification
		if (mutableAnnotation.finall()) {
			return previous == null;
		}
		// Mutable annotation is present and not finall. -> Allowed modification.
		return true;
	}

	/**
	 * Returns the {@link Mutable} anntotation for the {@link Field}, or <code>null</code>, if not found.
	 */
	private Mutable findMutableAnnotation(Field field) {
		for (Annotation annotation : field.getDeclaredAnnotations()) {
			if (annotation instanceof Mutable) {
				return (Mutable) annotation;
			}
		}
		return null;
	}
}
