package org.hil.ppm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.hil.ppm.domain.District;
import org.hil.ppm.domain.Province;
import org.hil.ppm.repository.DistrictRepository;
import org.hil.ppm.repository.ProvinceRepository;
import org.hil.ppm.repository.search.DistrictSearchRepository;
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
 * REST controller for managing District.
 */
@RestController
@RequestMapping("/api")
public class DistrictResource {

    private final Logger log = LoggerFactory.getLogger(DistrictResource.class);

    @Inject
    private DistrictRepository districtRepository;
    
    @Inject
    private ProvinceRepository provinceRepository;

    @Inject
    private DistrictSearchRepository districtSearchRepository;

    /**
     * POST  /districts -> Create a new district.
     */
    @RequestMapping(value = "/districts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody District district) throws URISyntaxException {
        log.debug("REST request to save District : {}", district);
        if (district.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new district cannot already have an ID").build();
        }
        district.setActive(true);
        districtRepository.save(district);
        districtSearchRepository.save(district);
        return ResponseEntity.created(new URI("/api/districts/" + district.getId())).build();
    }

    /**
     * PUT  /districts -> Updates an existing district.
     */
    @RequestMapping(value = "/districts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody District district) throws URISyntaxException {
        log.debug("REST request to update District : {}", district);
        if (district.getId() == null) {
            return create(district);
        }
        districtRepository.save(district);
        districtSearchRepository.save(district);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /districts -> get all the districts.
     */
    @RequestMapping(value = "/districts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<District> getAll() {
        log.debug("REST request to get all Districts");
        return districtRepository.findAll();
    }
    
    @RequestMapping(value = "/districts/p/{provinceId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<District> getAllDistrictByProvince(@PathVariable Long provinceId) {
        log.debug("REST request to get all Districts" + provinceId);
        Province province = provinceRepository.findOne(provinceId);
        return districtRepository.findAllByActiveIsTrueAndProvince(province);
    }

    /**
     * GET  /districts/:id -> get the "id" district.
     */
    @RequestMapping(value = "/districts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<District> get(@PathVariable Long id) {
        log.debug("REST request to get District : {}", id);
        return Optional.ofNullable(districtRepository.findOne(id))
            .map(district -> new ResponseEntity<>(
                district,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /districts/:id -> delete the "id" district.
     */
    @RequestMapping(value = "/districts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete District : {}", id);
        District district = districtRepository.findOne(id);
        if (district != null) {
        	district.setActive(false);
        	districtRepository.save(district);
            districtSearchRepository.save(district);
        }
    }

    /**
     * SEARCH  /_search/districts/:query -> search for the district corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/districts/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<District> search(@PathVariable String query) {
        return StreamSupport
            .stream(districtSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
