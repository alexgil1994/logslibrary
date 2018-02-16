package com.amarkovits.logslibrary;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogsLibrary {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger("logFiles");
        logger.debug("hello.");

        logger.error("its an error");
        logger.warn("warning ....");
        logger.info("fesfesff info");
        logger.trace("trace this log");

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(loggerContext);

        PostLog postLog = new PostLog();
    }
}
