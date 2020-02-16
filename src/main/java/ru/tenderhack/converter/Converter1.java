package ru.tenderhack.converter;

import ru.tenderhack.model.Offer;
import ru.tenderhack.model.Param;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Converter1 extends Converter{
	
	private static final Map<Integer, String> fieldMap;
	
	static {
		fieldMap = Map.of(
				1, "id",
				2, "name",
				3, "params",
				4, "description",
				5, "max_quantity",
				6, "price"
		);
	}
	
	@Override
	public Map<Integer, String> getFieldMap() {
		return fieldMap;
	}
	
	@Override
	protected Offer constructOffer(List<String> line) {
		
		Map<Integer, String> fieldMap = Map.of(
				1, "id",
				2, "name",
				3, "params",
				4, "description",
				5, "max_quantity",
				6, "price"
		);
		
		Offer offer = new Offer();
		
		for (int columnIndex = 0; columnIndex < line.size(); columnIndex++) {
			
			String name = fieldMap.get(columnIndex);
			
			if (name == null) continue;
			
			try {
				Field field = Offer.class.getDeclaredField(name);
				field.setAccessible(true);
				
				field.set(offer, process(name, line.get(columnIndex)));
			} catch (IllegalAccessException | NoSuchFieldException e) {
				System.out.println("Wrong field at " + columnIndex + " column");
				e.printStackTrace();
			}
		}
		
		return offer;
	}
	
	private Object process(String name, String value) {
		if ("id".equals(name)) {
			return (int) Double.parseDouble(value);
		}
		
		if ("name".equals(name) || "description".equals(name)) {
			return value;
		}
		
		if ("params".equals(name)) {
			return Arrays
					.stream(value.split("\\.\\n"))
					.map(String::strip)
					.map(Param::new)
					.collect(Collectors.toList());
		}
		
		if ("max_quantity".equals(name)) {
			return (int) Double.parseDouble(value);
		}
		
		if ("price".equals(name)) {
			return Double.parseDouble(value);
		}
		
		return value;
	}
}
