package org.example;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.Objects;

@Slf4j
public class ApplicationRunner {
    private final static String property = "application.properties";

    @SneakyThrows
    public static void main(String[] args) {
        CamelContext context = new DefaultCamelContext();
        context.getPropertiesComponent().setLocation(property);
        FileProcessor processor = new FileProcessor();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws RuntimeException {
                from("file:{{filesDir}}")
                        .choice()
                        .when().simple("${file:name} endsWith 'xml'")
                            .process(processor)
                            .to("activemq:queue:FOO")
                        .when().simple("${file:name} endsWith 'txt'")
                            .process(processor)
                            .to("activemq:queue:FOO")
                        .otherwise()
                            .process(processor)
                            .end();

            }
        });

        context.start();
        Thread.sleep(3000);
        context.stop();
    }
}