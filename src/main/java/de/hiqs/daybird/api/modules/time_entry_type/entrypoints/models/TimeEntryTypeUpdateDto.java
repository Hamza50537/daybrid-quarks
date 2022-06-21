package de.hiqs.daybird.api.modules.time_entry_type.entrypoints.models;

import de.hiqs.daybird.api.shared.entrypoints.models.AbstractUpdateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeEntryTypeUpdateDto extends AbstractUpdateDto {
    String typeName;
    boolean name;
    boolean startTime;
    boolean endTime;
    boolean date;
    boolean description;
    boolean workPackageUuid;
    boolean employeeUuid;
}
