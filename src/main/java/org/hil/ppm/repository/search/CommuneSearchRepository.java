package org.hil.ppm.repository.search;

import org.hil.ppm.domain.Commune;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Commune entity.
 */
public interface CommuneSearchRepository extends ElasticsearchRepository<Commune, Long> {
}
