package de.hiqs.daybird.api.modules.work_package.domains.models;

import de.hiqs.daybird.api.shared.domains.models.AbstractUpdateModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WorkPackageUpdate extends AbstractUpdateModel {
    String name;
    String sign;
    LocalDate startDate;
    LocalDate endDate;
    String projectUuid;
    String employeeUuid;
}
