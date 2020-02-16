package ru.tenderhack.converter;

import ru.tenderhack.model.Offer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Converter {
	
	public abstract Map<Integer, String> getFieldMap();
	
	public List<Offer> convert(Map<Integer, String> fieldMap, List<List<String>> data) {
		return data
				.stream()
				.skip(5)
				.map(this::constructOffer)
				.collect(Collectors.toList());
	}
	
	protected abstract Offer constructOffer(List<String> line);
}
