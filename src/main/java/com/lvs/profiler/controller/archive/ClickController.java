package com.lvs.profiler.controller.archive;

import alexh.weak.Dynamic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvs.profiler.controller.Response;
import com.lvs.profiler.model.Click;
import com.lvs.profiler.service.ClickService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("archive")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClickController {

    private final ClickService clickService;

    private final Response response;

    /**
     * This method fetches all click
     *
     * @param pageable
     * @method GET
     * @return result as list of paginated clickObject
     */
    @GetMapping(value="click", produces= MediaType.APPLICATION_JSON_VALUE)
    public Page<Click> findClicks(Pageable pageable) {

        return clickService.findAllClick(pageable);

    }

    /**
     * This method fetches all click by user hotel
     *
     * @param top Top clicked hotel
     * @param user_id user id
     * @method GET
     * @return result as list of paginated clickObject
     */
    @GetMapping(value="click/{user_id}/hotel", produces= MediaType.APPLICATION_JSON_VALUE)
    public Response findByUserHotel(@PathVariable Long user_id, @RequestParam(value = "top", defaultValue="5") int top) throws JsonProcessingException {

        SearchResponse searchResponse = clickService.findByUserHotel(user_id, top);

        return response.fetch(searchResponse);

    }

    /**
     * This method fetches all click by user hotel region
     *
     * @param user_id
     * @param top  top clicked hotel region
     * @method GET
     * @return result as list of paginated clickObject
     */
    @GetMapping(value="click/{user_id}/hotel-region", produces= MediaType.APPLICATION_JSON_VALUE)
    public Response findByUserHotelRegion(@PathVariable Long user_id, @RequestParam(value = "top", defaultValue="5") int top) throws JsonProcessingException {

        SearchResponse searchResponse = clickService.findByUserHotelRegion(user_id, top);

        return response.fetch(searchResponse);

    }

}
