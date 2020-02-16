package ru.tenderhack.model;

import lombok.ToString;

@ToString
public class Param {
	
	private String name;
	private String unit;
	private String value;
	
	public Param(String string) {
		try {
			String[] parts = string.split(":");
			name = parts[0].strip();
			
			String secondPart = parts[1].trim();
			if (Character.isDigit(secondPart.charAt(0))) {
				
				String[] valueParts = secondPart.split(" ");
				
				if (valueParts.length == 2) {
					value = valueParts[0].trim();
					unit = valueParts[1].trim();
				} else {
					value = valueParts[0].trim();
				}
				
			} else {
				value = secondPart;
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
	}
}
