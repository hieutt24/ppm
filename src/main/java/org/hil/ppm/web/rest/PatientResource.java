package org.hil.ppm.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.hil.ppm.domain.Patient;
import org.hil.ppm.repository.PatientRepository;
import org.hil.ppm.repository.search.PatientSearchRepository;
import org.hil.ppm.web.rest.util.PaginationUtil;
import org.joda.time.DateTime;
import org.hil.ppm.web.rest.dto.PatientDTO;
import org.hil.ppm.web.rest.dto.PrivateMedicalAgentDTO;
import org.hil.ppm.web.rest.mapper.PatientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Patient.
 */
@RestController
@RequestMapping("/api")
public class PatientResource {

    private final Logger log = LoggerFactory.getLogger(PatientResource.class);

    @Inject
    private PatientRepository patientRepository;

    @Inject
    private PatientMapper patientMapper;

    @Inject
    private PatientSearchRepository patientSearchRepository;

    /**
     * POST  /patients -> Create a new patient.
     */
    @RequestMapping(value = "/patients",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody PatientDTO patientDTO) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", patientDTO);
        if (patientDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new patient cannot already have an ID").build();
        }
        //Patient patient = patientMapper.patientDTOToPatient(patientDTO);
        Patient patient = new Patient();
        patient.setFullname(patientDTO.getFullname());
        patient.setAge(patientDTO.getAge());
        patient.setSex(patientDTO.getSex());
        patient.setAddress(patientDTO.getAddress());
        patient.setCommuneId(patientDTO.getCommuneId());
        patient.setDistrictId(patientDTO.getDistrictId());
        patient.setProvinceId(patientDTO.getProvinceId());
        patient.setExamDate(patientDTO.getExamDate());
        patient.setPhlegmTest(patientDTO.getPhlegmTest());
        patient.setReferredBy(patientDTO.getReferredBy());
        patient.setExamResult(patientDTO.getExamResult());
        patient.setCreatedDate(new DateTime());
        patient.setActive(true);
        patientRepository.save(patient);
        patientSearchRepository.save(patient);
        return ResponseEntity.created(new URI("/api/patients/" + patientDTO.getId())).build();
    }

    /**
     * PUT  /patients -> Updates an existing patient.
     */
    @RequestMapping(value = "/patients",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody PatientDTO patientDTO) throws URISyntaxException {
        log.debug("REST request to update Patient : {}", patientDTO);
        if (patientDTO.getId() == null) {
            return create(patientDTO);
        }
        Patient patient = patientMapper.patientDTOToPatient(patientDTO);
        patient.setModifiedDate(new DateTime());
        patientRepository.save(patient);
        patientSearchRepository.save(patient);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /patients -> get all the patients.
     */
    @RequestMapping(value = "/patients",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PatientDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Patient> page = patientRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/patients", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(patientMapper::patientToPatientDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /patients/:id -> get the "id" patient.
     */
    @RequestMapping(value = "/patients/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PatientDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Patient : {}", id);
        return Optional.ofNullable(patientRepository.findOne(id))
            .map(patientMapper::patientToPatientDTO)
            .map(patientDTO -> new ResponseEntity<>(
                patientDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @RequestMapping(value = "/patients/p/d/c/{provinceId}/{districtId}/{communeId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PatientDTO> getAllPatients(@PathVariable Long provinceId, @PathVariable Long districtId, @PathVariable Long communeId) {
        log.debug("REST request to get all Patients");        
        
        return patientRepository.findAllPatients(provinceId, districtId, communeId);
    }

    /**
     * DELETE  /patients/:id -> delete the "id" patient.
     */
    @RequestMapping(value = "/patients/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Patient : {}", id);
        patientRepository.delete(id);
        patientSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/patients/:query -> search for the patient corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/patients/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Patient> search(@PathVariable String query) {
        return StreamSupport
            .stream(patientSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
