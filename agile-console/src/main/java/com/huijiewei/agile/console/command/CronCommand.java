package com.huijiewei.agile.console.command;

import org.beryx.textio.TextIO;

import java.util.function.Consumer;

/**
 * @author huijiewei
 */

public class CronCommand implements Consumer<TextIO> {
    @Override
    public void accept(TextIO textIO) {
        textIO.getTextTerminal().println("cron");
        textIO.dispose();
    }

    @Override
    public String toString() {
        return "计划任务";
    }
}
