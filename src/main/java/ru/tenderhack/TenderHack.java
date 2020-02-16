package ru.tenderhack;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.poi.extractor.POIOLE2TextExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.extractor.ExtractorFactory;
import org.apache.poi.ooxml.extractor.POIXMLTextExtractor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class TenderHack extends Application {
	
	private static final String DOCUMENT1 = "document1.xlsx.xls";
	private static final String DOCUMENT2 = "document2.xlsx";
	private static final String DOCUMENT3 = "document3.xlsx";
	
	@Override
	public void start(Stage stage) throws Exception {
		
		List<List<String>> data = readCells(DOCUMENT3);
		
		int columnCount = data
				.stream()
				.mapToInt(List::size)
				.max()
				.orElseThrow();
		
		TableView<List<String>> table = new TableView<>();
		
		IntStream.range(0, columnCount)
		         .mapToObj(i -> {
			         TableColumn<List<String>, String> column = new TableColumn<>("Column " + i);
			         column.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().get(i)));
			         return column;
		         })
		         .forEach(table.getColumns()::add);
		
		table.getItems().addAll(data);
		
		int documentType = calculateType(data);
		
		Map<Integer, String> fieldMap;
		
		if (documentType == 0) {
			fieldMap = Map.of(
					1, "id",
					2, "name",
					3, "params",
					4, "description",
					5, "max_quantity",
					6, "price"
			);
		} else if (documentType == 1) {
		
		} else {
			fieldMap = new HashMap<>();
			fieldMap.put(0, "id");
			fieldMap.put(1, "name");
			fieldMap.put(2, "vendor");
			fieldMap.put(3, "country_of_origin");
			fieldMap.put(4, "categoryId");
			fieldMap.put(5, "subcategory");
			fieldMap.put(6, "dimensions-long");
			fieldMap.put(7, "dimensions-wigth");
			fieldMap.put(8, "dimensions-height");
			fieldMap.put(9, "param-material");
			fieldMap.put(10, "param-diameter");
			fieldMap.put(11, "manufacturer_warranty");
			fieldMap.put(12, "param-color");
			fieldMap.put(13, "weight");
			fieldMap.put(14, "volume");
			
		}
		
		System.out.println("documentType = " + documentType);

//		String organization = data
//				.stream()
//				.limit(5)
//				.flatMap(List::stream)
//				.filter(line -> line.toLowerCase().contains("организация"))
//				.findAny()
//				.map(line -> line.split(":")[1].trim())
//				.orElse("");

//		List<Offer> offers = data
//				.stream()
//				.skip(5)
//				.map(line -> new Offer(line, fieldMap))
//				.collect(Collectors.toList());
		
		BorderPane root = new BorderPane(table);
		Scene scene = new Scene(root, 1240, 720);
		stage.setScene(scene);
		stage.setTitle("TenderHack");
		stage.show();
	}
	
	private int calculateType(List<List<String>> data) {
		if (data.get(0).size() < 7) {
			return 1;
		} else {
			if (data.get(0).size() > 15) {
				return 2;
			}
			return 0;
		}
	}
	
	private List<List<String>> readCells(String filename) throws Exception {
		
		List<List<String>> tableData = new ArrayList<>();
		
		Object extractor = ExtractorFactory.createExtractor(new File(filename));
		
		try {
			if (filename.endsWith(".xlsx")) {
				XSSFSheet sheet = ((XSSFWorkbook) ((POIXMLTextExtractor) extractor).getDocument()).getSheetAt(0);
				for (int rowIndex = 0; rowIndex < sheet.getLastRowNum(); rowIndex++) {
					XSSFRow row = sheet.getRow(rowIndex);
					short cellCount = row.getLastCellNum();
					
					List<String> tableRow = new ArrayList<>();
					
					try {
						for (int columnIndex = 0; columnIndex < cellCount; columnIndex++) {
							
							XSSFCell cell = row.getCell(columnIndex);
							
							String value = "";
							
							if (cell != null) {
								CellType type = cell.getCellType();
								
								switch (type) {
									case STRING: {
										value = cell.getStringCellValue();
									}
									break;
									case FORMULA: {
										value = cell.getCellFormula();
									}
									break;
									case NUMERIC: {
										value = Double.toString(cell.getNumericCellValue());
									}
									break;
								}
							}
							tableRow.add(value);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					tableData.add(tableRow);
				}
			} else {
				HSSFSheet sheet = ((HSSFWorkbook) ((POIOLE2TextExtractor) extractor).getDocument()).getSheetAt(0);
				
				for (int rowIndex = 0; rowIndex < sheet.getLastRowNum(); rowIndex++) {
					HSSFRow row = sheet.getRow(rowIndex);
					short cellCount = row.getLastCellNum();
					
					List<String> tableRow = new ArrayList<>();
					
					for (int columnIndex = 0; columnIndex < cellCount; columnIndex++) {
						
						HSSFCell cell = row.getCell(columnIndex);
						CellType type = cell.getCellType();
						
						String value = "";
						
						switch (type) {
							case STRING: {
								value = cell.getStringCellValue();
							}
							break;
							case FORMULA: {
								value = cell.getCellFormula();
							}
							break;
							case NUMERIC: {
								value = Double.toString(cell.getNumericCellValue());
							}
							break;
						}
						
						tableRow.add(value);
					}
					
					tableData.add(tableRow);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tableData;
	}
	
	public static void main(String[] args) {
		TenderHack.launch(args);
	}
}
