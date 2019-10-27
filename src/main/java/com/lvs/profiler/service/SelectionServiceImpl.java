package com.lvs.profiler.service;

import com.lvs.profiler.model.Selection;
import com.lvs.profiler.repository.SelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class SelectionServiceImpl implements SelectionService {

    @Autowired
    private SelectionRepository selectionRepository;

    @Override
    public Page<Selection> findByAmenityId(int aId, Pageable pageRequest) {
        return selectionRepository.findByAmenityId(aId, pageRequest);
    }

    @Override
    public Page<Selection> findByUserId(int uId, Pageable pageRequest) {
        return selectionRepository.findByUserId(uId, pageRequest);
    }

    @Override
    public Page<Selection> findBySelectionLogtime(Date startDate, Date endDate, Pageable pageRequest) {
        LocalDateTime sd = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime ed = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());

        return selectionRepository.findByLogtime(sd, ed, pageRequest);
    }

    @Override
    public Page<Selection> findAllSelection(Pageable pageRequest) {
        return selectionRepository.findAll(pageRequest);
    }
}
