package de.hiqs.daybird.api.modules.employee.dataproviders.models;

import de.hiqs.daybird.api.shared.dataproviders.models.AbstractRequestUpdateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequestUpdateDto extends AbstractRequestUpdateDto {
    String firstName;
    String lastName;
    String addressUuid;
    String email;
    String targetHours;
    String jobTitle;
}
