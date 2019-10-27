package com.lvs.profiler.service;

import com.lvs.profiler.model.Click;
import com.lvs.profiler.repository.ClickRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class ClickServiceImpl implements ClickService {

    @Autowired
    private ClickRepository clickRepository;

    @Override
    public Page<Click> findByHotelId(int hId, Pageable pageRequest) {
        return clickRepository.findByHotelId(hId, pageRequest);
    }

    @Override
    public Page<Click> findByUserId(int uId, Pageable pageRequest) {
        return clickRepository.findByUserId(uId, pageRequest);
    }

    @Override
    public Page<Click> findByHotelRegion(String hRegion, Pageable pageRequest) {
        return clickRepository.findByHotelRegion(hRegion, pageRequest);
    }

    @Override
    public Page<Click> findByClickLogtime(Date startDate, Date endDate, Pageable pageRequest) {
        LocalDateTime sd = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime ed = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());

        return clickRepository.findByLogtime(sd, ed, pageRequest);
    }

    @Override
    public Page<Click> findAllClick(Pageable pageRequest) {
        return clickRepository.findAll(pageRequest);
    }
}
