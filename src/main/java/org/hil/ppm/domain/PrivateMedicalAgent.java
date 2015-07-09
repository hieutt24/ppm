package org.hil.ppm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A PrivateMedicalAgent.
 */
@Entity
@Table(name = "PRIVATEMEDICALAGENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="privatemedicalagent")
public class PrivateMedicalAgent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private Integer type;

    @Column(name = "commune_id")
    private Long communeId;
    
    @Column(name = "district_id")
    private Long districtId;
    
    @Column(name = "province_id")
    private Long provinceId;

    @Column(name = "active")
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

	public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PrivateMedicalAgent privateMedicalAgent = (PrivateMedicalAgent) o;

        if ( ! Objects.equals(id, privateMedicalAgent.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PrivateMedicalAgent{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", type='" + type + "'" +
                ", communeId='" + communeId + "'" +
                ", districtId='" + districtId + "'" +
                ", provinceId='" + provinceId + "'" +
                ", active='" + active + "'" +
                '}';
    }
}
