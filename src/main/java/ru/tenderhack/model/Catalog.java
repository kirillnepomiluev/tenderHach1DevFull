package ru.tenderhack.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JacksonXmlRootElement(localName = "yml_catalog")
public class Catalog {
	
	@Builder.Default
	@JacksonXmlProperty(isAttribute = true, localName = "date")
	private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm"));
	
	private Shop shop;
}
