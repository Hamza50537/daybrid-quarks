package de.hiqs.daybird.api.modules.time_entry.domains.models;

import de.hiqs.daybird.api.shared.domains.models.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TimeEntry extends AbstractModel {
    private String name;
    private LocalTime start;
    private LocalTime end;
    private LocalDate date;
    private String description;
    private String workPackageUuid;
    private String timeEntryTypeUuid;
    private String employeeUuid;
}
