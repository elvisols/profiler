package com.lvs.profiler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvs.profiler.controller.Response;
import com.lvs.profiler.controller.archive.SelectionController;
import com.lvs.profiler.service.SelectionService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchResponseSections;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.junit.Rule;
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
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@EnableSpringDataWebSupport
@ActiveProfiles(value="test")
@WebMvcTest(SelectionController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets", uriHost = "localhost", uriPort = 8999)
public class SelectionControllerTests {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SelectionService selectionService;

    @MockBean
    private Response response;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getSelectionsByUserTest() throws Exception {
        List<Map> buckets = new ArrayList<>();
        Map item = new HashMap();
        item.put("key", 123);
        item.put("doc_count", 7);
        item.put("top_amenity_hits", new HashMap<>());
        buckets.add(item);
        Response res = new Response(1, buckets);
        SearchResponseSections searchResponseSections = new SearchResponseSections( null, null, null, false, null, null, 5 );
        SearchResponse searchResponse = new SearchResponse(searchResponseSections, null, 1, 1, 0, 2, new ShardSearchFailure[] {}, null );

        when(selectionService.findByUserAmenity(anyInt(), anyInt())).thenReturn(searchResponse);
        when(response.fetch(searchResponse)).thenReturn(res);

        this.mockMvc.perform(get("/archive/selection/{user_id}", 12).param("top", "2"))
                .andExpect(status().isOk())
                .andDo(
                        document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("user_id").description("Application user id")
                                ),
                                requestParameters(
                                        parameterWithName("top").description("Top amenities selected count.").optional()
                                ),
                                responseFields(
                                        fieldWithPath("buckets[].key").description("Amenity id under review").type(JsonFieldType.NUMBER),
                                        fieldWithPath("buckets[].doc_count").description("Hit count").type(JsonFieldType.NUMBER),
                                        fieldWithPath("buckets[].top_amenity_hits").description("Top amenity hit aggregations").type(JsonFieldType.OBJECT),
                                        fieldWithPath("total_item_count").description("Size of the returned payload").type(JsonFieldType.NUMBER)
                                )
                        )
                );

    }

}
