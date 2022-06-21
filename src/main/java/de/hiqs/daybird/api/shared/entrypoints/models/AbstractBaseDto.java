package de.hiqs.daybird.api.shared.entrypoints.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbstractBaseDto {
    private String uuid;
    private boolean archived;
}
