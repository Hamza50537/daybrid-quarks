package de.hiqs.daybird.api.modules.customer.entrypoints.models;

import de.hiqs.daybird.api.shared.entrypoints.models.AbstractDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto extends AbstractDto {
    private String name;
    private String sign;
    private String email;
    private String phoneNumber;
    private String addressUuid;
}
