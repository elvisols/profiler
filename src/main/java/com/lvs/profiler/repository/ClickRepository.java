package com.lvs.profiler.repository;

import com.lvs.profiler.model.Click;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClickRepository extends ElasticsearchRepository<Click, String> {

    Page<Click> findAll(Pageable pageable);

}

