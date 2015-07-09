package org.hil.ppm.repository.search;

import org.hil.ppm.domain.PrivateMedicalAgent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PrivateMedicalAgent entity.
 */
public interface PrivateMedicalAgentSearchRepository extends ElasticsearchRepository<PrivateMedicalAgent, Long> {
}
