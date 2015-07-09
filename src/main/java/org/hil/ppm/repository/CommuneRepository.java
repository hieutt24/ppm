package org.hil.ppm.repository;

import org.hil.ppm.domain.Commune;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Commune entity.
 */
public interface CommuneRepository extends JpaRepository<Commune,Long> {
	List<Commune> findAllByActiveIsTrueAndDistrictId(Long districtId);
	
	@Query("select c from Commune c, District d where c.districtId=d.id and d.province.id= ?1")
	List<Commune> findAllByActiveIsTrueAndProvinceId(Long provinceId);
}
