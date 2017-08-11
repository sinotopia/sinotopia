package com.hkfs.fundamental.excel.poi;

import com.alibaba.fastjson.util.IOUtils;
import com.hkfs.fundamental.common.utils.NumberUtils;
import com.hkfs.fundamental.common.utils.StrUtils;
import com.hkfs.fundamental.common.utils.TimeUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * excel导出
 * Created by zhoubing on 2016/2/23.
 */
public abstract class BaseExcelExporter<T> {
    private static Logger logger = LoggerFactory.getLogger(BaseExcelExporter.class);
    /**
     * 导出列表
     * @param response
     * @param dataList
     */
    public void export(HttpServletResponse response, List<T> dataList) {
        if (processing) {
            return;
        }
        if (dataList == null) {
            dataList = new ArrayList<T>(0);
        }
        this.processing = true;

        HSSFWorkbook workbook = null;
        try {
            workbook = process(dataList);
            outputWorkbook(response, workbook);
        }
        finally {
            this.processing = false;
        }
    }

    protected void outputWorkbook(HttpServletResponse response, HSSFWorkbook workbook) {
        OutputStream os = null;
        try {
            String fileName = getFileName();
            if (!fileName.contains(".")) {
                fileName = fileName+".xls";
            }
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            response.setContentType("application/octet-stream");

            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.close(os);
        }
    }

    private boolean processing = false;

    /**
     * Excel文件名
     * @return
     */
    protected abstract String getFileName();

    /**
     * Excel标题头
     * @return
     */
    protected abstract String[] getHeaders();

    /**
     * 创建内容行
     * @param workbook
     * @param sheet
     * @param rowNum
     * @param data
     */
    protected abstract void createContentRow(HSSFWorkbook workbook, HSSFSheet sheet, int rowNum, T data);

    /**
     * 自定义其他内容
     * @param workbook
     * @param sheet
     * @param dataList
     */
    protected void customizeContents(HSSFWorkbook workbook, HSSFSheet sheet, int rowNum, List<T> dataList) {
        //子类重写
    }

    protected HSSFWorkbook process(List<T> dataList) {
        int i = 0;
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个sheet
        HSSFSheet sheet = createSheet(workbook, i++, getSheetTitle());
        //设置样式
        HSSFCellStyle style = createStyle(workbook);
        //产生表格标题行
        createHeaderRow(workbook, sheet, style, getHeaders());
        //内容行
        for (T data : dataList) {
            createContentRow(workbook, sheet, i++, data);
        }

        //自定义其他
        customizeContents(workbook, sheet, i, dataList);

        return workbook;
    }

    protected HSSFSheet createSheet(HSSFWorkbook workbook, int sheetNum, String sheetTitle) {
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetNum, sheetTitle);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth(40);
        return sheet;
    }

    protected HSSFCellStyle createStyle(HSSFWorkbook workbook) {
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);
        return style;
    }

    protected void createHeaderRow(HSSFWorkbook workbook, HSSFSheet sheet, HSSFCellStyle style, String[] headers) {
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            sheet.setColumnWidth(i, 6000);

            HSSFCell cell = row.createCell(i);
            //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text.toString());
        }
    }

    /**
     * 获取表格标题
     */
    protected String getSheetTitle() {
        return "Sheet1";
    }


    protected String formatAmount(Double amount, int decimalDigits) {
        return StrUtils.formatAmount(NumberUtils.valueOf(amount), decimalDigits);
    }

    protected String formatAmount(Double amount) {
        return formatAmount(amount, 2);
    }

    protected String formatTime(Date time) {
        if (time != null) {
            return TimeUtils.formatTime(time, TimeUtils.FORMAT_YYYYMMDDHHMMSS);
        }
        return "";
    }
}
