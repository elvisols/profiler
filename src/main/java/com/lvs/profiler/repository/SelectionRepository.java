package com.lvs.profiler.repository;

import com.lvs.profiler.model.Selection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectionRepository extends ElasticsearchRepository<Selection, String> {

    Page<Selection> findAll(Pageable pageable);

}
