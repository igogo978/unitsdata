/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.unitsdata.service;

import app.unitsdata.model.Item;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 *
 * @author igogo
 */
@Service
public class CreateXLSX {

    public XSSFWorkbook create(List<Item> items) {
        XSSFWorkbook wb = new XSSFWorkbook();

        String sheetName = "Sheet1";//name of sheet
        XSSFSheet sheet = wb.createSheet(sheetName);
//        List<Item> items = itemsrepository.findAll(unitInfo.getPid());
        Item item;
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);

        //設定標題列
        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("姓名");

        cell = row.createCell(1);
        cell.setCellValue("使用者識別碼");

        cell = row.createCell(2);
        cell.setCellValue("單位代碼");

        cell = row.createCell(3);
        cell.setCellValue("學期");

        cell = row.createCell(4);
        cell.setCellValue("年級");

        cell = row.createCell(5);
        cell.setCellValue("班級");

        cell = row.createCell(6);
        cell.setCellValue("班級名稱");

        cell = row.createCell(7);
        cell.setCellValue("職稱1");

        cell = row.createCell(8);
        cell.setCellValue("職稱2");

        cell = row.createCell(9);
        cell.setCellValue("職稱3");

        for (int i = 0; i < items.size(); i++) {
            row = sheet.createRow(i + 1);
            item = items.get(i);

            cell = row.createCell(0);
            cell.setCellValue(item.getFullName());

            cell = row.createCell(1);
            cell.setCellValue(item.getPersonGuid());

            cell = row.createCell(2);
            cell.setCellValue(item.getSchoolID());

            cell = row.createCell(3);
            cell.setCellValue(item.getSemester());

            cell = row.createCell(4);
            cell.setCellValue(item.getGrade());

            cell = row.createCell(5);
            cell.setCellValue(item.getClassno());

            cell = row.createCell(6);
            cell.setCellValue(item.getClasstitle());

            cell = row.createCell(7);
            cell.setCellValue(item.getTitle1());

            cell = row.createCell(8);
            cell.setCellValue(item.getTitle2());

            cell = row.createCell(9);
            cell.setCellValue(item.getTitle3());

        }

//
        return wb;
    }
}
