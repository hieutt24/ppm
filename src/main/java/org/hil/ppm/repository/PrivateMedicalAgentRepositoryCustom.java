package org.hil.ppm.repository;

import java.util.List;

import org.hil.ppm.domain.PrivateMedicalAgent;
import org.hil.ppm.web.rest.dto.PrivateMedicalAgentDTO;

public interface PrivateMedicalAgentRepositoryCustom {
	List<PrivateMedicalAgentDTO> findAllByProvinceDistrictCommune(Long provinceId, Long districtId, Long communeId);
}
