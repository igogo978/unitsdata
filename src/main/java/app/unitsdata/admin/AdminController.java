package app.unitsdata.admin;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/home")
    public String adminHome() {
        return "admin/home";
    }
}
