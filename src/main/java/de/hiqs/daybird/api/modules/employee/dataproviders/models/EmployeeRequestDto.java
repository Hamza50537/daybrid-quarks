package de.hiqs.daybird.api.modules.employee.dataproviders.models;

import de.hiqs.daybird.api.shared.dataproviders.models.AbstractRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequestDto extends AbstractRequestDto {
    private String firstName;
    private String lastName;
    private String addressUuid;
    private String email;
    private String targetHours;
    private String jobTitle;
}
