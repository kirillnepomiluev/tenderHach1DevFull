package ru.tenderhack.model;

import lombok.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Offer {
	
	private Integer id;
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
	
	public Offer(List<String> line, Map<Integer, String> fieldMap, ) {
		for (int columnIndex = 0; columnIndex < line.size(); columnIndex++) {
			
			String name = fieldMap.get(columnIndex);
			
			if (name == null) continue;
			
			try {
				Field field = Offer.class.getDeclaredField(name);
				field.setAccessible(true);
				
				String value = line.get(columnIndex);
				field.set(this, process(name, value));
			} catch (NoSuchFieldException | IllegalAccessException e) {
				System.out.println("Wrong field name");
				e.printStackTrace();
			}
		}
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
	
	@Override
	public String toString() {
		return "Offer{" +
		       "id=" + id +
		       ", available=" + available +
		       ", currencyId='" + currencyId + '\'' +
		       ", name='" + name + '\'' +
		       ", picture='" + picture + '\'' +
		       ", description='" + description + '\'' +
		       ", url='" + url + '\'' +
		       ", ste='" + ste + '\'' +
		       ", isVisibleToStateCustomers=" + isVisibleToStateCustomers +
		       ", isAvailableToIndividuals=" + isAvailableToIndividuals +
		       ", ppcategory=" + ppcategory +
		       ", categoryId=" + categoryId +
		       ", okei=" + okei +
		       ", min_quantity=" + min_quantity +
		       ", max_quantity=" + max_quantity +
		       ", beginDate='" + beginDate + '\'' +
		       ", endDate='" + endDate + '\'' +
		       ", packageType=" + packageType +
		       ", params=" + params +
		       ", price=" + price +
		       ", deliveryOptions=" + deliveryOptions +
		       ", model='" + model + '\'' +
		       ", vendorCode='" + vendorCode + '\'' +
		       ", vat=" + vat +
		       ", delivery=" + delivery +
		       ", manufacturer_warranty=" + manufacturer_warranty +
		       ", barcode=" + barcode +
		       ", expiry='" + expiry + '\'' +
		       ", weight=" + weight +
		       ", dimensions='" + dimensions + '\'' +
		       ", downloadable=" + downloadable +
		       ", age=" + age +
		       ", groupId='" + groupId + '\'' +
		       ", country_of_origin='" + country_of_origin + '\'' +
		       '}';
	}
	
	//	private boolean store;
//	private boolean pickup;
//	private String sales_notes;
}
