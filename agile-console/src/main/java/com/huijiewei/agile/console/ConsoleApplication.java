package com.huijiewei.agile.console;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import com.huijiewei.agile.console.command.CronCommand;
import com.huijiewei.agile.console.command.FakeUserCommand;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author huijiewei
 */

@SpringBootApplication
@ComponentScan(basePackages = "com.huijiewei.agile")
@EntityScan(basePackages = "com.huijiewei.agile")
@EnableJpaRepositories(
        repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class,
        basePackages = "com.huijiewei.agile",
        bootstrapMode = BootstrapMode.DEFERRED
)
public class ConsoleApplication implements CommandLineRunner {
    private final FakeUserCommand fakeUserCommand;

    public ConsoleApplication(FakeUserCommand fakeUserCommand) {
        this.fakeUserCommand = fakeUserCommand;
    }

    public static void main(String[] args) {
        SpringApplication.exit(SpringApplication.run(ConsoleApplication.class, args));
    }

    @Override
    public void run(String... args) {
        TextIO textIO = TextIoFactory.getTextIO();

        List<Consumer<TextIO>> commands = Arrays.asList(
                this.fakeUserCommand,
                new CronCommand()
        );

        Consumer<TextIO> app = textIO.<Consumer<TextIO>>newGenericInputReader(null)
                .withNumberedPossibleValues(commands)
                .read("Choose the application to be run");

        app.accept(textIO);
    }
}
