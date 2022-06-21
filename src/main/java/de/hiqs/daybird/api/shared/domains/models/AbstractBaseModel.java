package de.hiqs.daybird.api.shared.domains.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractBaseModel {
    private String uuid;
    private boolean archived;
    private EntityTypeEnum entityTypeEnum;
}
