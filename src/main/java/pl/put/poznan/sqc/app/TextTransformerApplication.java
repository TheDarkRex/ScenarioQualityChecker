package pl.put.poznan.sqc.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"pl.put.poznan.sqc.rest"})
public class TextTransformerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TextTransformerApplication.class, args);
    }
}
