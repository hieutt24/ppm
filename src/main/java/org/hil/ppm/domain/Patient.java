package org.hil.ppm.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.hil.ppm.domain.util.CustomDateTimeDeserializer;
import org.hil.ppm.domain.util.CustomDateTimeSerializer;
import org.hil.ppm.domain.util.CustomLocalDateSerializer;
import org.hil.ppm.domain.util.ISO8601LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Patient.
 */
@Entity
@Table(name = "PATIENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="patient")
public class Patient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "age")
    private Integer age;

    @Column(name = "sex")
    private Short sex;

    @Column(name = "commune_id")
    private Long communeId;

    @Column(name = "district_id")
    private Long districtId;
    
    @Column(name = "province_id")
    private Long provinceId;

    @Column(name = "address")
    private String address;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "exam_date")
    private DateTime examDate;

    @Column(name = "referred_by")
    private Long referredBy;

    @Column(name = "phlegm_test")
    private Boolean phlegmTest;

    @Column(name = "exam_result")
    private Long examResult;
    
    @Column(name = "active")
    private Boolean active;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "created_date")
    private DateTime createdDate;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "modified_date")
    private DateTime modifiedDate;
    
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

	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	public DateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(DateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Patient patient = (Patient) o;

        if ( ! Objects.equals(id, patient.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", fullname='" + fullname + "'" +
                ", age='" + age + "'" +
                ", sex='" + sex + "'" +
                ", communeId='" + communeId + "'" +
                ", districtId='" + districtId + "'" +
                ", provinceId='" + provinceId + "'" +
                ", address='" + address + "'" +
                ", examDate='" + examDate + "'" +
                ", referredBy='" + referredBy + "'" +
                ", phlegmTest='" + phlegmTest + "'" +
                ", examResult='" + examResult + "'" +
                ", active='" + active + "'" +
                ", createdDate='" + createdDate + "'" +
                ", modifiedDate='" + modifiedDate + "'" +
                '}';
    }
}
