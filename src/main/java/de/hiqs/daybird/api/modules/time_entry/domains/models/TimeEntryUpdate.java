package de.hiqs.daybird.api.modules.time_entry.domains.models;

import de.hiqs.daybird.api.shared.domains.models.AbstractUpdateModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TimeEntryUpdate extends AbstractUpdateModel {
    String name;
    LocalTime start;
    LocalTime end;
    LocalDate date;
    String description;
    String workPackageUuid;
    String timeEntryTypeUuid;
}
