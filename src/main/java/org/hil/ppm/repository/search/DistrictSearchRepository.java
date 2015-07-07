package org.hil.ppm.repository.search;

import org.hil.ppm.domain.District;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the District entity.
 */
public interface DistrictSearchRepository extends ElasticsearchRepository<District, Long> {
}
