package de.hiqs.daybird.api.modules.time_entry_type.entrypoints.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeEntryTypePostDto {
    private String typeName;
    private boolean name;
    private boolean startTime;
    private boolean endTime;
    private boolean date;
    private boolean description;
    private boolean workPackageUuid;
    private boolean employeeUuid;
}
