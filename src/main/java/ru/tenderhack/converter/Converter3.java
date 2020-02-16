package ru.tenderhack.converter;

import ru.tenderhack.model.Offer;
import ru.tenderhack.model.Param;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class Converter3 {
	
	private Map<String, Integer> categoryMap = new HashMap<>();
	
	public List<Offer> convert(Map<Integer, String> fieldMap, List<List<String>> data) {
		return data
				.stream()
				.skip(5)
				.map(line -> constructOffer(fieldMap, line))
				.collect(Collectors.toList());
	}
	
	private Offer constructOffer(Map<Integer, String> fieldMap, List<String> line) {
		
		Offer offer = new Offer();
		offer.setParams(new ArrayList<>());
		
		for (int columnIndex = 0; columnIndex < line.size(); columnIndex++) {
			
			String name = fieldMap.get(columnIndex);
			
			if (name == null) continue;
			
			try {
				Object value = process(offer, name, line.get(columnIndex));
				
				if (value == null) continue;
				
				Field field = Offer.class.getDeclaredField(name);
				field.setAccessible(true);
				field.set(offer, value);
			} catch (IllegalAccessException | NoSuchFieldException e) {
				System.out.println("Wrong field at " + columnIndex + " column");
				e.printStackTrace();
			}
		}
		
		return offer;
	}
	
	private Object process(Offer offer, String name, String value) {
		switch (name) {
			case "id":
				if (value.toLowerCase().equals("null")) {
					return null;
				}
				
				String string = value.replaceAll("[^0-9\\.]", "");
				
				if (string.isBlank()) {
					return null;
				}
				
				return (int) Double.parseDouble(string);
			case "price":
			case "weight": {
				
				String prepared = value
						.split(" ")[0]
						.replaceAll("[^0-9,]", "")
						.replaceAll(",", ".");
				
				if (prepared.isBlank()) {
					return null;
				}
				
				return Double.parseDouble(prepared);
			}
			case "name":
			case "vendorCode":
			case "country_of_origin":
			case "description":
				if (value.toLowerCase().equals("null")) {
					return null;
				}
				
				return value;
			case "categoryId": {
				if (categoryMap.containsKey(value)) {
					categoryMap.put(value, categoryMap.get(value) + 1);
				}
				return categoryMap.get(value);
			}
			case "subcategory": {
				return null;
			}
//			case "dimensions-long": {
//				offer.set
//			}
//			case "dimensions-width":
//			case "dimensions-height": {
//				if (value.toLowerCase().equals("null")) {
//					return "";
//				} else {
//					return Double.parseDouble(value);
//				}
//			}
			case "param-material": {
				Param param = makeParam("material", value);
				
				if (param != null) {
					offer.getParams().add(param);
				}
				
				return null;
			}
			case "param-diameter": {
				Param param = makeParam("diameter", value);
				
				if (param != null) {
					offer.getParams().add(param);
				}
				
				return null;
			}
			case "param-color": {
				Param param = makeParam("color", value);
				
				if (param != null) {
					offer.getParams().add(param);
				}
				
				return null;
			}
			case "param-volume": {
				Param param = makeParam("volume", value);
				
				if (param != null) {
					offer.getParams().add(param);
				}
				
				return null;
			}
			case "max_quantity": {
				return (int) Double.parseDouble(value);
			}
			case "param set": {
				List<Param> params = Arrays
						.stream(value.split("\\.\\n"))
						.map(String::strip)
						.map(Param::new)
						.collect(Collectors.toList());
				offer.getParams().addAll(params);
			}
		}
		
		return null;
	}
	
	private static Param makeParam(String name, String value) {
		if (value.toLowerCase().equals("null")) {
			return null;
		}
		
		String[] parts = value.split(" ");
		
		if (parts.length == 2) {
			return new Param(name, parts[1].strip(), parts[0].strip());
		} else {
			return new Param(name, null, value);
		}
	}
}
