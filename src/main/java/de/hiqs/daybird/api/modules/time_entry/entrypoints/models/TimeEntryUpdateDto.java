package de.hiqs.daybird.api.modules.time_entry.entrypoints.models;

import de.hiqs.daybird.api.shared.entrypoints.models.AbstractUpdateDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TimeEntryUpdateDto extends AbstractUpdateDto {
    String name;
    LocalTime start;
    LocalTime end;
    LocalDate date;
    String description;
    String workPackageUuid;
    String timeEntryTypeUuid;
}
