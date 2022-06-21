package bo.com.micrium.modulobase.util;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

@Log4j2
public class SysExcel {

    synchronized public static void pintarCabecera(HSSFWorkbook wb, String titulo) {
        try {
            if (wb != null) {

                HSSFSheet sheet = wb.getSheetAt(0);
                HSSFRow header = sheet.getRow(0);

                HSSFCellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

                HSSFFont cabecera = wb.createFont();
                cabecera.setColor(HSSFColor.WHITE.index);
                cabecera.setFontHeightInPoints((short) 10);
                cabecera.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                cabecera.setFontName("Arial");
                cellStyle.setFont(cabecera);

                for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
                    HSSFCell cell = header.getCell(i);
                    cell.setCellStyle(cellStyle);
                }

                sheet.shiftRows(0, sheet.getLastRowNum(), 3, true, false);

                HSSFRow newrow1 = sheet.createRow(1);
                Cell celltitulo = newrow1.createCell(2);

                sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));
                celltitulo.setCellValue(titulo);
                celltitulo.setCellStyle(cellStyle);

            } else {
                log.warn("[pintarCabecera] La hoja excel es nulo.");
            }
        } catch (Exception e) {
            log.error("[pintarCabecera] Fallo al pintar la cabecera.", e);
        }
    }

}
