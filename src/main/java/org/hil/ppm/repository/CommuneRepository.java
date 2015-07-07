package org.hil.ppm.repository;

import org.hil.ppm.domain.Commune;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Commune entity.
 */
public interface CommuneRepository extends JpaRepository<Commune,Long> {
	List<Commune> findAllByActiveIsTrueAndDistrictId(Long communeId);
}
