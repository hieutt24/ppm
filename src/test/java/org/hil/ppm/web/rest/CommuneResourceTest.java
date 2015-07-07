package org.hil.ppm.web.rest;

import org.hil.ppm.Application;
import org.hil.ppm.domain.Commune;
import org.hil.ppm.repository.CommuneRepository;
import org.hil.ppm.repository.search.CommuneSearchRepository;

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
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CommuneResource REST controller.
 *
 * @see CommuneResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CommuneResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    private static final Long DEFAULT_ID_DISTRICT = 0L;
    private static final Long UPDATED_ID_DISTRICT = 1L;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final DateTime DEFAULT_CREATED_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATED_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATED_DATE_STR = dateTimeFormatter.print(DEFAULT_CREATED_DATE);

    @Inject
    private CommuneRepository communeRepository;

    @Inject
    private CommuneSearchRepository communeSearchRepository;

    private MockMvc restCommuneMockMvc;

    private Commune commune;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommuneResource communeResource = new CommuneResource();
        ReflectionTestUtils.setField(communeResource, "communeRepository", communeRepository);
        ReflectionTestUtils.setField(communeResource, "communeSearchRepository", communeSearchRepository);
        this.restCommuneMockMvc = MockMvcBuilders.standaloneSetup(communeResource).build();
    }

    @Before
    public void initTest() {
        commune = new Commune();
        commune.setName(DEFAULT_NAME);
        commune.setDistrictId(DEFAULT_ID_DISTRICT);
        commune.setActive(DEFAULT_ACTIVE);
        commune.setCreatedDate(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createCommune() throws Exception {
        int databaseSizeBeforeCreate = communeRepository.findAll().size();

        // Create the Commune
        restCommuneMockMvc.perform(post("/api/communes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commune)))
                .andExpect(status().isCreated());

        // Validate the Commune in the database
        List<Commune> communes = communeRepository.findAll();
        assertThat(communes).hasSize(databaseSizeBeforeCreate + 1);
        Commune testCommune = communes.get(communes.size() - 1);
        assertThat(testCommune.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommune.getDistrictId()).isEqualTo(DEFAULT_ID_DISTRICT);
        assertThat(testCommune.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testCommune.getCreatedDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommunes() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get all the communes
        restCommuneMockMvc.perform(get("/api/communes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(commune.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].id_district").value(hasItem(DEFAULT_ID_DISTRICT.intValue())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].created_date").value(hasItem(DEFAULT_CREATED_DATE_STR)));
    }

    @Test
    @Transactional
    public void getCommune() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

        // Get the commune
        restCommuneMockMvc.perform(get("/api/communes/{id}", commune.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(commune.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.id_district").value(DEFAULT_ID_DISTRICT.intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.created_date").value(DEFAULT_CREATED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingCommune() throws Exception {
        // Get the commune
        restCommuneMockMvc.perform(get("/api/communes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommune() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

		int databaseSizeBeforeUpdate = communeRepository.findAll().size();

        // Update the commune
        commune.setName(UPDATED_NAME);
        commune.setDistrictId(UPDATED_ID_DISTRICT);
        commune.setActive(UPDATED_ACTIVE);
        commune.setCreatedDate(UPDATED_CREATED_DATE);
        restCommuneMockMvc.perform(put("/api/communes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commune)))
                .andExpect(status().isOk());

        // Validate the Commune in the database
        List<Commune> communes = communeRepository.findAll();
        assertThat(communes).hasSize(databaseSizeBeforeUpdate);
        Commune testCommune = communes.get(communes.size() - 1);
        assertThat(testCommune.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommune.getDistrictId()).isEqualTo(UPDATED_ID_DISTRICT);
        assertThat(testCommune.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testCommune.getCreatedDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void deleteCommune() throws Exception {
        // Initialize the database
        communeRepository.saveAndFlush(commune);

		int databaseSizeBeforeDelete = communeRepository.findAll().size();

        // Get the commune
        restCommuneMockMvc.perform(delete("/api/communes/{id}", commune.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Commune> communes = communeRepository.findAll();
        assertThat(communes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
