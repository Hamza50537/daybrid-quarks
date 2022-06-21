package de.hiqs.daybird.api.shared.domains.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AbstractModel extends AbstractBaseModel {
    private Instant createdAt;
    private String createdFrom;
    private Instant modifiedAt;
    private String modifiedFrom;
}
