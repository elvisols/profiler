package com.lvs.profiler.repository;

import com.lvs.profiler.model.Click;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Repository
public interface ClickRepository extends ElasticsearchRepository<Click, String> {

    Page<Click> findAll(Pageable pageable);

}

