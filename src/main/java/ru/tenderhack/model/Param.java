package ru.tenderhack.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)

@JacksonXmlRootElement
public class Param {
	
	@JacksonXmlProperty(isAttribute = true)
	private String name;
	
	@JacksonXmlProperty(isAttribute = true)
	private String unit;
	
	@JacksonXmlText
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
