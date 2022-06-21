package de.hiqs.daybird.api.modules.project.domains.models;

import de.hiqs.daybird.api.shared.domains.models.AbstractUpdateModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectUpdate extends AbstractUpdateModel {
    String name;
    String sign;
    LocalDate startDate;
    LocalDate endDate;
    String customerUuid;
}
