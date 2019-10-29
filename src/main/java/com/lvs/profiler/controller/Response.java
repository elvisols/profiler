package com.lvs.profiler.controller;

import alexh.weak.Dynamic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lvs.profiler.util.Chain;
import com.lvs.profiler.util.Singleton;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.elasticsearch.action.search.SearchResponse;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class Response {

    private int total_item_count;
    private List buckets;

    public static Response wrapper(SearchResponse searchResponse) throws JsonProcessingException {

        Map jsonMap = Singleton.getObjectMapper().readValue(searchResponse.getAggregations().getAsMap().get("user").toString(), Map.class);

        Dynamic jsonData = Dynamic.from(jsonMap);

        Chain hotelEntity = Singleton.getHotelResponse();
        Chain amenityEntity = Singleton.getAmenityResponse();

        hotelEntity.setNextChain(amenityEntity);

        return hotelEntity.getResponse(jsonData);

    }

}
