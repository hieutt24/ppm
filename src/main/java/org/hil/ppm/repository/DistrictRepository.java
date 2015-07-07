package org.hil.ppm.repository;

import org.hil.ppm.domain.District;
import org.hil.ppm.domain.Province;
import org.hil.ppm.domain.Region;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the District entity.
 */
public interface DistrictRepository extends JpaRepository<District,Long> {
	List<District> findAllByActiveIsTrueAndProvince(Province province);
}
