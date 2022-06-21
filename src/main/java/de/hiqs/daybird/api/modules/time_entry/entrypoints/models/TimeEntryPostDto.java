package de.hiqs.daybird.api.modules.time_entry.entrypoints.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeEntryPostDto {
    private String name;
    private LocalTime start;
    private LocalTime end;
    private LocalDate date;
    private String description;
    private String workPackageUuid;
    private String timeEntryTypeUuid;
    private String employeeUuid;
}
