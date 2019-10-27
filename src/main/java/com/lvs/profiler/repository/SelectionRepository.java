package com.lvs.profiler.repository;

import com.lvs.profiler.model.Selection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SelectionRepository extends ElasticsearchRepository<Selection, String> {

    Page<Selection> findByAmenityId(int aId, Pageable pageable);
    Page<Selection> findByUserId(int uId, Pageable pageable);

    @Query("{"
            + "\"range\": {"
            +	"\"logtime\": {"
            +		"\"gte\": \"?0\","
            +		"\"lte\": \"?1\""
            +	"}"
            + "}"
            + "}")
    Page<Selection> findByLogtime(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Selection> findAll(Pageable pageable);

}
