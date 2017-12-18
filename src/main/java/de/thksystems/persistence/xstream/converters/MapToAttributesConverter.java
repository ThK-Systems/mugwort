/*
 * tksCommons / mugwort
 * 
 * Author : Thomas Kuhlmann (ThK-Systems, http://oss.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.persistence.xstream.converters;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * <h3>Converts a map to a list of xml-attributes.</h3> The key of the {@link Map} becomes the name of the xml-attribute, and the value of the {@link Map} becomes the value of the xml-attribute.
 * <p>
 * <b>For example</b>
 * <p>
 * The following {@link Map} named 'myMap':
 * <ul>
 * <li>firstname -> homer</li>
 * <li>lastname -> simpson</li>
 * <li>spouse- > marge</li>
 * </ul>
 * will be converted to the following XML:
 * 
 * <pre>
 * {@code
 * <myMap lastname="simpson" firstname="homer" spouse="marge"/\>
 * }
 * </pre>
 * 
 * <b>Usage</b>
 * <p>
 * Use the converter by adding a {@link XStreamConverter} annotation, like:
 * <p>
 * <code>@XStreamConverter(MapToAttributesConverter.class)</code>
 * <p>
 * <br>
 * <p>
 * <b>Allowed Parameter Types</b>
 * <p>
 * The default use of the converter is for {@link Map} of {@link String},{@link String}. The type of the key must be {@link String}.
 * <p>
 * As the value type another type may be choosen. This type must be 'convertable' to {@link String}, so it must have a usable {@link #toString()} method and a constructor which takes a single {@link String} as its only argument. (Example types: {@link Integer}, {@link Long}, {@link BigDecimal}.) You need to specify the value type to the {@link XStreamConverter} annotation like this:
 * <p>
 * <code>@XStreamConverter(value = MapToAttributesConverter.class, useImplicitType = false, types = { Integer.class })</code>.
 */
public class MapToAttributesConverter implements Converter {

	protected static final Class<?> TARGET_CLASS = Map.class;
	private Class<?> valueClass = String.class;
	private Constructor<?> valueClassConstructor;

	public MapToAttributesConverter() {
		this(String.class);
	}

	public MapToAttributesConverter(Class<?> valueClass) {
		if (!TARGET_CLASS.isAssignableFrom(valueClass)) {
			this.valueClass = valueClass;
			checkValueClass();
		}
	}

	private void checkValueClass() {
		if (!String.class.isAssignableFrom(valueClass)) {
			Class<?>[] parameterTypes = new Class<?>[] { String.class };
			try {
				valueClassConstructor = valueClass.getConstructor(parameterTypes);
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException("Value class cannot be converted to string: " + valueClass);
			} catch (SecurityException e) {
				throw new IllegalArgumentException("Cannot check value class: " + e.getMessage(), e);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return TARGET_CLASS.isAssignableFrom(type);
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		Map<String, Object> map = (Map<String, Object>) source;
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			writer.addAttribute(entry.getKey(), entry.getValue().toString());
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Map<String, Object> map = new HashMap<>();
		for (int i = 0; i < reader.getAttributeCount(); i++) {
			String key = reader.getAttributeName(i);
			String valueString = reader.getAttribute(key);
			Object valueObject;
			if (valueClassConstructor != null) {
				try {
					valueObject = valueClassConstructor.newInstance(valueString);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new XStreamException(String.format("Cannot convert string value '%s' to class '%s': %s", valueString, valueClass, e.getMessage()), e);
				}
			} else {
				valueObject = valueString;
			}
			map.put(key, valueObject);
		}
		return map;
	}
}