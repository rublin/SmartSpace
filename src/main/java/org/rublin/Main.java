package org.rublin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableScheduling
public class Main {

    static {
        ApiContextInitializer.init();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}