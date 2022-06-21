package de.hiqs.daybird.api.modules.customer.dataproviders.models;

import de.hiqs.daybird.api.shared.dataproviders.models.AbstractRequestUpdateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequestUpdateDto extends AbstractRequestUpdateDto {
    String name;
    String sign;
    String email;
    String phoneNumber;
    String addressUuid;
}
