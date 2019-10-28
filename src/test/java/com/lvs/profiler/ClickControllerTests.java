package com.lvs.profiler;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getClicksByUserHotelTest() throws Exception {
        SearchResponseSections searchResponseSections = new SearchResponseSections( null, null, null, false, null, null, 5 );
        SearchResponse searchResponse = new SearchResponse(searchResponseSections, null, 1, 1, 0, 2, new ShardSearchFailure[] {}, null );

        when(clickService.findByUserHotel(anyInt(), anyInt())).thenReturn(searchResponse);

        this.mockMvc.perform(get("/archive/click/{user_id}/hotel", 12).param("top", "2"))
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
                                )
                        )
                );
    }

    @Test
    void getClicksByUserHotelRegionTest() throws Exception {
        SearchResponseSections searchResponseSections = new SearchResponseSections( null, null, null, false, null, null, 5 );
        SearchResponse searchResponse = new SearchResponse(searchResponseSections, null, 1, 1, 0, 2, new ShardSearchFailure[] {}, null );

        when(clickService.findByUserHotelRegion(anyInt(), anyInt())).thenReturn(searchResponse);

        this.mockMvc.perform(get("/archive/click/{user_id}/hotel-region", 12).param("top", "2"))
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
                                )
                        )
                );
    }

}
