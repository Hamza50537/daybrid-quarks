package de.hiqs.daybird.api.modules.project.entrypoints.models;

import de.hiqs.daybird.api.shared.entrypoints.models.AbstractUpdateDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectUpdateDto extends AbstractUpdateDto {
    String name;
    String sign;
    LocalDate startDate;
    LocalDate endDate;
    String customerUuid;
}
