package app.unitsdata.wellknown;

import app.unitsdata.model.School;
import app.unitsdata.repository.SchoolRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author igogo
 */
@RestController
@RequestMapping("/public")
public class ApplyFormController {

    private final Logger logger = LoggerFactory.getLogger(ApplyFormController.class);

    @Autowired
    SchoolRepository repository;

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public Boolean readItems(@RequestBody Optional<String> payload, HttpServletResponse response) throws JsonProcessingException, IOException {
        if (!payload.isPresent()) {
            return false;
        } else {
            logger.info(payload.get());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(payload.get());
            Optional<String> oid = Optional.of(node.get("oid").asText());
//        logger.info(node.get("oid").asText());
            logger.info("eduid:" + node.get("eduid").asText());
//        List<School> schools = repository.findByOid(node.get("oid").asText());
            List<School> schools = repository.findByOid(oid.orElse(" "));
            Boolean isMatch = schools.stream().anyMatch(school -> school.getEduid().equals(node.get("eduid").asText()));

            return isMatch;

        }

    }

    @RequestMapping(value = "/form/download", method = RequestMethod.POST)
    public ResponseEntity<Resource> downloadItems(@RequestBody Optional<String> payload, HttpServletResponse response) throws IOException {
        String rowtitle;
//        response.setContentType("application/pdf");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(payload.get());
        Optional<String> schoolname = Optional.of(node.get("schoolname").asText());
        Optional<String> address = Optional.of(node.get("address").asText());
        Optional<String> eduid = Optional.of(node.get("eduid").asText());
        Optional<String> tel = Optional.of(node.get("tel").asText());
        Optional<String> allowedip = Optional.of(node.get("allowedip").asText());
        Optional<String> contact = Optional.of(node.get("contact").asText());
        Optional<String> oid = Optional.of(node.get("oid").asText());

        //timestamp
        Instant instant = Instant.now();
        long timestamp = instant.getEpochSecond();
        ZonedDateTime now = instant.atZone(ZoneId.of("Asia/Taipei"));  //taipei時區
        String applyTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now);

        //step.1 寫入資料庫
        //step.2 產生pdf 檔
        logger.info("create pdf");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
        Document document = new Document(pdfDoc);

//        String DEST = String.format("/root/unitsdata/hello-%s.pdf", instant.getEpochSecond());
//        //Initialize PDF writer
//        PdfWriter writer = new PdfWriter(DEST);
//
//        //Initialize PDF document
//        PdfDocument pdf = new PdfDocument(writer);
        // Initialize document
        // Create a PdfFont
        PdfFont font = PdfFontFactory.createFont("KozMinPro-Regular", "UniJIS-UCS2-H", false);
        Paragraph head = new Paragraph();
        head.add("代理平臺申請表").setFont(font).setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER);
        document.add(head);

        // a table with three columns
        Paragraph title = new Paragraph();
        title.add(new Text("    ")).setFont(font).setFontSize(16).setTextAlignment(TextAlignment.LEFT);
        document.add(title);

        float[] columnWidths = {1, 5};
        Table table = new Table(columnWidths);
        table.setWidthPercent(100);

        //申請單位
        rowtitle = "申請單位";
        table.addCell(new Cell(1, 1).setWidth(60).setBackgroundColor(new DeviceGray(0.95f)).setFont(font).setTextAlignment(TextAlignment.CENTER).add(rowtitle));
        table.addCell(new Cell(1, 1).setFont(font).setTextAlignment(TextAlignment.CENTER).add(schoolname.orElse("")));

        //地址
        rowtitle = "地址";
        table.addCell(new Cell(2, 1).setWidth(60).setBackgroundColor(new DeviceGray(0.95f)).setFont(font).setTextAlignment(TextAlignment.CENTER).add(rowtitle));
        table.addCell(new Cell(2, 1).setFont(font).setTextAlignment(TextAlignment.CENTER).add(address.orElse("")));

        //oid
        rowtitle = "oid";
        table.addCell(new Cell(3, 1).setWidth(60).setBackgroundColor(new DeviceGray(0.95f)).setFont(font).setTextAlignment(TextAlignment.CENTER).add(rowtitle));
        table.addCell(new Cell(3, 1).setFont(font).setTextAlignment(TextAlignment.CENTER).add(oid.orElse("")));

        //聯絡人
        rowtitle = "聯絡人";
        table.addCell(new Cell(4, 1).setWidth(60).setBackgroundColor(new DeviceGray(0.95f)).setFont(font).setTextAlignment(TextAlignment.CENTER).add(rowtitle));
        table.addCell(new Cell(4, 1).setFont(font).setTextAlignment(TextAlignment.CENTER).add(contact.orElse("")));

        //聯絡電話
        rowtitle = "聯絡電話";
        table.addCell(new Cell(5, 1).setWidth(60).setBackgroundColor(new DeviceGray(0.95f)).setFont(font).setTextAlignment(TextAlignment.CENTER).add(rowtitle));
        table.addCell(new Cell(5, 1).setFont(font).setTextAlignment(TextAlignment.CENTER).add(tel.orElse("")));

        //允許上線IP
        rowtitle = "允許上線IP";
        table.addCell(new Cell(6, 1).setWidth(60).setBackgroundColor(new DeviceGray(0.95f)).setFont(font).setTextAlignment(TextAlignment.CENTER).add(rowtitle));
        table.addCell(new Cell(6, 1).setFont(font).setTextAlignment(TextAlignment.CENTER).add(allowedip.orElse("")));

        //申請時間
        rowtitle = "申請時間";
        table.addCell(new Cell(7, 1).setWidth(60).setBackgroundColor(new DeviceGray(0.95f)).setFont(font).setTextAlignment(TextAlignment.CENTER).add(rowtitle));
        table.addCell(new Cell(7, 1).setFont(font).setTextAlignment(TextAlignment.CENTER).add(applyTime));

        Cell cell2 = new Cell(8, 6).setWidth(100).setTextAlignment(TextAlignment.CENTER).add("主管核章").setFont(font);
        Cell cell3 = new Cell(9, 6).setWidth(100).setTextAlignment(TextAlignment.CENTER).add("").setHeight(75f);
        table.addCell(cell2);
        table.addCell(cell3);

        document.add(table);
        document.close();
        logger.info("pdf file has been created ");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("charset", "utf-8");
        headers.setContentDispositionFormData("attachment", String.format("%s", "applyform.pdf"));
        headers.setContentType(MediaType.APPLICATION_PDF);
        Resource resource = new InputStreamResource(new ByteArrayInputStream(baos.toByteArray()));
        return ResponseEntity.ok().headers(headers).body(resource);
    }

}
