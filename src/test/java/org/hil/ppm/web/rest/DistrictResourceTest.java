package org.hil.ppm.web.rest;

import org.hil.ppm.Application;
import org.hil.ppm.domain.District;
import org.hil.ppm.repository.DistrictRepository;
import org.hil.ppm.repository.search.DistrictSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DistrictResource REST controller.
 *
 * @see DistrictResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DistrictResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Inject
    private DistrictRepository districtRepository;

    @Inject
    private DistrictSearchRepository districtSearchRepository;

    private MockMvc restDistrictMockMvc;

    private District district;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DistrictResource districtResource = new DistrictResource();
        ReflectionTestUtils.setField(districtResource, "districtRepository", districtRepository);
        ReflectionTestUtils.setField(districtResource, "districtSearchRepository", districtSearchRepository);
        this.restDistrictMockMvc = MockMvcBuilders.standaloneSetup(districtResource).build();
    }

    @Before
    public void initTest() {
        district = new District();
        district.setName(DEFAULT_NAME);
        district.setActive(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createDistrict() throws Exception {
        int databaseSizeBeforeCreate = districtRepository.findAll().size();

        // Create the District
        restDistrictMockMvc.perform(post("/api/districts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(district)))
                .andExpect(status().isCreated());

        // Validate the District in the database
        List<District> districts = districtRepository.findAll();
        assertThat(districts).hasSize(databaseSizeBeforeCreate + 1);
        District testDistrict = districts.get(districts.size() - 1);
        assertThat(testDistrict.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDistrict.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllDistricts() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get all the districts
        restDistrictMockMvc.perform(get("/api/districts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(district.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

        // Get the district
        restDistrictMockMvc.perform(get("/api/districts/{id}", district.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(district.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDistrict() throws Exception {
        // Get the district
        restDistrictMockMvc.perform(get("/api/districts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

		int databaseSizeBeforeUpdate = districtRepository.findAll().size();

        // Update the district
        district.setName(UPDATED_NAME);
        district.setActive(UPDATED_ACTIVE);
        restDistrictMockMvc.perform(put("/api/districts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(district)))
                .andExpect(status().isOk());

        // Validate the District in the database
        List<District> districts = districtRepository.findAll();
        assertThat(districts).hasSize(databaseSizeBeforeUpdate);
        District testDistrict = districts.get(districts.size() - 1);
        assertThat(testDistrict.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDistrict.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void deleteDistrict() throws Exception {
        // Initialize the database
        districtRepository.saveAndFlush(district);

		int databaseSizeBeforeDelete = districtRepository.findAll().size();

        // Get the district
        restDistrictMockMvc.perform(delete("/api/districts/{id}", district.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<District> districts = districtRepository.findAll();
        assertThat(districts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
