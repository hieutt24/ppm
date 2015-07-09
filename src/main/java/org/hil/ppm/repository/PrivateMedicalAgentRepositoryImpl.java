package org.hil.ppm.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hil.ppm.domain.PrivateMedicalAgent;
import org.hil.ppm.web.rest.dto.PrivateMedicalAgentDTO;

public class PrivateMedicalAgentRepositoryImpl implements PrivateMedicalAgentRepositoryCustom {

	@PersistenceContext
	 private EntityManager em;
	
	public List<PrivateMedicalAgentDTO> findAllByProvinceDistrictCommune(Long provinceId, Long districtId, Long communeId) {
		String sql = "SELECT new org.hil.ppm.web.rest.dto.PrivateMedicalAgentDTO(a.id,a.name,a.type,a.communeId,a.districtId,a.provinceId,c.name,d.name) "
				+ " FROM PrivateMedicalAgent a, Commune c, District d ";
		Long locationId = communeId;
		if (districtId > 0) {
			locationId = districtId;
		} else if (provinceId > 0) {
			sql += " , Province p ";
			locationId = provinceId;
		}
		sql += " WHERE a.active=true AND a.communeId=c.id ";
		if (communeId > 0) {
			sql += " AND c.id=:locationId ";
		} else if (districtId > 0) {
			sql += " AND c.districtId=d.id AND d.id=:locationId ";
		} else if (provinceId > 0) {
			sql += " AND c.districtId=d.id AND d.province.id=p.id AND p.id=:locationId ";
		}
		Query qry = em.createQuery(sql);
		qry.setParameter("locationId", locationId);
		return qry.getResultList();
	}
}
