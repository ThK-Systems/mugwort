/*
 * tksCommons / mugwort
 * 
 * Author : Thomas Kuhlmann (ThK-Systems, http://oss.thk-systems.de) License : LGPL (https://www.gnu.org/licenses/lgpl.html)
 */
package de.thksystems.persistence.xstream.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;

public class MapToAttributesConverterTest {

	@Test
	public void testConverter() throws Exception {
		String textFoo = "Foo";
		String keyFirstname = "firstname";
		String keyLastname = "lastname";
		String valueHomer = "homer";
		String valueSimpson = "simpson";

		MapTestEntity origEntity = new MapTestEntity();
		origEntity.myText = textFoo;
		origEntity.myMap.put(keyFirstname, valueHomer);
		origEntity.myMap.put(keyLastname, valueSimpson);
		origEntity.intMap.put("age", 42);

		XStream xstream = new XStream();
		xstream.processAnnotations(MapTestEntity.class);
		String xml = xstream.toXML(origEntity);
		assertTrue(xml.indexOf(String.format("%s=\"%s\"", keyLastname, valueSimpson)) > 0);
		assertTrue(xml.indexOf(String.format("%s=\"%s\"", keyFirstname, valueHomer)) > 0);
		// System.out.println(xml);

		MapTestEntity newEntity = (MapTestEntity) xstream.fromXML(xml);
		assertEquals(textFoo, newEntity.myText);
		assertEquals(2, newEntity.myMap.size());
		assertTrue(newEntity.myMap.containsKey(keyFirstname));
		assertTrue(newEntity.myMap.containsKey(keyLastname));
		assertEquals(valueHomer, newEntity.myMap.get(keyFirstname));
		assertEquals(valueSimpson, newEntity.myMap.get(keyLastname));
		assertEquals(1, newEntity.intMap.size());
		assertEquals(origEntity.intMap.get("age"), origEntity.intMap.get("age"));
	}
}
