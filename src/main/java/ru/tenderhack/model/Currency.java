package ru.tenderhack.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JacksonXmlRootElement(localName = "currency")
public class Currency {
	
	@JacksonXmlProperty(isAttribute = true)
	private String id;
	
	@JacksonXmlProperty(isAttribute = true)
	private int rate;
	
}
