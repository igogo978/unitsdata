/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.unitsdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author igogo
 */
@Controller
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/")
    public RedirectView index(HttpServletRequest request) {
        logger.info("index page, but redirect to home");
        logger.info(request.getHeader("x-forwarded-for"));
        return new RedirectView("home");
    }

    @GetMapping("/login")
    public String login() {
        logger.info("login page");
        return "login";
    }


}
