package org.hil.ppm.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.hil.ppm.domain.ExamResult;
import org.hil.ppm.domain.Patient;
import org.hil.ppm.repository.ExamResultRepository;
import org.hil.ppm.repository.PatientRepository;
import org.hil.ppm.repository.search.PatientSearchRepository;
import org.hil.ppm.web.rest.util.PaginationUtil;
import org.joda.time.DateTime;
import org.hil.ppm.web.rest.dto.PatientDTO;
import org.hil.ppm.web.rest.dto.PrivateMedicalAgentDTO;
import org.hil.ppm.web.rest.dto.SearchPatientDTO;
import org.hil.ppm.web.rest.mapper.PatientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
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
    private ExamResultRepository examResultRepository;

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
        //Patient patient = patientMapper.patientDTOToPatient(patientDTO);
        Patient patient = patientRepository.findOne(patientDTO.getId());
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
        patient.setActive(true);
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
    public PatientDTO get(@PathVariable Long id) {
        log.debug("REST request to get Patient : {}", id);
        return patientRepository.findPatient(id);
    }
    
    @RequestMapping(value = "/patients/search",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PatientDTO>> getAllPatients(@RequestBody SearchPatientDTO searchParams) throws URISyntaxException {
        log.debug("REST request to get all Patients");        

        Pageable pable = PaginationUtil.generatePageRequest(searchParams.getOffset(), searchParams.getLimit());
        List<PatientDTO> patients = patientRepository.findAllPatients(searchParams);//patientRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        long total = patientRepository.findTotalPatients(searchParams);
        Page<PatientDTO> page = new PageImpl<PatientDTO>(patients,pable,total);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/patients", searchParams.getOffset(), searchParams.getLimit(), total);
        return new ResponseEntity<>(patients.stream()
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);        
    }
    
    @RequestMapping(value = "/patients/excel",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> exportExcelPatients(@RequestBody SearchPatientDTO searchParams) throws URISyntaxException {
        log.debug("REST request to get all Patients");        
        
    	List<PatientDTO> patients = patientRepository.findAllPatients(searchParams);    		
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");	   		
			
		List<ExamResult> results = examResultRepository.findAll();
		long currentTime = System.currentTimeMillis();
		String reportDir = "d:/HProjects/path.ppm/program/ppm/src/main/webapp/assets/";
		String filePath = reportDir + "ppm_.xls";
		String path = "";
		POIFSFileSystem fs;
		try {
			//filePath += ".xls";
			fs = new POIFSFileSystem(new FileInputStream(reportDir + "ppm.xls"));
			HSSFWorkbook wb = new  HSSFWorkbook(fs, true);
			
			HSSFSheet s = wb.getSheetAt(0);
			
			HSSFRow r = null;
			HSSFCell c = null;
			
			
			HSSFCellStyle cs = wb.createCellStyle();
			cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
			
			HSSFCellStyle cs1 = wb.createCellStyle();
			cs1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs1.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs1.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cs1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			
			HSSFCellStyle cs2 = wb.createCellStyle();
			cs2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs2.setBorderRight(HSSFCellStyle.BORDER_THIN);					
			CreationHelper createHelper = wb.getCreationHelper();
			cs2.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
			
			int rownum=3;
			for (rownum=3;rownum<patients.size() + 3;rownum++) {			
				r = s.createRow(rownum);
								
				c = r.createCell(0);
				c.setCellStyle(cs1);
				c.setCellValue(rownum-2);
				
				c = r.createCell(1);
				c.setCellStyle(cs2);
				c.setCellValue(format.format(patients.get(rownum-3).getExamDate().toDate()));
								
				c = r.createCell(2);
				c.setCellStyle(cs);
				c.setCellValue(patients.get(rownum-3).getFullname());
				
				c = r.createCell(3);
				c.setCellStyle(cs1);
				c.setCellValue(patients.get(rownum-3).getAge());
				
				c = r.createCell(4);
				c.setCellStyle(cs1);
				c.setCellValue(patients.get(rownum-3).getSex() == 1 ? "Nữ" : "Nam");
				
				c = r.createCell(5);
				c.setCellStyle(cs);
				c.setCellValue(patients.get(rownum-3).getDistrictName());
				
				c = r.createCell(6);
				c.setCellStyle(cs);
				c.setCellValue(patients.get(rownum-3).getCommuneName());
				
				c = r.createCell(7);
				c.setCellStyle(cs);
				c.setCellValue(patients.get(rownum-3).getReferredByName());
				
				c = r.createCell(8);
				c.setCellStyle(cs1);
				c.setCellValue(patients.get(rownum-3).getReferredByType() == 1 ? "PK Tư" : "BV Tư");
				
				c = r.createCell(9);
				c.setCellStyle(cs);
				c.setCellValue(patients.get(rownum-3).getPhlegmTest() == true ? "Có" : "Không");
				
				c = r.createCell(10);
				c.setCellStyle(cs);
				c.setCellValue(results.get(patients.get(rownum-3).getExamResult().intValue()-1).getName());
				
				c = r.createCell(11);
				c.setCellStyle(cs);
				c.setCellValue("");
			}
			
					
			FileOutputStream fileOut = new FileOutputStream(filePath); 
			wb.write(fileOut);
			fileOut.close();
			path = "/assets/ppm_.xls";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ResponseEntity.created(new URI(path)).build();
        	
        
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
