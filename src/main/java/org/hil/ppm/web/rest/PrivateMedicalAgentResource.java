package org.hil.ppm.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.hil.ppm.domain.Commune;
import org.hil.ppm.domain.PrivateMedicalAgent;
import org.hil.ppm.repository.PrivateMedicalAgentRepository;
import org.hil.ppm.repository.search.PrivateMedicalAgentSearchRepository;
import org.hil.ppm.web.rest.dto.PrivateMedicalAgentDTO;
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
 * REST controller for managing PrivateMedicalAgent.
 */
@RestController
@RequestMapping("/api")
public class PrivateMedicalAgentResource {

    private final Logger log = LoggerFactory.getLogger(PrivateMedicalAgentResource.class);

    @Inject
    private PrivateMedicalAgentRepository privateMedicalAgentRepository;

    @Inject
    private PrivateMedicalAgentSearchRepository privateMedicalAgentSearchRepository;

    /**
     * POST  /privateMedicalAgents -> Create a new privateMedicalAgent.
     */
    @RequestMapping(value = "/privateMedicalAgents",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody PrivateMedicalAgentDTO privateMedicalAgentDTO) throws URISyntaxException {
        log.debug("REST request to save PrivateMedicalAgent : {}", privateMedicalAgentDTO);
        if (privateMedicalAgentDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new privateMedicalAgent cannot already have an ID").build();
        }
        PrivateMedicalAgent privateMedicalAgent = new PrivateMedicalAgent();
        privateMedicalAgent.setName(privateMedicalAgentDTO.getName());
        privateMedicalAgent.setType(privateMedicalAgentDTO.getType());
        privateMedicalAgent.setCommuneId(privateMedicalAgentDTO.getCommuneId());
        privateMedicalAgent.setDistrictId(privateMedicalAgentDTO.getDistrictId());
        privateMedicalAgent.setProvinceId(privateMedicalAgentDTO.getProvinceId());
        privateMedicalAgent.setActive(true);
        privateMedicalAgentRepository.save(privateMedicalAgent);
        privateMedicalAgentSearchRepository.save(privateMedicalAgent);
        return ResponseEntity.created(new URI("/api/privateMedicalAgents/" + privateMedicalAgent.getId())).build();
    }

    /**
     * PUT  /privateMedicalAgents -> Updates an existing privateMedicalAgent.
     */
    @RequestMapping(value = "/privateMedicalAgents",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody PrivateMedicalAgentDTO privateMedicalAgentDTO) throws URISyntaxException {
        log.debug("REST request to update PrivateMedicalAgent : {}", privateMedicalAgentDTO);
        if (privateMedicalAgentDTO.getId() == null) {
            return create(privateMedicalAgentDTO);
        }
        PrivateMedicalAgent privateMedicalAgent = privateMedicalAgentRepository.findOne(privateMedicalAgentDTO.getId());
        privateMedicalAgent.setName(privateMedicalAgentDTO.getName());
        privateMedicalAgent.setType(privateMedicalAgentDTO.getType());
        privateMedicalAgent.setCommuneId(privateMedicalAgentDTO.getCommuneId());
        privateMedicalAgent.setDistrictId(privateMedicalAgentDTO.getDistrictId());
        privateMedicalAgent.setProvinceId(privateMedicalAgentDTO.getProvinceId());
        privateMedicalAgent.setActive(true);
        privateMedicalAgentRepository.save(privateMedicalAgent);
        privateMedicalAgentSearchRepository.save(privateMedicalAgent);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /privateMedicalAgents -> get all the privateMedicalAgents.
     */
    @RequestMapping(value = "/privateMedicalAgents",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PrivateMedicalAgent> getAll() {
        log.debug("REST request to get all PrivateMedicalAgents");
        return privateMedicalAgentRepository.findAll();
    }

    /**
     * GET  /privateMedicalAgents/:id -> get the "id" privateMedicalAgent.
     */
    @RequestMapping(value = "/privateMedicalAgents/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrivateMedicalAgent> get(@PathVariable Long id) {
        log.debug("REST request to get PrivateMedicalAgent : {}", id);
        return Optional.ofNullable(privateMedicalAgentRepository.findOne(id))
            .map(privateMedicalAgent -> new ResponseEntity<>(
                privateMedicalAgent,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @RequestMapping(value = "/privateMedicalAgents/p/d/c/{provinceId}/{districtId}/{communeId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PrivateMedicalAgentDTO> getAllPrivateMedicalAgentByLocation(@PathVariable Long provinceId, @PathVariable Long districtId, @PathVariable Long communeId) {
        log.debug("REST request to get all Private Medical Agents");        
        
        return privateMedicalAgentRepository.findAllByProvinceDistrictCommune(provinceId, districtId, communeId);
    }

    /**
     * DELETE  /privateMedicalAgents/:id -> delete the "id" privateMedicalAgent.
     */
    @RequestMapping(value = "/privateMedicalAgents/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete PrivateMedicalAgent : {}", id);
        PrivateMedicalAgent agent = privateMedicalAgentRepository.findOne(id);
        if (agent != null) {
        	agent.setActive(false);
        	privateMedicalAgentRepository.save(agent);
        	privateMedicalAgentSearchRepository.save(agent);
        }
    }

    /**
     * SEARCH  /_search/privateMedicalAgents/:query -> search for the privateMedicalAgent corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/privateMedicalAgents/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PrivateMedicalAgent> search(@PathVariable String query) {
        return StreamSupport
            .stream(privateMedicalAgentSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
