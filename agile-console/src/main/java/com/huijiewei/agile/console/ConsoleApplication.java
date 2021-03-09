package com.huijiewei.agile.console;

import com.huijiewei.agile.console.command.*;
import lombok.RequiredArgsConstructor;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author huijiewei
 */

@SpringBootApplication(proxyBeanMethods = false, scanBasePackages = "com.huijiewei.agile")
@EntityScan(basePackages = "com.huijiewei.agile")
@RequiredArgsConstructor
public class ConsoleApplication implements CommandLineRunner {
    private final UserFakeCommand userFakeCommand;
    private final UserAddressFakeCommand userAddressFakeCommand;
    private final DistrictImportCommand districtImportCommand;
    private final DistrictClosureCommand districtClosureCommand;
    private final DatabaseInitCommand databaseInitCommand;

    public static void main(String[] args) {
        SpringApplication.exit(SpringApplication.run(ConsoleApplication.class, args));
    }

    @Override
    public void run(String... args) {
        var textIO = TextIoFactory.getTextIO();

        var commands = Arrays.asList(
                this.userFakeCommand,
                this.userAddressFakeCommand,
                this.districtImportCommand,
                this.districtClosureCommand,
                this.databaseInitCommand
        );

        var app = textIO.<Consumer<TextIO>>newGenericInputReader(null)
                .withNumberedPossibleValues(commands)
                .read("选择一个命令运行");

        app.accept(textIO);
    }
}
