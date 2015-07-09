package org.hil.ppm.web.rest;

import org.hil.ppm.Application;
import org.hil.ppm.domain.PrivateMedicalAgent;
import org.hil.ppm.repository.PrivateMedicalAgentRepository;
import org.hil.ppm.repository.search.PrivateMedicalAgentSearchRepository;

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
 * Test class for the PrivateMedicalAgentResource REST controller.
 *
 * @see PrivateMedicalAgentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PrivateMedicalAgentResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    private static final Integer DEFAULT_TYPE = 0;
    private static final Integer UPDATED_TYPE = 1;

    private static final Long DEFAULT_COMMUNE_ID = 0L;
    private static final Long UPDATED_COMMUNE_ID = 1L;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Inject
    private PrivateMedicalAgentRepository privateMedicalAgentRepository;

    @Inject
    private PrivateMedicalAgentSearchRepository privateMedicalAgentSearchRepository;

    private MockMvc restPrivateMedicalAgentMockMvc;

    private PrivateMedicalAgent privateMedicalAgent;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrivateMedicalAgentResource privateMedicalAgentResource = new PrivateMedicalAgentResource();
        ReflectionTestUtils.setField(privateMedicalAgentResource, "privateMedicalAgentRepository", privateMedicalAgentRepository);
        ReflectionTestUtils.setField(privateMedicalAgentResource, "privateMedicalAgentSearchRepository", privateMedicalAgentSearchRepository);
        this.restPrivateMedicalAgentMockMvc = MockMvcBuilders.standaloneSetup(privateMedicalAgentResource).build();
    }

    @Before
    public void initTest() {
        privateMedicalAgent = new PrivateMedicalAgent();
        privateMedicalAgent.setName(DEFAULT_NAME);
        privateMedicalAgent.setType(DEFAULT_TYPE);
        privateMedicalAgent.setCommuneId(DEFAULT_COMMUNE_ID);
        privateMedicalAgent.setActive(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createPrivateMedicalAgent() throws Exception {
        int databaseSizeBeforeCreate = privateMedicalAgentRepository.findAll().size();

        // Create the PrivateMedicalAgent
        restPrivateMedicalAgentMockMvc.perform(post("/api/privateMedicalAgents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(privateMedicalAgent)))
                .andExpect(status().isCreated());

        // Validate the PrivateMedicalAgent in the database
        List<PrivateMedicalAgent> privateMedicalAgents = privateMedicalAgentRepository.findAll();
        assertThat(privateMedicalAgents).hasSize(databaseSizeBeforeCreate + 1);
        PrivateMedicalAgent testPrivateMedicalAgent = privateMedicalAgents.get(privateMedicalAgents.size() - 1);
        assertThat(testPrivateMedicalAgent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPrivateMedicalAgent.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPrivateMedicalAgent.getCommuneId()).isEqualTo(DEFAULT_COMMUNE_ID);
        assertThat(testPrivateMedicalAgent.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPrivateMedicalAgents() throws Exception {
        // Initialize the database
        privateMedicalAgentRepository.saveAndFlush(privateMedicalAgent);

        // Get all the privateMedicalAgents
        restPrivateMedicalAgentMockMvc.perform(get("/api/privateMedicalAgents"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(privateMedicalAgent.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
                .andExpect(jsonPath("$.[*].communeId").value(hasItem(DEFAULT_COMMUNE_ID.intValue())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getPrivateMedicalAgent() throws Exception {
        // Initialize the database
        privateMedicalAgentRepository.saveAndFlush(privateMedicalAgent);

        // Get the privateMedicalAgent
        restPrivateMedicalAgentMockMvc.perform(get("/api/privateMedicalAgents/{id}", privateMedicalAgent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(privateMedicalAgent.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.communeId").value(DEFAULT_COMMUNE_ID.intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPrivateMedicalAgent() throws Exception {
        // Get the privateMedicalAgent
        restPrivateMedicalAgentMockMvc.perform(get("/api/privateMedicalAgents/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrivateMedicalAgent() throws Exception {
        // Initialize the database
        privateMedicalAgentRepository.saveAndFlush(privateMedicalAgent);

		int databaseSizeBeforeUpdate = privateMedicalAgentRepository.findAll().size();

        // Update the privateMedicalAgent
        privateMedicalAgent.setName(UPDATED_NAME);
        privateMedicalAgent.setType(UPDATED_TYPE);
        privateMedicalAgent.setCommuneId(UPDATED_COMMUNE_ID);
        privateMedicalAgent.setActive(UPDATED_ACTIVE);
        restPrivateMedicalAgentMockMvc.perform(put("/api/privateMedicalAgents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(privateMedicalAgent)))
                .andExpect(status().isOk());

        // Validate the PrivateMedicalAgent in the database
        List<PrivateMedicalAgent> privateMedicalAgents = privateMedicalAgentRepository.findAll();
        assertThat(privateMedicalAgents).hasSize(databaseSizeBeforeUpdate);
        PrivateMedicalAgent testPrivateMedicalAgent = privateMedicalAgents.get(privateMedicalAgents.size() - 1);
        assertThat(testPrivateMedicalAgent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPrivateMedicalAgent.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPrivateMedicalAgent.getCommuneId()).isEqualTo(UPDATED_COMMUNE_ID);
        assertThat(testPrivateMedicalAgent.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void deletePrivateMedicalAgent() throws Exception {
        // Initialize the database
        privateMedicalAgentRepository.saveAndFlush(privateMedicalAgent);

		int databaseSizeBeforeDelete = privateMedicalAgentRepository.findAll().size();

        // Get the privateMedicalAgent
        restPrivateMedicalAgentMockMvc.perform(delete("/api/privateMedicalAgents/{id}", privateMedicalAgent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PrivateMedicalAgent> privateMedicalAgents = privateMedicalAgentRepository.findAll();
        assertThat(privateMedicalAgents).hasSize(databaseSizeBeforeDelete - 1);
    }
}
