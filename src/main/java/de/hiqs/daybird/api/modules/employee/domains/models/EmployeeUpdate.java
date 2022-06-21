package de.hiqs.daybird.api.modules.employee.domains.models;

import de.hiqs.daybird.api.shared.domains.models.AbstractUpdateModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeUpdate extends AbstractUpdateModel {
    String firstName;
    String lastName;
    String addressUuid;
    String email;
    String targetHours;
    String jobTitle;
}
