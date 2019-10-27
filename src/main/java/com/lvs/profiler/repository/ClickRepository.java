package com.lvs.profiler.repository;

import com.lvs.profiler.model.Click;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ClickRepository extends ElasticsearchRepository<Click, String> {

    Page<Click> findByHotelId(int hId, Pageable pageable);
    Page<Click> findByUserId(int uId, Pageable pageable);
    Page<Click> findByHotelRegion(String hRegion, Pageable pageable);

    @Query("{"
            + "\"range\": {"
            +	"\"timestamp\": {"
            +		"\"gte\": \"?0\","
            +		"\"lte\": \"?1\""
            +	"}"
            + "}"
            + "}")
    Page<Click> findByLogtime(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Click> findAll(Pageable pageable);

}

