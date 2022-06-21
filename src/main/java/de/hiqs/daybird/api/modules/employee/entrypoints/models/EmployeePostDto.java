package de.hiqs.daybird.api.modules.employee.entrypoints.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeePostDto {
    private String firstName;
    private String lastName;
    private String email;
    private String addressUuid;
    private String targetHours;
    private String jobTitle;
}
