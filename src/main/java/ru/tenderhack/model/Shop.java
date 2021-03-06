package ru.tenderhack.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JacksonXmlRootElement(localName = "shop")
public class Shop {
	
	private String name;
	private String company;
	private String url;
	private String platform;
	private String version;
	private String agency;
	private String email;
	
	@Builder.Default
	@JacksonXmlProperty(localName = "currency")
	@JacksonXmlElementWrapper(localName = "currencies")
	private List<Currency> currencies = List.of(Currency.builder().id("RUR").rate(1).build());
	
	@Builder.Default
	private List<Category> categories = List.of();
	
	@JacksonXmlProperty(localName = "offer")
	@JacksonXmlElementWrapper(localName = "offers")
	private List<Offer> offers;
}
