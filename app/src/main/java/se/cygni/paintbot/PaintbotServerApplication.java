package se.cygni.paintbot;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaintbotServerApplication {
    public static void main(String[] args) {

        // If no active profile is set, default to development!
        if (StringUtils.isEmpty(System.getProperty("spring.profiles.active"))) {
            System.setProperty("spring.profiles.active", "development");
        }

        SpringApplication.run(PaintbotServerApplication.class, args);
    }
}
