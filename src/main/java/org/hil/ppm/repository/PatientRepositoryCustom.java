package org.hil.ppm.repository;

import java.util.List;

import org.hil.ppm.domain.Patient;
import org.hil.ppm.domain.PrivateMedicalAgent;
import org.hil.ppm.web.rest.dto.PatientDTO;
import org.hil.ppm.web.rest.dto.PrivateMedicalAgentDTO;
import org.hil.ppm.web.rest.dto.SearchPatientDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientRepositoryCustom {
	List<PatientDTO> findAllPatients(SearchPatientDTO searchParams);
	long findTotalPatients(SearchPatientDTO searchParams);
	PatientDTO findPatient(Long id);
}
