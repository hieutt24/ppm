package org.hil.ppm.web.rest.mapper;

import org.hil.ppm.domain.*;
import org.hil.ppm.web.rest.dto.PatientDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Patient and its DTO PatientDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PatientMapper {

    PatientDTO patientToPatientDTO(Patient patient);

    Patient patientDTOToPatient(PatientDTO patientDTO);
}
