package de.hiqs.daybird.api.modules.employee.entrypoints.models;

import de.hiqs.daybird.api.shared.entrypoints.models.AbstractUpdateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeUpdateDto extends AbstractUpdateDto {
    String firstName;
    String lastName;
    String addressUuid;
    String email;
    String targetHours;
    String jobTitle;
}
