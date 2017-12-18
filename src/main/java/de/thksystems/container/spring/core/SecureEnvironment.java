package de.thksystems.container.spring.core;

import javax.annotation.PostConstruct;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

/**
 * Extends the common spring {@link Environment} ({@link StandardEnvironment}) by providing support for encrypted properties.
 * <p>
 * It uses jasypt's {@link BasicTextEncryptor} for decryption, so you do not need the 'java crypto extension'.
 * <p>
 * To encrypt a password using the command line, you can use the {@link JasyptTool}. <br>
 * To encrypt a password programmatically, you can use:
 * 
 * <pre>
 * BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
 * textEncryptor.setPassword(password);
 * String encrypted = textEncryptor.encrypt(plain));
 * </pre>
 */
public class SecureEnvironment extends StandardEnvironment {

	@Autowired
	private ApplicationContext appContext;

	private final BasicTextEncryptor textEncryptor;

	public SecureEnvironment(String masterPassword) {
		this(masterPassword.toCharArray());
	}

	public SecureEnvironment(char[] masterPassword) {
		textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPasswordCharArray(masterPassword);
	}

	public SecureEnvironment(int[] masterPassword) {
		this(int2charArray(masterPassword));
	}

	/**
	 * Merge with {@link PropertySources} from {@link ApplicationContext}'s {@link Environment} for adding {@link PropertySources} defined by
	 * {@link PropertySources} (and may be others)
	 * <p>
	 * <i>This is just a work-a-round, because you currently cannot tell the {@link ApplicationContext} which implementation of {@link Environment} to use.
	 */
	@PostConstruct
	protected void mergePropertySources() {
		MutablePropertySources myPropertySources = super.getPropertySources();
		if (appContext != null) {
			Environment appCtxEnv = appContext.getEnvironment();
			if (appCtxEnv != null && appCtxEnv instanceof ConfigurableEnvironment) {
				for (PropertySource<?> appCtxPropertySource : ((ConfigurableEnvironment) appCtxEnv).getPropertySources()) {
					if (!myPropertySources.contains(appCtxPropertySource.getName())) {
						myPropertySources.addLast(appCtxPropertySource);
					}
				}
			}
		}
	}

	private static char[] int2charArray(int[] ints) {
		char[] chars = new char[ints.length];
		for (int i = 0; i < ints.length; i++) {
			chars[i] = (char) ints[i];
		}
		return chars;
	}

	/**
	 * Returns the decrypted property value associated with the given key, or <code>null</code>, if the key cannot be resolved.
	 * <p>
	 * If the property value with the associated key is not encrypted, some unexpected data will be returned.
	 */
	public String getAndDecryptProperty(String key) {
		return getAndDecryptProperty(key, null);
	}

	/**
	 * Returns the decrypted property value associated with the given key, or the default value, if the key cannot be resolved.
	 * <p>
	 * If the property value with the associated key is not encrypted, some unexpected data will be returned.
	 */
	public String getAndDecryptProperty(String key, String defaultValue) {
		String prop = getProperty(key);
		if (prop == null) {
			return defaultValue;
		}
		return textEncryptor.decrypt(prop);
	}

	/**
	 * Returns the decrypted property value associated with the given key (never {@code null}).
	 * <p>
	 * If the property value with the associated key is not encrypted, some unexpected data will be returned.
	 * 
	 * @throws IllegalStateException
	 *             If the key cannot be resolved
	 */
	public String getAndDecryptRequiredProperty(String key) throws IllegalStateException {
		String prop = super.getRequiredProperty(key);
		return textEncryptor.decrypt(prop);
	}

}
