package com.lvs.profiler.service;

import com.lvs.profiler.model.Selection;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SelectionService {

    SearchResponse findByUserAmenity(Long uId, int topCount);

    Page<Selection> findAllSelection(Pageable pageRequest);

}