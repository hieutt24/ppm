package org.hil.ppm.web.rest;

import org.hil.ppm.Application;
import org.hil.ppm.domain.ExamResult;
import org.hil.ppm.repository.ExamResultRepository;
import org.hil.ppm.repository.search.ExamResultSearchRepository;

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
 * Test class for the ExamResultResource REST controller.
 *
 * @see ExamResultResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ExamResultResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private ExamResultRepository examResultRepository;

    @Inject
    private ExamResultSearchRepository examResultSearchRepository;

    private MockMvc restExamResultMockMvc;

    private ExamResult examResult;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExamResultResource examResultResource = new ExamResultResource();
        ReflectionTestUtils.setField(examResultResource, "examResultRepository", examResultRepository);
        ReflectionTestUtils.setField(examResultResource, "examResultSearchRepository", examResultSearchRepository);
        this.restExamResultMockMvc = MockMvcBuilders.standaloneSetup(examResultResource).build();
    }

    @Before
    public void initTest() {
        examResult = new ExamResult();
        examResult.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createExamResult() throws Exception {
        int databaseSizeBeforeCreate = examResultRepository.findAll().size();

        // Create the ExamResult
        restExamResultMockMvc.perform(post("/api/examResults")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(examResult)))
                .andExpect(status().isCreated());

        // Validate the ExamResult in the database
        List<ExamResult> examResults = examResultRepository.findAll();
        assertThat(examResults).hasSize(databaseSizeBeforeCreate + 1);
        ExamResult testExamResult = examResults.get(examResults.size() - 1);
        assertThat(testExamResult.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllExamResults() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        // Get all the examResults
        restExamResultMockMvc.perform(get("/api/examResults"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(examResult.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getExamResult() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

        // Get the examResult
        restExamResultMockMvc.perform(get("/api/examResults/{id}", examResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(examResult.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExamResult() throws Exception {
        // Get the examResult
        restExamResultMockMvc.perform(get("/api/examResults/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExamResult() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

		int databaseSizeBeforeUpdate = examResultRepository.findAll().size();

        // Update the examResult
        examResult.setName(UPDATED_NAME);
        restExamResultMockMvc.perform(put("/api/examResults")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(examResult)))
                .andExpect(status().isOk());

        // Validate the ExamResult in the database
        List<ExamResult> examResults = examResultRepository.findAll();
        assertThat(examResults).hasSize(databaseSizeBeforeUpdate);
        ExamResult testExamResult = examResults.get(examResults.size() - 1);
        assertThat(testExamResult.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteExamResult() throws Exception {
        // Initialize the database
        examResultRepository.saveAndFlush(examResult);

		int databaseSizeBeforeDelete = examResultRepository.findAll().size();

        // Get the examResult
        restExamResultMockMvc.perform(delete("/api/examResults/{id}", examResult.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ExamResult> examResults = examResultRepository.findAll();
        assertThat(examResults).hasSize(databaseSizeBeforeDelete - 1);
    }
}
