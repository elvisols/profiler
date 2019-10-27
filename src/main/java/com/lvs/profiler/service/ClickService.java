package com.lvs.profiler.service;

import com.lvs.profiler.model.Click;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface ClickService {

    Page<Click> findByHotelId(int hId, Pageable pageRequest);

    Page<Click> findByUserId(int uId, Pageable pageRequest);

    Page<Click> findByHotelRegion(String hRegion, Pageable pageRequest);

    Page<Click> findByClickLogtime(Date startDate, Date endDate, Pageable pageRequest);

    Page<Click> findAllClick(Pageable pageRequest);

}
