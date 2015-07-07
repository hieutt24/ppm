package org.hil.ppm.repository.search;

import org.hil.ppm.domain.Region;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Region entity.
 */
public interface RegionSearchRepository extends ElasticsearchRepository<Region, Long> {
}
