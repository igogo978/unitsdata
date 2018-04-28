/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.unitsdata.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author igogo
 */
@ConfigurationProperties("storage")
public class StorageProperties {

    private final Logger logger = LoggerFactory.getLogger(StorageProperties.class);

    //    private String location = "upload-dir";
    @Value("${multipart.location}")
    private String location;
//    private String location = System.getProperty("java.io.tmpdir");

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
