package de.hiqs.daybird.api.modules.time_entry_type.dataproviders.models;

import de.hiqs.daybird.api.shared.dataproviders.models.AbstractRequestUpdateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeEntryTypeRequestUpdateDto extends AbstractRequestUpdateDto {
    String typeName;
    boolean name;
    boolean startTime;
    boolean endTime;
    boolean date;
    boolean description;
    boolean workPackageUuid;
    boolean employeeUuid;
}
