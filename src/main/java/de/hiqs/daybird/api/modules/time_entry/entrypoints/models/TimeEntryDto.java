package de.hiqs.daybird.api.modules.time_entry.entrypoints.models;

import de.hiqs.daybird.api.shared.entrypoints.models.AbstractDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TimeEntryDto extends AbstractDto {
    private String name;
    private LocalTime start;
    private LocalTime end;
    private LocalDate date;
    private String description;
    private String workPackageUuid;
    private String timeEntryTypeUuid;
    private String employeeUuid;
}
