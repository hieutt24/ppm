package org.hil.ppm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.hil.ppm.domain.Province;
import org.hil.ppm.repository.ProvinceRepository;
import org.hil.ppm.repository.search.ProvinceSearchRepository;
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
 * REST controller for managing Province.
 */
@RestController
@RequestMapping("/api")
public class ProvinceResource {

    private final Logger log = LoggerFactory.getLogger(ProvinceResource.class);

    @Inject
    private ProvinceRepository provinceRepository;

    @Inject
    private ProvinceSearchRepository provinceSearchRepository;

    /**
     * POST  /provinces -> Create a new province.
     */
    @RequestMapping(value = "/provinces",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Province province) throws URISyntaxException {
        log.debug("REST request to save Province : {}", province);
        if (province.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new province cannot already have an ID").build();
        }
        province.setActive(true);
        provinceRepository.save(province);
        provinceSearchRepository.save(province);
        return ResponseEntity.created(new URI("/api/provinces/" + province.getId())).build();
    }

    /**
     * PUT  /provinces -> Updates an existing province.
     */
    @RequestMapping(value = "/provinces",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Province province) throws URISyntaxException {
        log.debug("REST request to update Province : {}", province);
        if (province.getId() == null) {
            return create(province);
        }
        provinceRepository.save(province);
        provinceSearchRepository.save(province);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /provinces -> get all the provinces.
     */
    @RequestMapping(value = "/provinces",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Province> getAll() {
        log.debug("REST request to get all Provinces");
        return provinceRepository.findAllByActiveIsTrueOrderByNameAsc();
    }

    /**
     * GET  /provinces/:id -> get the "id" province.
     */
    @RequestMapping(value = "/provinces/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Province> get(@PathVariable Long id) {
        log.debug("REST request to get Province : {}", id);
        return Optional.ofNullable(provinceRepository.findOne(id))
            .map(province -> new ResponseEntity<>(
                province,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /provinces/:id -> delete the "id" province.
     */
    @RequestMapping(value = "/provinces/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Province : {}", id);
        Province province = provinceRepository.findOne(id);
        if (province != null) {
        	province.setActive(false);
        	provinceRepository.save(province);
            provinceSearchRepository.save(province);
        }
    }

    /**
     * SEARCH  /_search/provinces/:query -> search for the province corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/provinces/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Province> search(@PathVariable String query) {
        return StreamSupport
            .stream(provinceSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
