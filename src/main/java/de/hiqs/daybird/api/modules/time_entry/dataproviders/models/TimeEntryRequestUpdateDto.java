package de.hiqs.daybird.api.modules.time_entry.dataproviders.models;

import de.hiqs.daybird.api.shared.dataproviders.models.AbstractRequestUpdateDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TimeEntryRequestUpdateDto extends AbstractRequestUpdateDto {
    String name;
    LocalTime start;
    LocalTime end;
    LocalDate date;
    String description;
    String workPackageUuid;
    String timeEntryTypeUuid;
}
