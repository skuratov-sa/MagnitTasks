package org.example;


import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.compress.utils.FileNameUtils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class FileProcessor implements Processor {
    private final Map<String, Integer> countFilesByPrefix;
    private final AtomicInteger countAllFiles;

    {
        countAllFiles = new AtomicInteger(0);
        countFilesByPrefix = new ConcurrentHashMap<>() {
            {
                put("txt", 0);
                put("xml", 0);
                put("other", 0);
            }
        };
    }

    @Override
    public void process(Exchange exchange) {
        String filename = getFilename(exchange);
        incrementMap(filename);

        if (countAllFiles.get() % 100 == 0) {
            printInfoMsg();
        }
    }

    private void printInfoMsg() {
        log.info("Файлов '.txt' -> " + countFilesByPrefix.get("txt"));
        log.info("Файлов '.xml' -> " + countFilesByPrefix.get("xml"));
        log.info("Других файлов  -> " + countFilesByPrefix.get("other"));
        log.info("Общее колличсество файлов -> " + countAllFiles);
    }

    private String getPrefixFilename(String filename) {
        return FileNameUtils.getExtension(filename);
    }

    private String getFilename(Exchange exchange) {
        return exchange.getIn().getBody(File.class).getName();
    }

    private void incrementMap(String filename) {
        String prefix = getPrefixFilename(filename);
        Integer countValue = countFilesByPrefix.get(prefix);

        if (countValue != null) {
            countFilesByPrefix.replace(prefix, ++countValue);
        } else {
            int countOther = countFilesByPrefix.get("other");
            countFilesByPrefix.replace("other", ++countOther);
            log.error(String.format("File '%s' is wrong format '.%s'", filename, prefix));
        }
        countAllFiles.incrementAndGet();
    }
}
