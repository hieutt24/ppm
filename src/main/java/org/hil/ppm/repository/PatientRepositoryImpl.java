package org.hil.ppm.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hil.ppm.web.rest.dto.PatientDTO;

public class PatientRepositoryImpl implements PatientRepositoryCustom {

	@PersistenceContext
	 private EntityManager em;

	public List<PatientDTO> findAllPatients(Long provinceId, Long districtId, Long communeId) {
		String sql = "SELECT new org.hil.ppm.web.rest.dto.PatientDTO(p.id,p.fullname,p.age,p.sex,p.address,p.examDate,p.referredBy, "
				+ " a.type,a.name,p.phlegmTest,p.examResult,p.communeId,a.districtId,a.provinceId,c.name,d.name) "
				+ " FROM Patient p, PrivateMedicalAgent a, Commune c, District d ";
		Long locationId = communeId;
		if (districtId > 0) {
			locationId = districtId;
		} else if (provinceId > 0) {
			sql += " , Province v ";
			locationId = provinceId;
		}
		sql += " WHERE p.active=true AND p.communeId=c.id AND p.referredBy=a.id ";
		if (communeId > 0) {
			sql += " AND c.id=:locationId ";
		} else if (districtId > 0) {
			sql += " AND c.districtId=d.id AND d.id=:locationId ";
		} else if (provinceId > 0) {
			sql += " AND c.districtId=d.id AND d.province.id=v.id AND v.id=:locationId ";
		}
		Query qry = em.createQuery(sql);
		qry.setParameter("locationId", locationId);
		return qry.getResultList();
	}
}
