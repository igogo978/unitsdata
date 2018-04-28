/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.unitsdata;

import app.unitsdata.model.Item;
import app.unitsdata.repository.ItemsRepository;
import app.unitsdata.service.CreateXLSX;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author igogo
 */
@RestController
@RequestMapping("/home/items")
public class ItemsCRUDController {

//    @Autowired
//    CheckLoginSessionService checkLoginService;

    @Autowired
    ItemsRepository itemsrepository;

    //    @Autowired
//    private UnitInfo unitInfo;
    @Autowired
    CreateXLSX createxlsx;

    private final Logger logger = LoggerFactory.getLogger(ItemsCRUDController.class);

    //    @RequestMapping(value = "/{keywords}", method = RequestMethod.GET)
//    public String fetchItem(@PathVariable("keywords") String keywords) {
//        if (checkLoginService.isLogin()) {
//            return "hello";
//        } else {
//            logger.info("session not exists");
//            return "forbidden";
//        }
//    }
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public String updateItems(@RequestBody List<Item> items, Principal principal) throws JsonProcessingException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        items.forEach(item -> {
            item.setCreator(principal.getName());
        });
        if (auth != null) {
            ObjectMapper mapper = new ObjectMapper();
            logger.info(String.format("db 目前筆數%d", itemsrepository.countAll()));
            int results = itemsrepository.updateItems(items);
//            logger.info(mapper.writeValueAsString(items));
//            return mapper.writeValueAsString(items);

            return String.format("{ \"success\" : %d}", results);

        } else {
            logger.info("session not exists");
            return "forbidden";
        }

    }

    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public ResponseEntity<?> returnItemsCounts() {
        //提供總共筆數, 寫在header裡
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + itemsrepository.countAll());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/read", method = RequestMethod.POST)
    public String readItems(@RequestBody String payload, HttpServletResponse response) throws JsonProcessingException, IOException {

        logger.info("read api:" + payload);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(payload);
            logger.info("start:" + node.get("start"));
            int start = node.get("start").asInt();
            int rows = node.get("rows").asInt();

            logger.info("creator request read items:" + auth.getName());
            List<Item> items;
            if (rows == 0) {
                items = itemsrepository.findAll(auth.getName());

            } else {
                items = itemsrepository.findByLimit(auth.getName(), start, rows);
            }

//            logger.info(mapper.writeValueAsString(items));
            logger.info(String.valueOf(items.size()));
            return mapper.writeValueAsString(items);

//            return "read";
        } else {
            logger.info("session not exists");
            return "forbidden";
        }

    }

    //    @RequestMapping(value = "/download", method = RequestMethod.GET)
//    public String downloadItems() throws IOException {
//        return "download page";
//    }
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public ResponseEntity<Resource> downloadItems(@RequestBody List<Item> items, Principal principal) throws IOException {

//https://stackoverflow.com/questions/46141482/redirecting-by-setting-the-location-in-the-header
//use a permanent redirect (301 Moved Permanently
        ObjectMapper mapper = new ObjectMapper();
//        logger.info(mapper.writeValueAsString(items));
//        logger.info(String.format("共%s筆資料", items.size()));
        String filename = principal.getName();
//        String tmpdir = System.getProperty("java.io.tmpdir");
//        String xlsxFilename = String.format("%s/%s.xlsx", tmpdir, filename);
//        logger.info(xlsxFilename);

//直接輸出
        XSSFWorkbook wb = createxlsx.create(items);

        ByteArrayOutputStream resourceStream = new ByteArrayOutputStream();
        wb.write(resourceStream);
        wb.close();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("charset", "utf-8");
        headers.setContentDispositionFormData("attachment", String.format("%s.xlsx", filename));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        Resource resource = new InputStreamResource(new ByteArrayInputStream(resourceStream.toByteArray()));
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String deleteItems(@RequestBody String payload, Principal principal) throws IOException {
        logger.info(payload);
        //資料擁有者
        String creator = principal.getName();
        List< Item> deleteItems = new ArrayList<>();
//        @RequestBody String str
//        logger.info("delete items:" + ids);
        ObjectMapper mapper = new ObjectMapper();
        List<String> deletePids = Arrays.asList(mapper.readValue(payload, String[].class));
        deletePids.forEach(pid -> {
            Item item = new Item();
            item.setPersonGuid(pid);
            item.setCreator(creator);
            deleteItems.add(item);
        });

//        deleteItems.forEach(item -> logger.info(item.getPersonGuid()));
        int results = itemsrepository.deleteItems(deleteItems);

        return String.format("{ \"success\" : %d}", results);
    }

}
