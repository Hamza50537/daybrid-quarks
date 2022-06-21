package de.hiqs.daybird.api.modules.customer.dataproviders.models;

import de.hiqs.daybird.api.shared.dataproviders.models.AbstractRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequestDto extends AbstractRequestDto {
    private String name;
    private String sign;
    private String email;
    private String phoneNumber;
    private String addressUuid;
}
