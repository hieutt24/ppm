package org.hil.ppm.repository;

import java.util.List;
import org.hil.ppm.domain.Region;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Region entity.
 */
public interface RegionRepository extends JpaRepository<Region,Long> {
	List<Region> findAllByActiveIsTrue();
}
