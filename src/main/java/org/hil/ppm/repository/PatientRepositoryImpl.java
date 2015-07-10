package org.hil.ppm.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hil.ppm.web.rest.dto.PatientDTO;
import org.hil.ppm.web.rest.dto.SearchPatientDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PatientRepositoryImpl implements PatientRepositoryCustom {

	@PersistenceContext
	 private EntityManager em;

	public long findTotalPatients(SearchPatientDTO searchParams) {
		String sql = "SELECT count(p.id) FROM Patient p, PrivateMedicalAgent a, Commune c, District d ";
		Long locationId = searchParams.getCommuneId();
		if (searchParams.getDistrictId() > 0) {
			locationId = searchParams.getDistrictId();
		} else if (searchParams.getProvinceId() > 0) {
			sql += " , Province v ";
			locationId = searchParams.getProvinceId();
		}
		sql += " WHERE p.active=true AND p.communeId=c.id AND p.referredBy=a.id ";
		if (searchParams.getCommuneId() > 0) {
			sql += " AND c.id=:locationId ";
		} else if (searchParams.getDistrictId() > 0) {
			sql += " AND c.districtId=d.id AND d.id=:locationId ";
		} else if (searchParams.getProvinceId() > 0) {
			sql += " AND c.districtId=d.id AND d.province.id=v.id AND v.id=:locationId ";
		} else {
			sql += " AND c.districtId=d.id ";
		}
		if (searchParams.getExamResultId() > 0) {
			sql += " AND p.examResult=:examResultId ";
		}
		Query qry = em.createQuery(sql);
		if (locationId > 0)
			qry.setParameter("locationId", locationId);
		if (searchParams.getExamResultId() > 0) {
			qry.setParameter("examResultId", searchParams.getExamResultId());
		}
		long total = (long)qry.getSingleResult();
		return total;
	}
	
	public List<PatientDTO> findAllPatients(SearchPatientDTO searchParams) {
		Long locationId = searchParams.getCommuneId();
		String sql = "SELECT new org.hil.ppm.web.rest.dto.PatientDTO(p.id,p.fullname,p.age,p.sex,p.address,p.examDate,p.referredBy, "
				+ " a.type,a.name,p.phlegmTest,p.examResult,p.communeId,p.districtId,p.provinceId,c.name,d.name) "
				+ " FROM Patient p, PrivateMedicalAgent a, Commune c, District d ";		
		if (searchParams.getDistrictId() > 0) {
			locationId = searchParams.getDistrictId();
		} else if (searchParams.getProvinceId() > 0) {
			sql += " , Province v ";
			locationId = searchParams.getProvinceId();
		}
		sql += " WHERE p.active=true AND p.communeId=c.id AND p.referredBy=a.id ";
		if (searchParams.getCommuneId() > 0) {
			sql += " AND c.id=:locationId ";
		} else if (searchParams.getDistrictId() > 0) {
			sql += " AND c.districtId=d.id AND d.id=:locationId ";
		} else if (searchParams.getProvinceId() > 0) {
			sql += " AND c.districtId=d.id AND d.province.id=v.id AND v.id=:locationId ";
		} else {
			sql += " AND c.districtId=d.id ";
		}
		if (searchParams.getExamResultId() > 0) {
			sql += " AND p.examResult=:examResultId ";
		}
		Query qry = em.createQuery(sql);
		if (locationId > 0)
			qry.setParameter("locationId", locationId);
		if (searchParams.getExamResultId() > 0) {
			qry.setParameter("examResultId", searchParams.getExamResultId());
		}
		if (searchParams.getExportExcel() == 0) {
			qry.setFirstResult((searchParams.getOffset() - 1) * searchParams.getLimit());
			qry.setMaxResults(searchParams.getLimit());
		}
		return qry.getResultList();
	}
	
	public PatientDTO findPatient(Long id) {
		String sql = "SELECT new org.hil.ppm.web.rest.dto.PatientDTO(p.id,p.fullname,p.age,p.sex,p.address,p.examDate,p.referredBy, "
				+ " a.type,a.name,p.phlegmTest,p.examResult,p.communeId,a.districtId,a.provinceId,c.name,d.name) "
				+ " FROM Patient p, PrivateMedicalAgent a, Commune c, District d, Province v "
				+ " WHERE p.active=true AND p.communeId=c.id AND p.referredBy=a.id "
				+ " AND c.districtId=d.id AND d.province.id=v.id AND p.id=:id ";
		
		Query qry = em.createQuery(sql);
		qry.setParameter("id", id);
		return (PatientDTO)qry.getResultList().get(0);
	}
}
