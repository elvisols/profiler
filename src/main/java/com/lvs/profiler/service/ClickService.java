package com.lvs.profiler.service;

import com.lvs.profiler.model.Click;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface ClickService {

    SearchResponse findByUserHotel(int uId, int topCount);

    SearchResponse findByUserHotelRegion(int uId, int topCount);

    Page<Click> findAllClick(Pageable pageRequest);

}
