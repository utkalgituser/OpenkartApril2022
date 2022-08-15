package com.qa.openkart.util;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtil {

	public static final String TEST_DATA_SHEET_PATH = "./src/test/resources/testdata/opencarttestdata.xlsx";

	private ExcelUtil() {

	}

	public static Object[][] getTestData(String sheetName) {
		Sheet sheet;
		Object[][] data = null;
		try (FileInputStream fis = new FileInputStream(TEST_DATA_SHEET_PATH);
				Workbook book = WorkbookFactory.create(fis);) {
			
			sheet = book.getSheet(sheetName);
			data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];
			for (int i = 0; i < sheet.getLastRowNum(); i++) {
				for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
					data[i][j] = sheet.getRow(i + 1).getCell(j).toString();
				}
			}
		} catch (IOException | EncryptedDocumentException e) {
			e.printStackTrace();
		}
		return data;
	}
}
