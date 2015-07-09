package org.hil.ppm.repository;

import org.hil.ppm.domain.PrivateMedicalAgent;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the PrivateMedicalAgent entity.
 */
public interface PrivateMedicalAgentRepository extends PrivateMedicalAgentRepositoryCustom, JpaRepository<PrivateMedicalAgent,Long> {	
	
}
