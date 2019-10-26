package com.lvs.profiler.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Route extends RouteBuilder {

    @Value("${file.input.source}")
    private String filePath;

    @Override
    public void configure() throws Exception {
                from("file:{{file.input.source}}")
                .log("...file received!!!");
    }

}