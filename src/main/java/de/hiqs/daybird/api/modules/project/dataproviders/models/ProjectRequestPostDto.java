package de.hiqs.daybird.api.modules.project.dataproviders.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectRequestPostDto {
    private String name;
    private String sign;
    private LocalDate startDate;
    private LocalDate endDate;
    private String customerUuid;
}
