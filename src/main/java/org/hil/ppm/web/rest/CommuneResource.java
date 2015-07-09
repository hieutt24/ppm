package org.hil.ppm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.hil.ppm.domain.Commune;
import org.hil.ppm.domain.District;
import org.hil.ppm.domain.Province;
import org.hil.ppm.repository.CommuneRepository;
import org.hil.ppm.repository.search.CommuneSearchRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Commune.
 */
@RestController
@RequestMapping("/api")
public class CommuneResource {

    private final Logger log = LoggerFactory.getLogger(CommuneResource.class);

    @Inject
    private CommuneRepository communeRepository;

    @Inject
    private CommuneSearchRepository communeSearchRepository;

    /**
     * POST  /communes -> Create a new commune.
     */
    @RequestMapping(value = "/communes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Commune commune) throws URISyntaxException {
        log.debug("REST request to save Commune : {}", commune);
        if (commune.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new commune cannot already have an ID").build();
        }
        commune.setActive(true);
        commune.setCreatedDate(new DateTime());
        communeRepository.save(commune);
        communeSearchRepository.save(commune);
        return ResponseEntity.created(new URI("/api/communes/" + commune.getId())).build();
    }

    /**
     * PUT  /communes -> Updates an existing commune.
     */
    @RequestMapping(value = "/communes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Commune commune) throws URISyntaxException {
        log.debug("REST request to update Commune : {}", commune);
        if (commune.getId() == null) {
            return create(commune);
        }
        communeRepository.save(commune);
        communeSearchRepository.save(commune);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /communes -> get all the communes.
     */
    @RequestMapping(value = "/communes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Commune> getAll() {
        log.debug("REST request to get all Communes");
        return communeRepository.findAll();
    }
    
    @RequestMapping(value = "/communes/d/{districtId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Commune> getAllCommuneByDistrict(@PathVariable Long districtId) {
        log.debug("REST request to get all Communes");        
        return communeRepository.findAllByActiveIsTrueAndDistrictId(districtId);
    }
    
    @RequestMapping(value = "/communes/p/{provinceId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Commune> getAllCommuneByProvince(@PathVariable Long provinceId) {
        log.debug("REST request to get all Communes");        
        return communeRepository.findAllByActiveIsTrueAndProvinceId(provinceId);
    }

    /**
     * GET  /communes/:id -> get the "id" commune.
     */
    @RequestMapping(value = "/communes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commune> get(@PathVariable Long id) {
        log.debug("REST request to get Commune : {}", id);
        return Optional.ofNullable(communeRepository.findOne(id))
            .map(commune -> new ResponseEntity<>(
                commune,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /communes/:id -> delete the "id" commune.
     */
    @RequestMapping(value = "/communes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Commune : {}", id);
        Commune commune = communeRepository.findOne(id);
        if (commune != null) {
        	commune.setActive(false);
        	communeRepository.save(commune);
        	communeSearchRepository.save(commune);
        }
    }

    /**
     * SEARCH  /_search/communes/:query -> search for the commune corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/communes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Commune> search(@PathVariable String query) {
        return StreamSupport
            .stream(communeSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
