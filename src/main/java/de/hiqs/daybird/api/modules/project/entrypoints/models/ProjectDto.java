package de.hiqs.daybird.api.modules.project.entrypoints.models;

import de.hiqs.daybird.api.shared.entrypoints.models.AbstractDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectDto extends AbstractDto {
    private String name;
    private String sign;
    private LocalDate startDate;
    private LocalDate endDate;
    private String customerUuid;
}
