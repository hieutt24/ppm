package org.hil.ppm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.hil.ppm.domain.Region;
import org.hil.ppm.repository.RegionRepository;
import org.hil.ppm.repository.search.RegionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Region.
 */
@RestController
@RequestMapping("/api")
public class RegionResource {

    private final Logger log = LoggerFactory.getLogger(RegionResource.class);

    @Inject
    private RegionRepository regionRepository;

    @Inject
    private RegionSearchRepository regionSearchRepository;

    /**
     * POST  /regions -> Create a new region.
     */
    @RequestMapping(value = "/regions",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Region region) throws URISyntaxException {
        log.debug("REST request to save Region : {}", region);
        if (region.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new region cannot already have an ID").build();
        }
        region.setActive(true);
        regionRepository.save(region);
        regionSearchRepository.save(region);
        return ResponseEntity.created(new URI("/api/regions/" + region.getId())).build();
    }

    /**
     * PUT  /regions -> Updates an existing region.
     */
    @RequestMapping(value = "/regions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Region region) throws URISyntaxException {
        log.debug("REST request to update Region : {}", region);
        if (region.getId() == null) {
            return create(region);
        }
        regionRepository.save(region);
        regionSearchRepository.save(region);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /regions -> get all the regions.
     */
    @RequestMapping(value = "/regions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Region> getAll() {
        log.debug("REST request to get all Regions");
        return regionRepository.findAllByActiveIsTrue();
    }

    /**
     * GET  /regions/:id -> get the "id" region.
     */
    @RequestMapping(value = "/regions/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Region> get(@PathVariable Long id) {
        log.debug("REST request to get Region : {}", id);
        return Optional.ofNullable(regionRepository.findOne(id))
            .map(region -> new ResponseEntity<>(
                region,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /regions/:id -> delete the "id" region.
     */
    @RequestMapping(value = "/regions/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Region : {}", id);
        Region region = regionRepository.findOne(id);
        if (region != null) {
        	region.setActive(false);
        	regionRepository.save(region);
            regionSearchRepository.save(region);
        }
//        regionRepository..delete(id);
//        regionSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/regions/:query -> search for the region corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/regions/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Region> search(@PathVariable String query) {
        return StreamSupport
            .stream(regionSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
