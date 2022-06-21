package de.hiqs.daybird.api.modules.project.domains.models;

import de.hiqs.daybird.api.shared.domains.models.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Project extends AbstractModel {
    private String name;
    private String sign;
    private LocalDate startDate;
    private LocalDate endDate;
    private String customerUuid;
}
