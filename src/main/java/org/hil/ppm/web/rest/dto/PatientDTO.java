package org.hil.ppm.web.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.hil.ppm.domain.util.CustomDateTimeDeserializer;
import org.hil.ppm.domain.util.CustomDateTimeSerializer;
import org.hil.ppm.domain.util.CustomLocalDateSerializer;
import org.hil.ppm.domain.util.ISO8601LocalDateDeserializer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Patient entity.
 */
public class PatientDTO implements Serializable {

    private Long id;

    private String fullname;

    private Integer age;

    private Short sex;

    private Long communeId;
    
    private String communeName;

    private Long districtId;
    
    private String districtName;
    
    private Long provinceId;

    private String address;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime examDate;

    private Integer referredByType;

    private Long referredBy;
    
    private String referredByName;

    private Boolean phlegmTest;

    private Long examResult;
    
    private Boolean active;
    
    public PatientDTO() {
    	
    }
    
    public PatientDTO(Long id, String fullname, Integer age, Short sex, String address, DateTime examDate, Long referredBy, Integer referredByType,
    		String referredByName, Boolean phlegmTest, Long examResult,
    		Long communeId, Long districtId, Long provinceId, String communeName, String districtName) {
    	this.id = id;
    	this.fullname = fullname;
    	this.age = age;
    	this.sex = sex;
    	this.address = address;
    	this.examDate = examDate;
    	this.referredBy = referredBy;
    	this.referredByName = referredByName;
    	this.referredByType = referredByType;
    	this.phlegmTest = phlegmTest;
    	this.examResult = examResult;
    	this.communeId = communeId;
    	this.districtId = districtId;
    	this.provinceId = provinceId;
    	this.communeName = communeName;
    	this.districtName = districtName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Short getSex() {
        return sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    public Long getCommuneId() {
        return communeId;
    }

    public void setCommuneId(Long communeId) {
        this.communeId = communeId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    
    public DateTime getExamDate() {
		return examDate;
	}

	public void setExamDate(DateTime examDate) {
		this.examDate = examDate;
	}

	public Integer getReferredByType() {
		return referredByType;
	}

	public void setReferredByType(Integer referredByType) {
		this.referredByType = referredByType;
	}

	public Long getReferredBy() {
		return referredBy;
	}

	public void setReferredBy(Long referredBy) {
		this.referredBy = referredBy;
	}

	public Boolean getPhlegmTest() {
		return phlegmTest;
	}

	public void setPhlegmTest(Boolean phlegmTest) {
		this.phlegmTest = phlegmTest;
	}

	public Long getExamResult() {
		return examResult;
	}

	public void setExamResult(Long examResult) {
		this.examResult = examResult;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCommuneName() {
		return communeName;
	}

	public void setCommuneName(String communeName) {
		this.communeName = communeName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getReferredByName() {
		return referredByName;
	}

	public void setReferredByName(String referredByName) {
		this.referredByName = referredByName;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PatientDTO patientDTO = (PatientDTO) o;

        if ( ! Objects.equals(id, patientDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PatientDTO{" +
                "id=" + id +
                ", fullname='" + fullname + "'" +
                ", age='" + age + "'" +
                ", sex='" + sex + "'" +
                ", communeId='" + communeId + "'" +
                ", districtId='" + districtId + "'" +
                ", provinceId='" + provinceId + "'" +
                ", address='" + address + "'" +
                ", examDate='" + examDate + "'" +
                ", referredByType='" + referredByType + "'" +
                ", referredBy='" + referredBy + "'" +
                ", phlegmTest='" + phlegmTest + "'" +
                ", examResult='" + examResult + "'" +
                ", active='" + active + "'" +
                '}';
    }
}
