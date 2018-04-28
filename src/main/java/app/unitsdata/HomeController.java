package app.unitsdata;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import app.unitsdata.storage.StorageFileNotFoundException;
import app.unitsdata.storage.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author igogo
 */
@Controller
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);


    private final StorageService storageService;

    @Autowired
    public HomeController(StorageService storageService) {
        this.storageService = storageService;
    }



    @GetMapping("/home")
    public String home(Model model) throws JsonProcessingException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getAuthorities().forEach(role -> {
            if (role.getAuthority().equals("ROLE_ADMIN")) {
                logger.info("admin page");
            };

        });

        logger.info("home page");
        return "home";

    }

    @GetMapping("/home/view")
    public String read(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//        if (auth != null) {
        model.addAttribute("username", auth.getName());

        return "view";
//        } else {
//            logger.info("session not exists");
//            return "redirect:/login";
//
//        }
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename
    ) {
        //讀出上傳檔案內容
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc
    ) {
        return ResponseEntity.notFound().build();
    }

}
