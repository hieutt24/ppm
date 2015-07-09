package org.hil.ppm.repository;

import org.hil.ppm.domain.Patient;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Patient entity.
 */
public interface PatientRepository extends PatientRepositoryCustom, JpaRepository<Patient,Long> {

}
