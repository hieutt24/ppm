package org.hil.ppm.web.rest;

import org.hil.ppm.Application;
import org.hil.ppm.domain.Province;
import org.hil.ppm.repository.ProvinceRepository;
import org.hil.ppm.repository.search.ProvinceSearchRepository;

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
 * Test class for the ProvinceResource REST controller.
 *
 * @see ProvinceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProvinceResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Long DEFAULT_ID_REGION = 0L;
    private static final Long UPDATED_ID_REGION = 1L;

    @Inject
    private ProvinceRepository provinceRepository;

    @Inject
    private ProvinceSearchRepository provinceSearchRepository;

    private MockMvc restProvinceMockMvc;

    private Province province;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProvinceResource provinceResource = new ProvinceResource();
        ReflectionTestUtils.setField(provinceResource, "provinceRepository", provinceRepository);
        ReflectionTestUtils.setField(provinceResource, "provinceSearchRepository", provinceSearchRepository);
        this.restProvinceMockMvc = MockMvcBuilders.standaloneSetup(provinceResource).build();
    }

    @Before
    public void initTest() {
        province = new Province();
        province.setName(DEFAULT_NAME);
        province.setActive(DEFAULT_ACTIVE);        
    }

    @Test
    @Transactional
    public void createProvince() throws Exception {
        int databaseSizeBeforeCreate = provinceRepository.findAll().size();

        // Create the Province
        restProvinceMockMvc.perform(post("/api/provinces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(province)))
                .andExpect(status().isCreated());

        // Validate the Province in the database
        List<Province> provinces = provinceRepository.findAll();
        assertThat(provinces).hasSize(databaseSizeBeforeCreate + 1);
        Province testProvince = provinces.get(provinces.size() - 1);
        assertThat(testProvince.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProvince.getActive()).isEqualTo(DEFAULT_ACTIVE);        
    }

    @Test
    @Transactional
    public void getAllProvinces() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get all the provinces
        restProvinceMockMvc.perform(get("/api/provinces"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(province.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].id_region").value(hasItem(DEFAULT_ID_REGION.intValue())));
    }

    @Test
    @Transactional
    public void getProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

        // Get the province
        restProvinceMockMvc.perform(get("/api/provinces/{id}", province.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(province.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.id_region").value(DEFAULT_ID_REGION.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProvince() throws Exception {
        // Get the province
        restProvinceMockMvc.perform(get("/api/provinces/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

		int databaseSizeBeforeUpdate = provinceRepository.findAll().size();

        // Update the province
        province.setName(UPDATED_NAME);
        province.setActive(UPDATED_ACTIVE);        
        restProvinceMockMvc.perform(put("/api/provinces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(province)))
                .andExpect(status().isOk());

        // Validate the Province in the database
        List<Province> provinces = provinceRepository.findAll();
        assertThat(provinces).hasSize(databaseSizeBeforeUpdate);
        Province testProvince = provinces.get(provinces.size() - 1);
        assertThat(testProvince.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProvince.getActive()).isEqualTo(UPDATED_ACTIVE);        
    }

    @Test
    @Transactional
    public void deleteProvince() throws Exception {
        // Initialize the database
        provinceRepository.saveAndFlush(province);

		int databaseSizeBeforeDelete = provinceRepository.findAll().size();

        // Get the province
        restProvinceMockMvc.perform(delete("/api/provinces/{id}", province.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Province> provinces = provinceRepository.findAll();
        assertThat(provinces).hasSize(databaseSizeBeforeDelete - 1);
    }
}
