package de.hiqs.daybird.api.modules.work_package.dataproviders.models;

import de.hiqs.daybird.api.shared.dataproviders.models.AbstractRequestUpdateDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WorkPackageRequestUpdateDto extends AbstractRequestUpdateDto {
    String name;
    String sign;
    LocalDate startDate;
    LocalDate endDate;
    String projectUuid;
    String employeeUuid;
}
