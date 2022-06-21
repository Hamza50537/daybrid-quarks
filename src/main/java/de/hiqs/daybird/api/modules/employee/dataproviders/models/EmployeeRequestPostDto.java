package de.hiqs.daybird.api.modules.employee.dataproviders.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeRequestPostDto {
    private String firstName;
    private String lastName;
    private String addressUuid;
    private String email;
    private String targetHours;
    private String jobTitle;
}
