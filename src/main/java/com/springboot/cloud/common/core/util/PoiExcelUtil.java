package com.springboot.cloud.common.core.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@Slf4j
public class PoiExcelUtil {

    public static ByteArrayInputStream exportExcel(List<JSONObject> list, String sheetName, String[] keys, String[] columnNames) throws IOException {
        SXSSFWorkbook wb = null;
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        try {
            wb = new SXSSFWorkbook(1000);
            Sheet sheet = wb.createSheet(sheetName);

            for(int i = 0; i < keys.length; i++) {
                sheet.setColumnWidth((short) i, (short) (35.7 * 150));
            }

            Font f = wb.createFont();
            f.setFontHeightInPoints((short) 10);
            f.setColor(IndexedColors.BLACK.getIndex());
            f.setBoldweight(Font.BOLDWEIGHT_BOLD);

            CellStyle cs = wb.createCellStyle();
            cs.setFont(f);
            cs.setBorderLeft(CellStyle.BORDER_THIN);
            cs.setBorderRight(CellStyle.BORDER_THIN);
            cs.setBorderTop(CellStyle.BORDER_THIN);
            cs.setBorderBottom(CellStyle.BORDER_THIN);
            cs.setAlignment(CellStyle.ALIGN_CENTER);

            Font f2 = wb.createFont();
            f2.setFontHeightInPoints((short) 10);
            f2.setColor(IndexedColors.BLACK.getIndex());

            CellStyle cs2 = wb.createCellStyle();
            cs2.setFont(f2);
            cs2.setBorderLeft(CellStyle.BORDER_THIN);
            cs2.setBorderRight(CellStyle.BORDER_THIN);
            cs2.setBorderTop(CellStyle.BORDER_THIN);
            cs2.setBorderBottom(CellStyle.BORDER_THIN);
            cs2.setAlignment(CellStyle.ALIGN_CENTER);
            cs2.setDataFormat(wb.createDataFormat().getFormat("@")); //设置成文本格式

            //设置列名
            Cell cell = null;
            Row row = sheet.createRow((short) 0);
            for(int i = 0; i < columnNames.length; i++){
                cell = row.createCell(i);
                cell.setCellValue(columnNames[i]);
                cell.setCellStyle(cs);
            }

            Row row1 = null;
            for (Integer i = 0, size = list.size(); i < size; i++) { //生成数据
                row1 = sheet.createRow(i + 1);
                for(Integer j = 0, length = keys.length; j < length; j++){
                    cell = row1.createCell(j);
                    //日期处理
                    if (list.get(i).get(keys[j]) instanceof Date) {
                        list.get(i).put(keys[j], list.get(i).get(keys[j]).toString().replace(".0", ""));
                    }
                    //数字处理
                    if (CommonUtil.isNumber(list.get(i).get(keys[j])) == true) {
                        list.get(i).put(keys[j], CommonUtil.trimZero(list.get(i).get(keys[j])));
                    }
                    cell.setCellValue(list.get(i).get(keys[j]) == null ? "--" : list.get(i).get(keys[j]).toString());
                    cell.setCellStyle(cs2);
                }
            }

            if (wb != null) {
                baos = new ByteArrayOutputStream();
                wb.write(baos); //输出
                bais = new ByteArrayInputStream(baos.toByteArray());
            }
        } finally {
            if (baos != null) {
                baos.close();
            }
            wb.dispose();
        }
        return bais;
    }

    /**
     * 写excel文件到response
    **/
    public static void writeToOutput(ByteArrayInputStream bais, HttpServletResponse response, String outName){
        byte[] buf = new byte[1024];
        int len;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(outName + System.currentTimeMillis() + "", "utf-8"));
            OutputStream out = response.getOutputStream();
            while ((len = bais.read(buf)) > 0){
                out.write(buf);
            }
        }catch (IOException e){
            log.error("writeToOutput error -> " + e.getMessage());
        }
    }
}
