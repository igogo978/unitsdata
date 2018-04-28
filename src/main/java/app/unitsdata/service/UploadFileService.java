/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.unitsdata.service;

import app.unitsdata.model.Item;
import app.unitsdata.storage.StorageProperties;
import app.unitsdata.storage.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

/**
 * @author igogo
 */

//讀出上傳的檔案內容
@RestController
@RequestMapping("/home/upload/file")
public class UploadFileService {

    private final Logger logger = LoggerFactory.getLogger(UploadFileService.class);
    private ObjectMapper mapper = new ObjectMapper();


    @Autowired
    StorageProperties storageProperties;

    private final StorageService storageService;

    @Autowired
    public UploadFileService(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(value = "/{filename}", method = RequestMethod.GET)
    public List<Item> readFile(@PathVariable("filename") String filename) {

//        檢查是否登入

        List items = new ArrayList<Item>();
//  logger.info("檔案：" + filename);
//        logger.info(storageProperties.getLocation());
//        logger.info(new String(Base64.getDecoder().decode(filename)));
        File file = new File(storageProperties.getLocation() + "/" + new String(Base64.getDecoder().decode(filename)));

        if (file.exists()) {
            logger.info("file exists");

            try {
                FileInputStream fis = new FileInputStream(file);
                Workbook workbook = WorkbookFactory.create(fis);

                // Return first sheet from the XLSX  workbook
                Sheet mySheet = workbook.getSheetAt(0);

                // Get iterator to all the rows in current sheet
                Iterator<Row> rowIterator = mySheet.iterator();

                //讀列
                while (rowIterator.hasNext()) {

                    Row row = rowIterator.next();

                    if (row.getRowNum() == 0) {  //ommit first row
                        continue;
                    }

                    //讀欄
                    // For each row, iterate through each columns
                    Iterator<Cell> cellIterator = row.cellIterator();

                    Item item = new Item();
//                    item.setCreator(unitInfo.getPid());
                    item.setCreator("igogo");
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        cell.setCellType(CellType.STRING);

//                    byte[] byteArray;
                        switch (cell.getColumnIndex()) {
                            case 0:    //第一個欄位, 姓名
//                            byteArray = cell.getStringCellValue().getBytes(Charset.forName("UTF-8"));
//                            astudent.setFullName(new String(byteArray, "UTF-8"));
                                logger.info(cell.getStringCellValue());
                                String value = String.valueOf(cell.getStringCellValue());
                                item.setFullName(value);

                                break;
                            case 1: //身份證字號
//                            byteArray = cell.getStringCellValue().getBytes(Charset.forName("UTF-8"));
//                            astudent.setPersonGuid(new String(byteArray, "UTF-8").toUpperCase());
//                            System.out.print(cell.getStringCellValue());
                                logger.info(cell.getStringCellValue());
                                item.setPersonGuid(cell.getStringCellValue());
                                break;
                            case 2: //單位代碼
//                            byteArray = cell.getStringCellValue().getBytes(Charset.forName("UTF-8"));
//                            astudent.setSchoolID(new String(byteArray, "UTF-8").toUpperCase());
//                            System.out.print(cell.getStringCellValue());
                                logger.info(cell.getStringCellValue());
                                item.setSchoolID(cell.getStringCellValue());
                                break;
                            case 3: //學期
                                item.setSemester(cell.getStringCellValue());
                                break;

                            case 4: //年級
//                            byteArray = cell.getStringCellValue().getBytes(Charset.forName("UTF-8"));
//                            astudent.setClasstitle(new String(byteArray, "UTF-8"));
                                logger.info(cell.getStringCellValue());
                                item.setGrade(cell.getStringCellValue());
                                break;

                            case 5: //班級
//                            byteArray = cell.getStringCellValue().getBytes(Charset.forName("UTF-8"));
//                            astudent.setYear(new String(byteArray, "UTF-8"));

                                logger.info(String.format("%010d", Integer.parseInt(cell.getStringCellValue())));
                                item.setClassno(String.format("%010d", Integer.parseInt(cell.getStringCellValue())));
                                break;

                            case 6: //班級名稱
//                            byteArray = cell.getStringCellValue().getBytes(Charset.forName("UTF-8"));
//                            astudent.setSemester(new String(byteArray, "UTF-8"));
                                item.setClasstitle(cell.getStringCellValue());
                                break;

                            case 7: //職稱1
//                            byteArray = cell.getStringCellValue().getBytes(Charset.forName("UTF-8"));
//                            astudent.setGrade(new String(byteArray, "UTF-8"));
                                item.setTitle1(cell.getStringCellValue());
                                break;
                            case 8: //職稱2
//                            byteArray = cell.getStringCellValue().getBytes(Charset.forName("UTF-8"));
//                            astudent.setClassno(new String(byteArray, "UTF-8"));
                                item.setTitle2(cell.getStringCellValue());
                                break;

                            case 9: //職稱3
//                            byteArray = cell.getStringCellValue().getBytes(Charset.forName("UTF-8"));
//                            astudent.setClassno(new String(byteArray, "UTF-8"));
                                item.setTitle3(cell.getStringCellValue());
                                break;

                            default:

                        } //end switch

                    }  //end while (cellIterator.hasNext())

                    items.add(item);
                } //end wihle  (rowIterator.hasNext())
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.info(ex.getMessage());
            }

        } // end if
        try {
            logger.info(mapper.writeValueAsString(items));
            logger.info("內容已讀出,應刪除上傳檔案：");
            file.delete();
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            logger.info(ex.getMessage());
        }
        return items;
    }

}
