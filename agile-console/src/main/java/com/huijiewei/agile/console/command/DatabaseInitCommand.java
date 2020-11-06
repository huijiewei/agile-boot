package com.huijiewei.agile.console.command;

import com.huijiewei.agile.core.adapter.config.PrefixTableNamingStrategy;
import lombok.RequiredArgsConstructor;
import org.beryx.textio.TextIO;
import org.flywaydb.core.Flyway;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author huijiewei
 */

@Component
@RequiredArgsConstructor
public class DatabaseInitCommand implements Consumer<TextIO> {
    private final DataSource dataSource;
    private final Environment environment;

    @Override
    @SuppressWarnings("checked")
    public void accept(TextIO textIO) {
        textIO.getTextTerminal().println("开始初始化数据库...");

        var locations = environment.getProperty("spring.flyway.locations", List.class);

        assert locations != null;

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations((String) locations.get(0))
                .placeholders(Map.of("table-prefix", PrefixTableNamingStrategy.tablePrefix))
                .load();

        flyway.migrate();

        textIO.getTextTerminal().println("初始化数据库完成。");
    }

    @Override
    public String toString() {
        return "初始化数据库";
    }
}
