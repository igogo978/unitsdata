/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.unitsdata;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author igogo
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
//        registry.addViewController("/").setViewName("index");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/public/popup").setViewName("popup");
        registry.addViewController("/public/applyform").setViewName("applyform");
    }
}
