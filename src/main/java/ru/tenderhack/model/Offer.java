package ru.tenderhack.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JacksonXmlRootElement(localName = "offer")
public class Offer {
	
	@JacksonXmlProperty(isAttribute = true)
	private Integer id;
	
	@JacksonXmlProperty(isAttribute = true)
	private Boolean available;
	
	private String currencyId;
	private String name;
	private String picture;
	private String description;
	private String url;
	private String ste;
	private Boolean isVisibleToStateCustomers;
	private Boolean isAvailableToIndividuals;
	private Integer ppcategory;
	private Integer categoryId;
	private Okei okei;
	private Integer min_quantity;
	private Integer max_quantity;
	private String beginDate;
	private String endDate;
	private Integer packageType;
	
	@JacksonXmlProperty(localName = "param")
	@JacksonXmlElementWrapper(useWrapping = false, localName = "param")
	private List<Param> params;
	private Double price;
	private List<DeliveryOption> deliveryOptions;
	private String model;
	private String vendorCode;
	private Integer vat;
	private Boolean delivery;
	private Boolean manufacturer_warranty;
	private List<Integer> barcode;
	private String expiry;
	private Double weight;
	private String dimensions;
	private Boolean downloadable;
	private Age age;
	private String groupId;
	private String country_of_origin;
	
	//	private boolean store;
//	private boolean pickup;
//	private String sales_notes;
}
