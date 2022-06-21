package de.hiqs.daybird.api.modules.customer.entrypoints.models;

import de.hiqs.daybird.api.shared.entrypoints.models.AbstractUpdateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUpdateDto extends AbstractUpdateDto {
    String name;
    String sign;
    String email;
    String phoneNumber;
    String addressUuid;
}
