package com.maveric.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	static XSSFWorkbook wb;
	static XSSFSheet sh;
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		setExcel("TestData","Data");
		addData(1,1,"123");
		addData(1,2,1234);
		addData(2,1,true);
		addData(2,2,12.12);
		System.out.println(readData(1,1));
		System.out.println(readData(2,2));
		saveExcel("TestData");
	}
	public static void setExcel(String workbook, String sheet) throws FileNotFoundException, IOException
	{
		wb = new XSSFWorkbook(new FileInputStream("src/test/resources/data/"+workbook+".xlsx"));
		sh = wb.getSheet(sheet);
	}
	public static void addData(int row_count, int col_count, Object data)
	{
		XSSFRow row=null;
		if(sh.getRow(row_count-1)==null)
		{
			row = sh.createRow(row_count-1);
		}	
		else
		{
			row = sh.getRow(row_count-1);
		}
		XSSFCell cell = row.createCell(col_count-1);
		cell.setCellValue(data.toString());
	}
public static String readData(int row_count, int col_count) {
	Object data=null;
	XSSFRow row = sh.getRow(row_count-1);
	XSSFCell cell = row.getCell(col_count-1);
	try {
	data = cell.getNumericCellValue();
	}
	catch(Exception e)
	{
		if(e.getMessage().contains("STRING"))
			data = cell.getStringCellValue();
		else if(e.getMessage().contains("BOOLEAN"))
			data = cell.getBooleanCellValue();
	}
	return data.toString();
}
public static void saveExcel(String workbook) throws FileNotFoundException, IOException {
	wb.write(new FileOutputStream("src/test/resources/data/"+workbook+".xlsx"));
}







}
