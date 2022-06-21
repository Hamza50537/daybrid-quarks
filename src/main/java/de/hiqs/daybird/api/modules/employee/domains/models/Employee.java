package de.hiqs.daybird.api.modules.employee.domains.models;


import de.hiqs.daybird.api.shared.domains.models.AbstractModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee extends AbstractModel {
    private String firstName;
    private String lastName;
    private String addressUuid;
    private String email;
    private String targetHours;
    private String jobTitle;
}
