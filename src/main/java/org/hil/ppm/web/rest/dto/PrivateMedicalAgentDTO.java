package org.hil.ppm.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the PrivateMedicalAgent entity.
 */
public class PrivateMedicalAgentDTO implements Serializable {

    private Long id;

    private String name;

    private Integer type;

    private Long communeId;
    
    private String communeName;

    private Long districtId;
    
    private String districtName;

    private Long provinceId;
    
    public PrivateMedicalAgentDTO() {
    	
    }
    
    public PrivateMedicalAgentDTO(Long id, String name, Integer type, Long communeId, Long districtId, Long provinceId, String communeName, String districtName) {
    	this.id = id;
    	this.name = name;
    	this.type = type;
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

	public String getCommuneName() {
		return communeName;
	}

	public void setCommuneName(String communeName) {
		this.communeName = communeName;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	
	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PrivateMedicalAgentDTO privateMedicalAgentDTO = (PrivateMedicalAgentDTO) o;

        if ( ! Objects.equals(id, privateMedicalAgentDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PrivateMedicalAgentDTO{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", type='" + type + "'" +
                ", communeId='" + communeId + "'" +
                ", districtId='" + districtId + "'" +
                ", provinceId='" + provinceId + "'" +
                ", communeName='" + communeName + "'" +
                ", districtName='" + districtName + "'" +
                '}';
    }
}
