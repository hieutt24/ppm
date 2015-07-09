package org.hil.ppm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.hil.ppm.domain.ExamResult;
import org.hil.ppm.repository.ExamResultRepository;
import org.hil.ppm.repository.search.ExamResultSearchRepository;
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
 * REST controller for managing ExamResult.
 */
@RestController
@RequestMapping("/api")
public class ExamResultResource {

    private final Logger log = LoggerFactory.getLogger(ExamResultResource.class);

    @Inject
    private ExamResultRepository examResultRepository;

    @Inject
    private ExamResultSearchRepository examResultSearchRepository;

    /**
     * POST  /examResults -> Create a new examResult.
     */
    @RequestMapping(value = "/examResults",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody ExamResult examResult) throws URISyntaxException {
        log.debug("REST request to save ExamResult : {}", examResult);
        if (examResult.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new examResult cannot already have an ID").build();
        }
        examResultRepository.save(examResult);
        examResultSearchRepository.save(examResult);
        return ResponseEntity.created(new URI("/api/examResults/" + examResult.getId())).build();
    }

    /**
     * PUT  /examResults -> Updates an existing examResult.
     */
    @RequestMapping(value = "/examResults",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody ExamResult examResult) throws URISyntaxException {
        log.debug("REST request to update ExamResult : {}", examResult);
        if (examResult.getId() == null) {
            return create(examResult);
        }
        examResultRepository.save(examResult);
        examResultSearchRepository.save(examResult);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /examResults -> get all the examResults.
     */
    @RequestMapping(value = "/examResults",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ExamResult> getAll() {
        log.debug("REST request to get all ExamResults");
        return examResultRepository.findAll();
    }

    /**
     * GET  /examResults/:id -> get the "id" examResult.
     */
    @RequestMapping(value = "/examResults/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExamResult> get(@PathVariable Long id) {
        log.debug("REST request to get ExamResult : {}", id);
        return Optional.ofNullable(examResultRepository.findOne(id))
            .map(examResult -> new ResponseEntity<>(
                examResult,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /examResults/:id -> delete the "id" examResult.
     */
    @RequestMapping(value = "/examResults/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ExamResult : {}", id);
        examResultRepository.delete(id);
        examResultSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/examResults/:query -> search for the examResult corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/examResults/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ExamResult> search(@PathVariable String query) {
        return StreamSupport
            .stream(examResultSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
