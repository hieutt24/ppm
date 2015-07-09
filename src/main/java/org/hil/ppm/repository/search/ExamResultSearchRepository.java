package org.hil.ppm.repository.search;

import org.hil.ppm.domain.ExamResult;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ExamResult entity.
 */
public interface ExamResultSearchRepository extends ElasticsearchRepository<ExamResult, Long> {
}
