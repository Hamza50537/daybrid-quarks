package de.hiqs.daybird.api.modules.employee.entrypoints.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.hiqs.daybird.api.shared.entrypoints.models.AbstractDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDto extends AbstractDto {
    private String firstName;
    private String lastName;
    private String email;
    private String addressUuid;
    private String targetHours;
    private String jobTitle;
}
