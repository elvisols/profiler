package com.lvs.profiler.service;

import com.lvs.profiler.model.Selection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface SelectionService {
    Page<Selection> findByAmenityId(int aId, Pageable pageRequest);

    Page<Selection> findByUserId(int uId, Pageable pageRequest);

    Page<Selection> findBySelectionLogtime(Date startDate, Date endDate, Pageable pageRequest);

    Page<Selection> findAllSelection(Pageable pageRequest);

}