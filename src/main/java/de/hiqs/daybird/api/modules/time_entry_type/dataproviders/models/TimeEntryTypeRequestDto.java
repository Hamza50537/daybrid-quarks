package de.hiqs.daybird.api.modules.time_entry_type.dataproviders.models;

import de.hiqs.daybird.api.shared.dataproviders.models.AbstractRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeEntryTypeRequestDto extends AbstractRequestDto {
    private String typeName;
    private boolean name;
    private boolean startTime;
    private boolean endTime;
    private boolean date;
    private boolean description;
    private boolean workPackageUuid;
    private boolean employeeUuid;
}
