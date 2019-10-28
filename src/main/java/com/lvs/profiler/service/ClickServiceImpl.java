package com.lvs.profiler.service;

import com.lvs.profiler.model.Click;
import com.lvs.profiler.repository.ClickRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class ClickServiceImpl implements ClickService {

    @Autowired
    private ClickRepository clickRepository;

    @Autowired
    Client client;

    @Override
    public SearchResponse findByUserHotel(int uId, int topCount) {
        return client.prepareSearch("user-clicks")
                .setTypes("click")
                .addAggregation(
                        AggregationBuilders
                                .filter("user", QueryBuilders.termQuery("userId", uId))
                                .subAggregation(AggregationBuilders
                                        .terms("hotels").field("hotelId").size(topCount)
                                        .subAggregation(AggregationBuilders.topHits("top_hotel_hits").sort("timestamp", SortOrder.DESC))
                                )
                )
                .setSize(0)
                .get();

    }

    @Override
    public SearchResponse findByUserHotelRegion(int uId, int topCount) {
        return client.prepareSearch("user-clicks")
                .setTypes("click")
                .addAggregation(
                        AggregationBuilders
                                .filter("user", QueryBuilders.termQuery("userId", uId))
                                .subAggregation(AggregationBuilders
                                        .terms("hotels").field("hotelRegion.keyword").size(topCount)
                                        .subAggregation(AggregationBuilders.topHits("top_hotel_region_hits").sort("timestamp", SortOrder.DESC))
                                )
                )
                .setSize(0)
                .get();

    }

    @Override
    public Page<Click> findAllClick(Pageable pageRequest) {
        return clickRepository.findAll(pageRequest);
    }

}
