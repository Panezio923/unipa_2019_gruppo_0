package it.eng.unipa.filesharing.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.util.Iterator;


@Component
public class PreviewXlsxService implements PreviewService{
    @Override
    public byte[] convert(byte[] toConvert) throws IOException, DocumentException {
        InputStream filecontent = new ByteArrayInputStream(toConvert);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XSSFWorkbook my_xlsx_workbook = null;
        XSSFSheet my_worksheet_xlsx = null;
        Document document = new Document(PageSize.ARCH_E, 0, 0, 0, 0);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        PdfDestination magnify = null;
        float magnifyOpt = (float) 70.0;
        magnify = new PdfDestination(PdfDestination.XYZ, -1, -1, magnifyOpt / 100);
        int pageNumberToOpenTo = 1;
        PdfAction zoom = PdfAction.gotoLocalPage(pageNumberToOpenTo, magnify, writer);
        writer.setOpenAction(zoom);
        document.addDocListener(writer);

        Iterator<Row> rowIterator = null;
        int maxColumn = 0;

            try {
                my_xlsx_workbook = new XSSFWorkbook(filecontent);
                my_worksheet_xlsx = my_xlsx_workbook.getSheetAt(0);
                rowIterator = my_worksheet_xlsx.iterator();
                maxColumn = my_worksheet_xlsx.getRow(0).getLastCellNum();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        PdfPTable my_table = new PdfPTable(maxColumn);
        my_table.setHorizontalAlignment(Element.ALIGN_CENTER);
        my_table.setWidthPercentage(100);
        my_table.setSpacingBefore(0f);
        my_table.setSpacingAfter(0f);
        PdfPCell table_cell;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next(); //Fetch CELL
                switch (cell.getCellType()) { //Identify CELL type
                    case Cell.CELL_TYPE_STRING:
                        //Push the data from Excel to PDF Cell
                        table_cell = new PdfPCell(new Phrase(cell.getStringCellValue()));
                        if (row.getRowNum() == 0) {
                            table_cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            table_cell.setBorderColor(BaseColor.BLACK);
                        }
                        my_table.addCell(table_cell);
                        break;
                }
            }
        }
        document.add(my_table);
        document.close();
        System.out.println("Excel file converted to PDF successfully");
        return out.toByteArray();
    }
}
