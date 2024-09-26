package com.Librarian2.Librarian2.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


import com.Librarian2.Librarian2.models.Books;

public class Helper {
	public static String[] HEADERS={
	        "book_id","book_name","author","genre"
	    } ;

	    public static String SHEET_NAME="Available_books";

	    public static ByteArrayInputStream dataToExcel(List<Books> list) throws IOException{
	        
	        Workbook workbook= new XSSFWorkbook();
	        ByteArrayOutputStream out=new ByteArrayOutputStream();
	        try{
	            
	            Sheet sheet = workbook.createSheet(SHEET_NAME);
	            Row row = sheet.createRow(0);
	            for(int i =0; i<HEADERS.length;i++){
	                Cell cell = row.createCell(i);
	                cell.setCellValue(HEADERS[i]);
	            }
	            int rowIndex=1;
	            for(Books b: list){
	                Row dataRow = sheet.createRow(rowIndex);
	                rowIndex++;
	                dataRow.createCell(0).setCellValue(b.getBook_id());
	                dataRow.createCell(1).setCellValue(b.getBook_name());
	                dataRow.createCell(2).setCellValue(b.getAuthor());
	                dataRow.createCell(3).setCellValue(b.getGenre());
	                
	            }
	            workbook.write(out);
	            return new ByteArrayInputStream(out.toByteArray());
	        }
	        catch(IOException e){
	            e.printStackTrace();
	            throw new IOException("Failed to import data", e);
	        }
	        finally{
	            workbook.close();
	            out.close();
	        }
	    }
}
