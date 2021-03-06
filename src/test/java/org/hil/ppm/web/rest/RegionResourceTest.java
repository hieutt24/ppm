package org.hil.ppm.web.rest;

import org.hil.ppm.Application;
import org.hil.ppm.domain.Region;
import org.hil.ppm.repository.RegionRepository;
import org.hil.ppm.repository.search.RegionSearchRepository;

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
 * Test class for the RegionResource REST controller.
 *
 * @see RegionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RegionResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Inject
    private RegionRepository regionRepository;

    @Inject
    private RegionSearchRepository regionSearchRepository;

    private MockMvc restRegionMockMvc;

    private Region region;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RegionResource regionResource = new RegionResource();
        ReflectionTestUtils.setField(regionResource, "regionRepository", regionRepository);
        ReflectionTestUtils.setField(regionResource, "regionSearchRepository", regionSearchRepository);
        this.restRegionMockMvc = MockMvcBuilders.standaloneSetup(regionResource).build();
    }

    @Before
    public void initTest() {
        region = new Region();
        region.setName(DEFAULT_NAME);
        region.setActive(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createRegion() throws Exception {
        int databaseSizeBeforeCreate = regionRepository.findAll().size();

        // Create the Region
        restRegionMockMvc.perform(post("/api/regions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(region)))
                .andExpect(status().isCreated());

        // Validate the Region in the database
        List<Region> regions = regionRepository.findAll();
        assertThat(regions).hasSize(databaseSizeBeforeCreate + 1);
        Region testRegion = regions.get(regions.size() - 1);
        assertThat(testRegion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRegion.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllRegions() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get all the regions
        restRegionMockMvc.perform(get("/api/regions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(region.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getRegion() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

        // Get the region
        restRegionMockMvc.perform(get("/api/regions/{id}", region.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(region.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRegion() throws Exception {
        // Get the region
        restRegionMockMvc.perform(get("/api/regions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegion() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

		int databaseSizeBeforeUpdate = regionRepository.findAll().size();

        // Update the region
        region.setName(UPDATED_NAME);
        region.setActive(UPDATED_ACTIVE);
        restRegionMockMvc.perform(put("/api/regions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(region)))
                .andExpect(status().isOk());

        // Validate the Region in the database
        List<Region> regions = regionRepository.findAll();
        assertThat(regions).hasSize(databaseSizeBeforeUpdate);
        Region testRegion = regions.get(regions.size() - 1);
        assertThat(testRegion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRegion.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void deleteRegion() throws Exception {
        // Initialize the database
        regionRepository.saveAndFlush(region);

		int databaseSizeBeforeDelete = regionRepository.findAll().size();

        // Get the region
        restRegionMockMvc.perform(delete("/api/regions/{id}", region.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Region> regions = regionRepository.findAll();
        assertThat(regions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
