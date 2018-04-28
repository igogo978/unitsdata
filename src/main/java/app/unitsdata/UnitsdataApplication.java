package app.unitsdata;

import app.unitsdata.model.User;
import app.unitsdata.model.UserMapper;
import app.unitsdata.storage.StorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class UnitsdataApplication implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(UnitsdataApplication.class);

    @Autowired
    JdbcTemplate jdbctemplate;


    public static void main(String[] args) {
        SpringApplication.run(UnitsdataApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        logger.info("哈喽,世界");
        jdbctemplate.execute("CREATE TABLE IF NOT EXISTS users("
                + "username VARCHAR(30) NOT NULL, "
                + "passwd VARCHAR(100) NOT NULL,"
                + "contact VARCHAR(10) NOT NULL,"
                + "unit VARCHAR(20) NOT NULL,"
                + "phone VARCHAR (20) NOT NULL,"
                + "email VARCHAR (20) NOT NULL,"
                + "schoolid VARCHAR(6) NOT NULL,"
                + "role VARCHAR (10) NOT NULL,"
                + "enabled BOOLEAN DEFAULT TRUE,"
                + "PRIMARY KEY(username)"
                + ")");

        logger.info("query users...");
        List<User> users = jdbctemplate.query("select * FROM users", new UserMapper());
        users.forEach(u -> logger.info("查詢:" + u.getUsername()));
        logger.info(String.valueOf(users.size()));


    }
}
