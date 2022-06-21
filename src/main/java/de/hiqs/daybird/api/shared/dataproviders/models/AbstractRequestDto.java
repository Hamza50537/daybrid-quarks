package de.hiqs.daybird.api.shared.dataproviders.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbstractRequestDto {
    private String uuid;
    private Instant createdAt;
    private String createdFrom;
    private Instant modifiedAt;
    private String modifiedFrom;
    private boolean archived;
}
