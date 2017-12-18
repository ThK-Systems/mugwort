package de.thksystems.persistence.xstream.converters;

import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("test")
public class MapTestEntity {

	@XStreamConverter(MapToAttributesConverter.class)
	public Map<String, String> myMap = new HashMap<>();

	@XStreamConverter(value = MapToAttributesConverter.class, useImplicitType = false, types = {Integer.class})
	public Map<String, Integer> intMap = new HashMap<>();

	@XStreamAsAttribute
	public String myText;
}
