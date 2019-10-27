package com.lvs.profiler.route;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvs.profiler.model.Click;
import com.lvs.profiler.model.Selection;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.elasticsearch.ElasticsearchConstants;
import org.apache.camel.component.elasticsearch.ElasticsearchOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class Route extends RouteBuilder {

    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
    ObjectMapper mapper = new ObjectMapper();
    @Value("${file.input.source}")
    private String filePath;

    // helper methods
    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

    @Override
    public void configure() throws Exception {
        /**
         * generic exception handler...
         */
        onException(Exception.class)
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                        log.debug("...Oops! exception caught!..." + cause.getMessage());
                    }
                });

        /**
         * transform csv to json before loading to kafka queue
         */
        interceptSendToEndpoint("kafka:{{kafka.topic.selections.csv}}?brokers={{kafka.server}}:{{kafka.port}}").process(new Processor() {
            public void process(Exchange exchange) throws JsonProcessingException {
                String body = exchange.getIn().getBody().toString();

                List<String> line = parseLine(body);

                Selection selection = new Selection(); // Use FlyWeight design pattern here to get related object

                selection.setTimestamp(Integer.valueOf(line.get(0)));
                selection.setUserId(Integer.valueOf(line.get(1)));
                selection.setAmenityId(Integer.valueOf(line.get(2)));

                exchange.getIn().setBody(mapper.writeValueAsString(selection));

            }
        });
        interceptSendToEndpoint("kafka:{{kafka.topic.clicks.csv}}?brokers={{kafka.server}}:{{kafka.port}}").process(new Processor() {
            public void process(Exchange exchange) throws JsonProcessingException {
                String body = exchange.getIn().getBody().toString();

                List<String> line = parseLine(body);

                Click click = new Click(); // Use FlyWeight design pattern here to get related object

                click.setTimestamp(Integer.valueOf(line.get(0)));
                click.setUserId(Integer.valueOf(line.get(1)));
                click.setHotelId(Integer.valueOf(line.get(2)));
                click.setHotelRegion(line.get(3));

                exchange.getIn().setBody(mapper.writeValueAsString(click));

            }
        });

        /**
         * load csv
         */
        from("file:{{file.input.source}}")
                .choice()
                .when(header("CamelFileName").startsWith("selections"))
                .log("...loading selections...")
                .to("direct:selections")

                .when(header("CamelFileName").startsWith("clicks"))
                .log("...loading clicks...")
                .to("direct:clicks");

        from("direct:selections")
                .split()
                .tokenize("\n")
//                .log("...selections...${body}")
                .to("kafka:{{kafka.topic.selections.csv}}?brokers={{kafka.server}}:{{kafka.port}}");

        from("direct:clicks")
                .split()
                .tokenize("\n")
//                .log("...clicks...${body}")
                .to("kafka:{{kafka.topic.clicks.csv}}?brokers={{kafka.server}}:{{kafka.port}}");

        /**
         * sink to ES for archiving
         */
        from("kafka:{{kafka.topic.clicks.csv}}?brokers={{kafka.server}}:{{kafka.port}}&autoOffsetReset=earliest")
                .transform(body().append("\n"))
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        try {
                            String json = exchange.getIn().getBody(String.class);

                            Map<String, Object> map = new HashMap<String, Object>();

                            map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});

                            exchange.getIn().setHeader(ElasticsearchConstants.PARAM_INDEX_NAME, "user-clicks");
                            exchange.getIn().setHeader(ElasticsearchConstants.PARAM_INDEX_TYPE, "click");
                            exchange.getIn().setHeader(ElasticsearchConstants.PARAM_OPERATION, ElasticsearchOperation.Index);
                            exchange.getIn().setBody(map);

                        } catch (JsonGenerationException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                })
                .to("elasticsearch-rest://{{elasticsearch.clustername}}");

        from("kafka:{{kafka.topic.selections.csv}}?brokers={{kafka.server}}:{{kafka.port}}&autoOffsetReset=earliest")
                .transform(body().append("\n"))
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        try {
                            String json = exchange.getIn().getBody(String.class);

                            Map<String, Object> map = new HashMap<String, Object>();

                            map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});

                            exchange.getIn().setHeader(ElasticsearchConstants.PARAM_INDEX_NAME, "user-selections");
                            exchange.getIn().setHeader(ElasticsearchConstants.PARAM_INDEX_TYPE, "selection");
                            exchange.getIn().setHeader(ElasticsearchConstants.PARAM_OPERATION, ElasticsearchOperation.Index);
                            exchange.getIn().setBody(map);

                        } catch (JsonGenerationException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                })
                .to("elasticsearch-rest://{{elasticsearch.clustername}}");

    }

}