package de.hiqs.daybird.api.modules.time_entry_type.entrypoints.models;

import de.hiqs.daybird.api.shared.entrypoints.models.AbstractDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeEntryTypeDto extends AbstractDto {
    private String typeName;
    private boolean name;
    private boolean startTime;
    private boolean endTime;
    private boolean date;
    private boolean description;
    private boolean workPackageUuid;
    private boolean employeeUuid;
}
