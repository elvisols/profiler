package com.lvs.profiler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvs.profiler.controller.Response;
import com.lvs.profiler.controller.archive.ClickController;
import com.lvs.profiler.service.ClickService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchResponseSections;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@EnableSpringDataWebSupport
@ActiveProfiles(value="test")
@WebMvcTest(ClickController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets", uriHost = "localhost", uriPort = 8999)
public class ClickControllerTests {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClickService clickService;

    @MockBean
    private Response response;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getClicksByUserHotelTest() throws Exception {
        List<Map> buckets = new ArrayList<>();
        Map item = new HashMap();
        item.put("key", 323);
        item.put("doc_count", 5);
        item.put("top_hotel_hits", new HashMap<>());
        buckets.add(item);
        Response res = new Response(1, buckets);
        SearchResponseSections searchResponseSections = new SearchResponseSections( null, null, null, false, null, null, 5 );
        SearchResponse searchResponse = new SearchResponse(searchResponseSections, null, 1, 1, 0, 2, new ShardSearchFailure[] {}, null );

        when(clickService.findByUserHotel(anyLong(), anyInt())).thenReturn(searchResponse);
        when(response.fetch(searchResponse)).thenReturn(res);

        this.mockMvc.perform(get("/archive/click/{user_id}/hotel", 9).param("top", "2"))
                .andExpect(status().isOk())
                .andDo(
                        document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("user_id").description("Application user id")
                                ),
                                requestParameters(
                                        parameterWithName("top").description("Top hotels click count.").optional()
                                ),
                                responseFields(
                                        fieldWithPath("buckets[].key").description("Hotel id under review").type(JsonFieldType.NUMBER),
                                        fieldWithPath("buckets[].doc_count").description("Hit count").type(JsonFieldType.NUMBER),
                                        fieldWithPath("buckets[].top_hotel_hits").description("Top hotel hit aggregations").type(JsonFieldType.OBJECT),
                                        fieldWithPath("total_item_count").description("Size of the returned payload").type(JsonFieldType.NUMBER)
                                )
                        )
                );
    }

    @Test
    void getClicksByUserHotelRegionTest() throws Exception {
        List<Map> buckets = new ArrayList<>();
        Map item = new HashMap();
        item.put("key", "LAGOS");
        item.put("doc_count", 12);
        item.put("top_hotel_region_hits", new HashMap<>());
        buckets.add(item);
        Response res = new Response(1, buckets);

        SearchResponseSections searchResponseSections = new SearchResponseSections( null, null, null, false, null, null, 5 );
        SearchResponse searchResponse = new SearchResponse(searchResponseSections, null, 1, 1, 0, 2, new ShardSearchFailure[] {}, null );

        when(clickService.findByUserHotelRegion(anyLong(), anyInt())).thenReturn(searchResponse);
        when(response.fetch(searchResponse)).thenReturn(res);

        this.mockMvc.perform(get("/archive/click/{user_id}/hotel-region", 23).param("top", "2"))
                .andExpect(status().isOk())
                .andDo(
                        document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("user_id").description("Application user id")
                                ),
                                requestParameters(
                                        parameterWithName("top").description("Top hotel regions clicked count.").optional()
                                ),
                                responseFields(
                                        fieldWithPath("buckets[].key").description("Hotel region under review").type(JsonFieldType.STRING),
                                        fieldWithPath("buckets[].doc_count").description("Hit count").type(JsonFieldType.NUMBER),
                                        fieldWithPath("buckets[].top_hotel_region_hits").description("Top hotel region hit aggregations").type(JsonFieldType.OBJECT),
                                        fieldWithPath("total_item_count").description("Size of the returned payload").type(JsonFieldType.NUMBER)
                                )
                        )
                );
    }

}
