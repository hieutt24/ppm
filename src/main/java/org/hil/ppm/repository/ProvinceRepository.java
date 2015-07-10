package org.hil.ppm.repository;

import org.hil.ppm.domain.Province;
import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Province entity.
 */
public interface ProvinceRepository extends JpaRepository<Province,Long> {
	List<Province> findAllByActiveIsTrueOrderByNameAsc();
}
