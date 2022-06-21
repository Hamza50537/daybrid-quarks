package de.hiqs.daybird.api.modules.work_package.dataproviders.models;

import de.hiqs.daybird.api.shared.dataproviders.models.AbstractRequestDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WorkPackageRequestDto extends AbstractRequestDto {
    private String name;
    private String sign;
    private LocalDate startDate;
    private LocalDate endDate;
    private String projectUuid;
    private String employeeUuid;
}
