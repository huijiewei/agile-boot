package com.huijiewei.agile.console;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author huijiewei
 */

@SpringBootApplication
public class ConsoleApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ConsoleApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Hello Spring Boot from console!");

        System.exit(0);
    }
}
