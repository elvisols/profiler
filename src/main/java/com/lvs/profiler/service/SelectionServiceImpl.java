package com.lvs.profiler.service;

import com.lvs.profiler.model.Selection;
import com.lvs.profiler.repository.SelectionRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SelectionServiceImpl implements SelectionService {

    @Autowired
    private SelectionRepository selectionRepository;

    @Autowired
    Client client;

    @Override
    public SearchResponse findByUserAmenity(int uId, int topCount) {
        return client.prepareSearch("user-selections")
                .setTypes("selection")
                .addAggregation(
                        AggregationBuilders
                                .filter("user", QueryBuilders.termQuery("userId", uId))
                                .subAggregation(AggregationBuilders
                                        .terms("amenities").field("amenityId").size(topCount)
                                        .subAggregation(AggregationBuilders.topHits("top_amenity_hits").sort("timestamp", SortOrder.DESC))
                                )
                )
                .setSize(0)
                .get();

    }

    @Override
    public Page<Selection> findAllSelection(Pageable pageRequest) {
        return selectionRepository.findAll(pageRequest);
    }

}
