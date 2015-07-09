package org.hil.ppm.web.rest;

import org.hil.ppm.Application;
import org.hil.ppm.domain.Patient;
import org.hil.ppm.repository.PatientRepository;
import org.hil.ppm.repository.search.PatientSearchRepository;
import org.hil.ppm.web.rest.mapper.PatientMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PatientResource REST controller.
 *
 * @see PatientResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PatientResourceTest {

    private static final String DEFAULT_FULLNAME = "SAMPLE_TEXT";
    private static final String UPDATED_FULLNAME = "UPDATED_TEXT";

    private static final Integer DEFAULT_AGE = 0;
    private static final Integer UPDATED_AGE = 1;

    private static final Short DEFAULT_SEX = 1;
    private static final Short UPDATED_SEX = 2;

    private static final Long DEFAULT_COMMUNE_ID = 0L;
    private static final Long UPDATED_COMMUNE_ID = 1L;

    private static final Long DEFAULT_DISTRICT_ID = 0L;
    private static final Long UPDATED_DISTRICT_ID = 1L;
    private static final String DEFAULT_ADDRESS = "SAMPLE_TEXT";
    private static final String UPDATED_ADDRESS = "UPDATED_TEXT";

    private static final DateTime DEFAULT_EXAM_DATE = new DateTime(0L);
    private static final DateTime UPDATED_EXAM_DATE = new DateTime();

    private static final Integer DEFAULT_REFERRED_FROM = 0;
    private static final Integer UPDATED_REFERRED_FROM = 1;

    private static final Long DEFAULT_REFERRED_BY = 0L;
    private static final Long UPDATED_REFERRED_BY = 1L;

    private static final Boolean DEFAULT_PHLEGM_TEST = false;
    private static final Boolean UPDATED_PHLEGM_TEST = true;

    private static final Long DEFAULT_EXAM_RESULT = 0L;
    private static final Long UPDATED_EXAM_RESULT = 1L;

    @Inject
    private PatientRepository patientRepository;

    @Inject
    private PatientMapper patientMapper;

    @Inject
    private PatientSearchRepository patientSearchRepository;

    private MockMvc restPatientMockMvc;

    private Patient patient;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PatientResource patientResource = new PatientResource();
        ReflectionTestUtils.setField(patientResource, "patientRepository", patientRepository);
        ReflectionTestUtils.setField(patientResource, "patientMapper", patientMapper);
        ReflectionTestUtils.setField(patientResource, "patientSearchRepository", patientSearchRepository);
        this.restPatientMockMvc = MockMvcBuilders.standaloneSetup(patientResource).build();
    }

    @Before
    public void initTest() {
        patient = new Patient();
        patient.setFullname(DEFAULT_FULLNAME);
        patient.setAge(DEFAULT_AGE);
        patient.setSex(DEFAULT_SEX);
        patient.setCommuneId(DEFAULT_COMMUNE_ID);
        patient.setDistrictId(DEFAULT_DISTRICT_ID);
        patient.setAddress(DEFAULT_ADDRESS);
        patient.setExamDate(DEFAULT_EXAM_DATE);        
        patient.setReferredBy(DEFAULT_REFERRED_BY);
        patient.setPhlegmTest(DEFAULT_PHLEGM_TEST);
        patient.setExamResult(DEFAULT_EXAM_RESULT);
    }

    @Test
    @Transactional
    public void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient
        restPatientMockMvc.perform(post("/api/patients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(patient)))
                .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patients = patientRepository.findAll();
        assertThat(patients).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patients.get(patients.size() - 1);
        assertThat(testPatient.getFullname()).isEqualTo(DEFAULT_FULLNAME);
        assertThat(testPatient.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testPatient.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testPatient.getCommuneId()).isEqualTo(DEFAULT_COMMUNE_ID);
        assertThat(testPatient.getDistrictId()).isEqualTo(DEFAULT_DISTRICT_ID);
        assertThat(testPatient.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPatient.getExamDate()).isEqualTo(DEFAULT_EXAM_DATE);
        assertThat(testPatient.getReferredBy()).isEqualTo(DEFAULT_REFERRED_BY);
        assertThat(testPatient.getPhlegmTest()).isEqualTo(DEFAULT_PHLEGM_TEST);
        assertThat(testPatient.getExamResult()).isEqualTo(DEFAULT_EXAM_RESULT);
    }

    @Test
    @Transactional
    public void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patients
        restPatientMockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
                .andExpect(jsonPath("$.[*].fullname").value(hasItem(DEFAULT_FULLNAME.toString())))
                .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
                .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.shortValue())))
                .andExpect(jsonPath("$.[*].communeId").value(hasItem(DEFAULT_COMMUNE_ID.intValue())))
                .andExpect(jsonPath("$.[*].districtId").value(hasItem(DEFAULT_DISTRICT_ID.intValue())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].exam_date").value(hasItem(DEFAULT_EXAM_DATE.toString())))
                .andExpect(jsonPath("$.[*].referred_from").value(hasItem(DEFAULT_REFERRED_FROM)))
                .andExpect(jsonPath("$.[*].referred_by").value(hasItem(DEFAULT_REFERRED_BY.intValue())))
                .andExpect(jsonPath("$.[*].phlegm_test").value(hasItem(DEFAULT_PHLEGM_TEST.booleanValue())))
                .andExpect(jsonPath("$.[*].exam_result").value(hasItem(DEFAULT_EXAM_RESULT.intValue())));
    }

    @Test
    @Transactional
    public void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.fullname").value(DEFAULT_FULLNAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.shortValue()))
            .andExpect(jsonPath("$.communeId").value(DEFAULT_COMMUNE_ID.intValue()))
            .andExpect(jsonPath("$.districtId").value(DEFAULT_DISTRICT_ID.intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.exam_date").value(DEFAULT_EXAM_DATE.toString()))
            .andExpect(jsonPath("$.referred_from").value(DEFAULT_REFERRED_FROM))
            .andExpect(jsonPath("$.referred_by").value(DEFAULT_REFERRED_BY.intValue()))
            .andExpect(jsonPath("$.phlegm_test").value(DEFAULT_PHLEGM_TEST.booleanValue()))
            .andExpect(jsonPath("$.exam_result").value(DEFAULT_EXAM_RESULT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

		int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        patient.setFullname(UPDATED_FULLNAME);
        patient.setAge(UPDATED_AGE);
        patient.setSex(UPDATED_SEX);
        patient.setCommuneId(UPDATED_COMMUNE_ID);
        patient.setDistrictId(UPDATED_DISTRICT_ID);
        patient.setAddress(UPDATED_ADDRESS);
        patient.setExamDate(UPDATED_EXAM_DATE);
        patient.setReferredBy(UPDATED_REFERRED_BY);
        patient.setPhlegmTest(UPDATED_PHLEGM_TEST);
        patient.setExamResult(UPDATED_EXAM_RESULT);
        restPatientMockMvc.perform(put("/api/patients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(patient)))
                .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patients = patientRepository.findAll();
        assertThat(patients).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patients.get(patients.size() - 1);
        assertThat(testPatient.getFullname()).isEqualTo(UPDATED_FULLNAME);
        assertThat(testPatient.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPatient.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testPatient.getCommuneId()).isEqualTo(UPDATED_COMMUNE_ID);
        assertThat(testPatient.getDistrictId()).isEqualTo(UPDATED_DISTRICT_ID);
        assertThat(testPatient.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPatient.getExamDate()).isEqualTo(UPDATED_EXAM_DATE);
        assertThat(testPatient.getReferredBy()).isEqualTo(UPDATED_REFERRED_BY);
        assertThat(testPatient.getPhlegmTest()).isEqualTo(UPDATED_PHLEGM_TEST);
        assertThat(testPatient.getExamResult()).isEqualTo(UPDATED_EXAM_RESULT);
    }

    @Test
    @Transactional
    public void deletePatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

		int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Get the patient
        restPatientMockMvc.perform(delete("/api/patients/{id}", patient.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Patient> patients = patientRepository.findAll();
        assertThat(patients).hasSize(databaseSizeBeforeDelete - 1);
    }
}
