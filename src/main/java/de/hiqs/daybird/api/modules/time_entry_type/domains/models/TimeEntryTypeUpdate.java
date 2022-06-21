package de.hiqs.daybird.api.modules.time_entry_type.domains.models;

import de.hiqs.daybird.api.shared.domains.models.AbstractUpdateModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeEntryTypeUpdate extends AbstractUpdateModel {
    String typeName;
    boolean name;
    boolean startTime;
    boolean endTime;
    boolean date;
    boolean description;
    boolean workPackageUuid;
    boolean employeeUuid;
}
