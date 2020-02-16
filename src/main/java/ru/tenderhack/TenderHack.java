package ru.tenderhack;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
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
import org.w3c.dom.Text;
import ru.tenderhack.converter.Converter3;
import ru.tenderhack.model.Offer;
import ru.tenderhack.model.Offers;
import ru.tenderhack.model.Shop;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TenderHack extends Application {
	
	@Override
	public void start(Stage stage) throws Exception {
		
		Button parseButton = new Button("Parse");
		Button chooseFile = new Button("Choose File");
		Button shopButton = new Button("Shop");
//		shopButton.setVisible(false);
		
		
		
		GridPane shopGrid = new GridPane();
		shopGrid.setVgap(10);
		shopGrid.setHgap(10);
		shopGrid.setPadding(new Insets(20));
		
		TextField shopName = new TextField();
		TextField shopUrl = new TextField();
		
		shopGrid.add(new Label("Name:"), 0, 0);
		shopGrid.add(shopName, 1, 0);
		
		shopGrid.add(new Label("Url:"), 0, 1);
		shopGrid.add(shopUrl, 1, 1);

		TableView<List<String>> table = new TableView<>();
		
		HBox hBox = new HBox(chooseFile, shopButton, parseButton);
		hBox.setSpacing(20);
		hBox.setPadding(new Insets(10));
		
		BorderPane root = new BorderPane(table);
		root.setTop(hBox);
		
		shopButton.setOnAction(kasfksdf -> {
			if (root.getCenter().equals(table)) {
				root.setCenter(shopGrid);
			} else {
				root.setCenter(table);
			}
		});
		
		
		List<String> options = new ArrayList<>();
		options.add("ignore");
		options.add("id");
		options.add("name");
		options.add("vendorCode");
		options.add("country_of_origin");
		options.add("categoryId");
		options.add("subcategory");
		options.add("dimensions-long");
		options.add("dimensions-width");
		options.add("dimensions-height");
		options.add("param-material");
		options.add("param-diameter");
		options.add("manufacturer_warranty");
		options.add("param-color");
		options.add("weight");
		options.add("param-volume");
		options.add("param set");
		
		chooseFile.setOnAction(eee -> {
			try {
				
				FileChooser chooser = new FileChooser();
				chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("sdfsdf", "xls", "xlsx"));
				chooser.setTitle("Select file");
				File file = chooser.showOpenDialog(stage);
				
				if (file == null) {
					return;
				}
				
				table.getItems().clear();
				table.getColumns().clear();
				shopButton.setVisible(false);
				
				List<List<String>> data = readCells(file);
				int columnCount = data
						.stream()
						.mapToInt(List::size)
						.max()
						.orElseThrow();
				
				TableColumn<List<String>, String> visibilityColumn = new TableColumn<>("Visibility");
				table.getColumns().add(visibilityColumn);
				visibilityColumn.setCellFactory(c -> {
					TableCell<List<String>, String> tableCell = new TableCell<>();
					CheckBox checkBox = new CheckBox();
					checkBox.setSelected(true);
					tableCell.setGraphic(checkBox);
					return tableCell;
				});
				
				IntStream.range(0, columnCount)
				         .mapToObj(i -> {
					         TableColumn<List<String>, String> column = new TableColumn<>();
					         column.setGraphic(new ComboBox<>(FXCollections.observableArrayList(options)));
					         column.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().get(i)));
					         return column;
				         })
				         .forEach(table.getColumns()::add);
				
				table.getItems().addAll(data.stream().skip(3).collect(Collectors.toList()));
				
				int documentType = calculateType(data);
				System.out.println("documentType = " + documentType);
				
				shopButton.setVisible(true);
				
				parseButton.setOnAction(e -> {
					
					Map<Integer, String> fieldMap = IntStream
							.range(0, table.getColumns().size())
							.skip(1)
							.mapToObj(i -> {
								TableColumn<List<String>, String> column = (TableColumn<List<String>, String>) table.getColumns().get(i);
								String name = ((ComboBox<String>) column.getGraphic()).getSelectionModel().getSelectedItem();
								return new AbstractMap.SimpleEntry<>(i - 1, name);
							})
							.filter(ee -> ee.getValue() != null && !ee.getValue().equals("ignore"))
							.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
					
					List<Offer> offers = new Converter3().convert(fieldMap, data);
					
					JacksonXmlModule module = new JacksonXmlModule();
					XmlMapper mapper = new XmlMapper(module);
					mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
					mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
					mapper.enable(SerializationFeature.INDENT_OUTPUT);
					
					Shop shop = new Shop();
					shop.setName(shopName.getText());
					shop.setUrl(shopUrl.getText());
					shop.setOffers(offers);
					
					try {
						mapper.writeValue(new File("result.xml"), shop);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
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
	
	private List<List<String>> readCells(File file) throws Exception {
		
		List<List<String>> tableData = new ArrayList<>();
		
		Object extractor = ExtractorFactory.createExtractor(file);
		
		try {
			if (file.getName().endsWith(".xlsx")) {
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
