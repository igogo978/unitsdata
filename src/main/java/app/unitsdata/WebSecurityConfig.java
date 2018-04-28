/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.unitsdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

/**
 *
 * @author igogo
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    CustomizeAuthenticationSuccessHandler successHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/public/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/admin", "/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successHandler)
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
        http
                .csrf().disable().exceptionHandling().accessDeniedPage("/error");

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        logger.info("username:" + username);

        auth
                .jdbcAuthentication()
                .dataSource(dataSource).passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("SELECT username, passwd, enabled  FROM users WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT username, role FROM users WHERE username = ?");

    }

}
