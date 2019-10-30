package com.lvs.profiler.service;

import com.lvs.profiler.model.Click;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClickService {

    SearchResponse findByUserHotel(Long uId, int topCount);

    SearchResponse findByUserHotelRegion(Long uId, int topCount);

    Page<Click> findAllClick(Pageable pageRequest);

}
